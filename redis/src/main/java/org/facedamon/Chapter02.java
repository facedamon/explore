package org.facedamon;

import redis.clients.jedis.Jedis;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;

/**
 * @author damon
 * @desc
 * @date 2021/5/25
 */
public class Chapter02 {
    public static void main(String[] args) throws InterruptedException {
        new Chapter02().run();
    }

    private void run() throws InterruptedException {
        Jedis conn = new Jedis("127.0.0.1");
        conn.select(15);
        
        //testLoginCookies(conn);
        //testShoppingCartCookies(conn);
        testCacheRequest(conn);
    }

    private void testLoginCookies(Jedis conn) throws InterruptedException {
        System.out.println("\n----- testLoginCookies -----");
        String token = UUID.randomUUID().toString();
        updateToken(conn, token, "jack", "itemX");
        System.out.println("We just logged-in/updated token: " + token);
        System.out.println("For user: 'jack'\n");

        System.out.println("What username do we get when we look-up that token?");
        String r = checkToken(conn, token);
        System.out.println(r+"\n");
        assert  r != null;

        System.out.println("Let`s drop the maximum number of cookies to 0 to clean them out");
        System.out.println("We will start a thread to do the cleaning, while we stop it later");
        
        CleanSessionThread thread = new CleanSessionThread(0);
        thread.start();
        Thread.sleep(1000);
        thread.quit();
        Thread.sleep(2000);
        if (thread.isAlive()) {
            throw new RuntimeException("The clean session thread is still alive!!!");
        }

        long s = conn.hlen("login:");
        System.out.println("The current number of sessions still available is " + s);
        assert s == 0;

    }

    private String checkToken(Jedis conn, String token) {
        return conn.hget("login:", token);
    }

    private void testShoppingCartCookies(Jedis conn) throws InterruptedException{
        System.out.println("\n----- testShoppingCartCookies -----");
        String token = UUID.randomUUID().toString();
        System.out.println("We`ll refresh our session...");
        updateToken(conn, token, "jack", "itemX");
        System.out.println("And add an item to the shopping cart");
        addToCart(conn, token, "itemY", 3);
        Map<String, String> r = conn.hgetAll("cart:" + token);
        System.out.println("Our shopping cart currently has:");
        r.forEach((k,v) -> {
            System.out.println(" " + k + ": " + v);
        });
        System.out.println();
        assert r.size() >= 1;

        System.out.println("Let`s clean out our sessions and carts");
        CleanFullSessionsThread thread = new CleanFullSessionsThread(0);
        thread.start();
        Thread.sleep(1000);
        thread.quit();
        Thread.sleep(2000);
        if (thread.isAlive()) {
            throw new RuntimeException("The clean sessions thread is still alive!!!");
        }
        r = conn.hgetAll("cart:" + token);
        System.out.println("Our shopping cart now contains:");
        if (Objects.isNull(r) || r.isEmpty()) {
            System.out.println("Empty");
        }
        assert r.size() == 0;
    }

    private void addToCart(Jedis conn, String session, String item, int count) {
        if (count <= 0) {
            //????????????????????????0??????????????????????????????session???item??????
            conn.hdel("cart:" + session, item);
        } else {
            conn.hset("cart:" + session, item, String.valueOf(count));
        }
    }

    private void testCacheRequest(Jedis conn) {
        System.out.println("\n----- testCacheRequest -----");
        String token = UUID.randomUUID().toString();
        Callback callback = r -> "content for " + r;
        updateToken(conn, token, "jack", "itemX");
        String url = "http://test.com/?item=itemX";
        System.out.println("We are going to cache a simple request against " + url);
        String result = cacheRequest(conn, url, callback);
        System.out.println("We got initial content:" + result);
        System.out.println();
        assert  result != null;

        assert !canCache(conn, "http://test.com/");
        assert !canCache(conn, "http://test.com/?item=itemX&_=123456");
    }

    private String cacheRequest(Jedis conn, String request, Callback callback) {
        if (!canCache(conn, request)) {
            return callback != null ? callback.call(request) : null;
        }

        String pageKey = "cache:" + hashRequest(request);
        String content = conn.get(pageKey);
        if (null == content && callback != null) {
            content = callback.call(request);
            conn.setex(pageKey, 300, content);
        }
        return content;
    }

