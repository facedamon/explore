package org.facedamon.func;

/**
 * @author damon
 * @desc 多线程
 * @date 2021/5/1
 */
public class Runnable2 {
    public static void main(String[] args) {
        // 匿名类
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("Hello World");
            }
        });
        t.start();

        // Lambda
        Thread t1 = new Thread(() -> System.out.println("Hello World"));
        t1.start();


    }
}
