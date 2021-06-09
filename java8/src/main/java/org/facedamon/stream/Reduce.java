package org.facedamon.stream;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

/**
 * @author damon
 * @desc
 * @date 2021/6/9
 */
public class Reduce {

    public static void main(String[] args) {
        //testForReduceWithSum();
        //testForReduceWithMaxOrMin();
        testForMapReduce();
    }

    /**
     * @author damon
     * @desc 求和
     * @date 14:40 2021/6/9
     **/
    private static void testForReduceWithSum() {
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
        System.out.println(numbers.stream()
                .reduce(0, Integer::sum));
    }

    private static void testForReduceWithMaxOrMin() {
        //System.out.println(Stream.of(1, 2, 3, 4, 5).min(Integer::compare).get());
        Stream.of(1, 2, 3, 4, 5).reduce(Integer::max).ifPresent(System.out::println);
    }

    /**
     * @author damon
     * @desc 使用map-reduce统计count
     * @date 15:23 2021/6/9
     **/
    private static void testForMapReduce() {
        Stream.of(10, 23, 57, 23).map(d -> 1).reduce(Integer::sum).ifPresent(System.out::println);
    }
}