    /**
     *
     *  ??????????????????????????????????????????????????????????????????????????????
     *  ????????????expire????????????????????????????????????????????????????????????????????????????????????
     *  ??????????????????????????????????????????????????????????????????????????????
     *  ??????
     *  ???????????????????????????????????????1000w?????????
     *
     **/
    private void updateToken(Jedis conn, String token, String username, String item) {
        long second = LocalDateTime.now().toEpochSecond(ZoneOffset.of("+8"));
        //token?????????username??????
        conn.hset("login:", token, username);
        //token?????????????????????????????? (??????)
        conn.zadd("recent:", second, token);
        //??????????????????
        if (!Objects.isNull(item)) {
            //???token??????item??????
            conn.zadd("viewed:" + token, second, item);
            //?????????25?????????
            conn.zremrangeByRank("viewed:" + token, 0, -26);
            //TODO why   ???item??????-1???
            conn.zincrby("viewed:", -1, item);
        }
    }

    @FunctionalInterface
    public interface Callback {
        String call(String request);
    }

    public boolean canCache(Jedis conn, String request) {
        try {
            URL url = new URL(request);
            Map<String, String> params = new HashMap<>();
            if (!Objects.isNull(url.getQuery())) {
                for (String param: url.getQuery().split("&")) {
                    String[] pair = param.split("=", 2);
                    params.put(pair[0], pair.length == 2 ? pair[1] : null);
                }
            }

            String itemId = extractItemId(params);
            if (itemId == null || isDynamic(params)) {
                return false;
            }
            Long rank = conn.zrank("viewed:", itemId);
            return rank != null && rank < 10000;
        } catch (MalformedURLException e) {
            return false;
        }
    }

    public String extractItemId(Map<String, String> params) {
        return params.get("item");
    }

    public boolean isDynamic(Map<String,String> params) {
        return params.containsKey("_");
    }

    public String hashRequest(String request) {
        return String.valueOf(request.hashCode());
    }

    // ????????????limit100????????????token??????
    // ?????? login???recent???viewed
    public class CleanSessionThread extends Thread {
        private Jedis conn;
        private int limit;
        private boolean quit;

        public CleanSessionThread(int limit) {
            this.conn = new Jedis("127.0.0.1");
            this.conn.select(15);
            this.limit = limit;
        }

        public void quit() {
            quit = true;
        }

        @Override
        public void run() {
            while (!quit) {
                long size = conn.zcard("recent:");
                if (size <= limit) {
                    try {
                        sleep(1000);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    continue;
                }
                long endIndex = Math.min(size - limit, 100);
                Set<String> tokenSet = conn.zrange("recent:", 0, endIndex - 1);
                String[] tokens = tokenSet.stream().toArray(String[]::new);
                //??????login ??????token??????
                conn.hdel("login:", tokens);
                //??????recent ??????token??????
                conn.zrem("recent:", tokens);
                //??????viewed ??????token??????
                Queue<String> viewed = new ArrayDeque<>();
                for (String token: tokens) {
                    viewed.add("viewed:"+token);
                }
                conn.del(viewed.stream().toArray(String[]::new));
            }
        }
    }

    // ????????????limit100????????????token??????
    // ?????? login???recent???viewed???cart
    public class CleanFullSessionsThread extends Thread{
        private Jedis conn;
        private int limit;
        private boolean quit;

        public CleanFullSessionsThread(int limit) {
            this.conn = new Jedis("127.0.0.1");
            this.conn.select(15);
            this.limit = limit;
        }

        public void quit() {
            quit = true;
        }

        @Override
        public void run() {
            while (!quit) {
                long size = conn.zcard("recent:");
                if (size <= limit) {
                    try {
                        sleep(1000);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    continue;
                }
                long endIndex = Math.min(size - limit, 100);
                Set<String> sessionSet = conn.zrange("recent:", 0, endIndex - 1);
                String[] sessions = sessionSet.stream().toArray(String[]::new);
                conn.hdel("login:", sessions);
                conn.zrem("recent:", sessions);

                Queue<String> cart = new ArrayDeque<>();
                for (String session: sessions) {
                    cart.add("viewed:" + session);
                    cart.add("cart:" + session);
                }
                conn.del(cart.stream().toArray(String[]::new));
            }
        }
    }
}
