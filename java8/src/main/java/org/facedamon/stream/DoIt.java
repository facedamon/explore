package org.facedamon.stream;

import com.sun.java.accessibility.util.Translator;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author damon
 * @desc 实践
 * @date 2021/6/9
 */
public class DoIt {

    @Data
    @AllArgsConstructor
    private static class Trader {
        private final String name;
        private final String city;
    }
    @Data
    @AllArgsConstructor
    private static class Transaction {
        private final Trader trader;
        private final int year;
        private final int value;
    }
    private static List<Transaction> transactions;

    static {
        Trader trader = new Trader("张三", "北京");
        Trader trader1 = new Trader("张三", "成都");
        Trader trader2 = new Trader("王五", "北京");
        Trader trader3 = new Trader("马六", "成都");
        transactions = Arrays.asList(
                new Transaction(trader, 2011, 100),
                new Transaction(trader, 2012, 1100),
                new Transaction(trader1, 2013, 1100),
                new Transaction(trader2, 2011, 800),
                new Transaction(trader3, 2011, 600));
    }

    public static void main(String[] args) {
        //testOne();
        //testTwo();
        //testThree();
        //testFour();
        //testFive();
        //testSix();
        testSeven();
    }

    /**
     * @author damon
     * @desc 找出2011年的所有交易并按交易额排序
     * @date 16:00 2021/6/9
     **/
    public static void testOne() {
        transactions.stream()
                .filter(t -> t.getYear() == 2011)
                .sorted(Comparator.comparing(Transaction::getValue).reversed())
                .forEach(System.out::println);
    }

    /**
     * @author damon
     * @desc 交易员都在哪些不同城市呆过
     * @date 16:02 2021/6/9
     **/
    public static void testTwo() {
        transactions.stream()
                .map(t -> t.getTrader().getCity())
                .distinct()
                .forEach(System.out::println);

    }

    /**
     * @author damon
     * @desc 查找所有来自于成都的交易员,按照姓名排序
     * @date 16:05 2021/6/9
     **/
    public static void testThree() {
        transactions.stream()
                .map(Transaction::getTrader)
                .filter(t -> t.getCity().equals("成都"))
                .distinct()
                .sorted(Comparator.comparing(Trader::getName))
                .forEach(System.out::println);
    }

    /**
     * @author damon
     * @desc 返回所有交易员的姓名字符串，按字母顺序排序
     * @date 16:08 2021/6/9
     **/
    public static void testFour() {
        transactions.stream()
                .map(t -> t.getTrader().getName())
                .distinct()
                .sorted()
                //.reduce(String::concat) 效率低
                //.ifPresent(System.out::println);
                .collect(Collectors.joining(","));
    }

    /**
     * @author damon
     * @desc 有没有交易员在北京工作
     * @date 16:13 2021/6/9
     **/
    public static void testFive() {
        if (transactions.stream()
                .anyMatch(t -> t.getTrader().getCity().equals("北京"))) {
            System.out.println("yes!");
        }
    }

    /**
     * @author damon
     * @desc 打印生活在成都的交易员的所有交易额
     * @date 16:15 2021/6/9
     **/
    public static void testSix() {
        transactions.stream()
                .filter(t -> "成都".equals(t.getTrader().getCity()))
                .map(Transaction::getValue)
                .forEach(System.out::println);
    }

    /**
     * @author damon
     * @desc 所有交易中最高的交易额是多少
     * @date 16:19 2021/6/9
     **/
    public static void testSeven() {
        transactions.stream()
                .map(Transaction::getValue)
                //.reduce(Integer::max)
                //.ifPresent(System.out::println);
                .max(Integer::compare)
                .ifPresent(System.out::println);
    }

}
