package org.facedamon;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.ZParams;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;

/**
 * 1. 文章 hash
 * article:id {title, link, now, voted}
 * id 利用redis incr自增
 * now 时间，获取当前时间距离1970的秒数
 * voted 投票数
 * <p>
 * 2. 分数 zset
 * score: {分数，文章id}
 * 分数：now + VOTE_SCORE
 * 3. 时间 zset
 * time: {时间，文章id}
 * 4. 投票
 * 判断当前时间秒数是否超过一周，如果超过一周表示文章过期，不能投票
 * 投票集合 set
 * voted:id {投票者}
 * 更新voted:id投票者信息
 * 更新score分数，增加 VOTE_SCORE
 * 更新文章hash，hash中的voted自增
 * 5. set分页
 * zrevrange 按照分数逆序
 */
public class Chapter01 {

    // 86400 表示每天的秒数
    private static final int ONE_WEEK_IN_SECONDS = 7 * 86400;
    private static final int VOTE_SCORE = 86400 / 200;
    private static final int ARTICLES_PER_PAGE = 25;

    public static String format(Object string) {
        return JSON.toJSONString(string, SerializerFeature.PrettyFormat);
    }

    public static void main(String[] args) {
        Chapter01 chapter01 = new Chapter01();
        chapter01.run();
    }

    public void run() {
        Jedis jedis = new Jedis("127.0.0.1");
        jedis.select(15);
        String articleId = this.postArticle(jedis, "username", "A title", "http://www.google.com");
        System.out.println("We posted a new article with id: " + articleId);
        System.out.println("Its HASH looks like:");
        Map<String, String> articleData = jedis.hgetAll("article:" + articleId);
        articleData.forEach((k, v) -> {
            System.out.println(" " + k + ":" + v);
        });

        System.out.println();

        this.articleVote(jedis, "other_user", "article:" + articleId);
        String votes = jedis.hget("article:" + articleId, "votes");
        System.out.println("We voted for the article, it now has votes: " + votes);
        assert Integer.parseInt(votes) > 1;

        System.out.println();

        System.out.println("The currently highest-scoring articles are:");
        List<Map<String, String>> articles = getArticles(jedis, 1);
        this.printArticles(articles);
        assert articles.size() >= 1;
    }

    //发布文章
    public String postArticle(Jedis conn, String user, String title, String link) {
        //获取文章id
        String articleId = String.valueOf(conn.incr("article:"));
        //建立此文章的投票用户列表,并设置一周的有效期。文章一周内可以投票，一周后无法投票并删除投票用户列表
        String voted = String.format("voted:%s", articleId);
        conn.sadd(voted, user);
        conn.expire(voted, ONE_WEEK_IN_SECONDS);
        //建立文章对象
        long second = LocalDateTime.now().toEpochSecond(ZoneOffset.of("+8"));
        String article = String.format("article:%s", articleId);
        HashMap<String, String> articleData = new HashMap<>();
        articleData.put("title", title);
        articleData.put("link", link);
        articleData.put("now", String.valueOf(second));
        //域 投票数
        articleData.put("votes", "1");
        conn.hmset(article, articleData);
        //建立文章分数集合
        conn.zadd("score:", second + VOTE_SCORE, article);
        //建立文章发布时间集合
        conn.zadd("time:", second, article);
        return articleId;
    }

    //投票
    public void articleVote(Jedis conn, String user, String article) {
        //当前秒数是否超过一周
        long cutoff = LocalDateTime.now().toEpochSecond(ZoneOffset.of("+8")) - ONE_WEEK_IN_SECONDS;
        if (conn.zscore("time:", article) < cutoff) {
            return;
        }

        String articleId = article.substring(article.indexOf(':') + 1);
        //更新投票集合
        if (conn.sadd("voted:" + articleId, user) == 1) {
            //给score中的article的分数加VOTE_SCORE
            conn.zincrby("score:", VOTE_SCORE, article);
            //给hash article中的votes域加1
            conn.hincrBy(article, "votes", 1);
        }
    }

    //获取文章,按照评分排序
    public List<Map<String, String>> getArticles(Jedis conn, int page) {
        return getArticles(conn, page, "score:");
    }

    private List<Map<String, String>> getArticles(Jedis conn, int page, String order) {
        int start = (page - 1) * ARTICLES_PER_PAGE;
        int end = start + ARTICLES_PER_PAGE - 1;

        Set<String> ids = conn.zrevrange(order, start, end);
        List<Map<String, String>> articles = new ArrayList<>();
        for (String id : ids) {
            Map<String, String> articleData = conn.hgetAll(id);
            articleData.put("id", id);
            articles.add(articleData);
        }
        return articles;
    }

    public void addGroups(Jedis conn, String articleId, String[] toAdd) {
        String article = "article:" + articleId;
        for (String group : toAdd) {
            conn.sadd("group:" + group, article);
        }
    }

    public List<Map<String, String>> getGroupArticles(Jedis conn, String group, int page) {
        return getGroupArticles(conn, group, page, "score:");
    }

    private List<Map<String, String>> getGroupArticles(Jedis conn, String group, int page, String order) {
        String key = order + group;
        //缓存60s
        if (!conn.exists(key)) {
            ZParams params = new ZParams().aggregate(ZParams.Aggregate.MAX);
            conn.zinterstore(key, params, "group:" + group, order);
            conn.expire(key, 60);
        }
        return getArticles(conn, page, key);
    }

    private void printArticles(List<Map<String, String>> articles) {
        articles.forEach(a -> {
            System.out.println(" id: " + a.get("id"));
            a.forEach((k, v) -> {
                if (k.equals("id")) {
                    return;
                }
                System.out.println(" " + k + ": " + v);
            });
        });
    }
}
