package org.facedamon.stream;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

/**
 * @author damon
 * @desc 数值类stream
 * @date 2021/6/9
 */
public class NumberStream {
    public static void main(String[] args) {
        //testForInt();
        testForIntNew();
    }

    private static void testForInt() {
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
        numbers.stream()
                .mapToInt(Integer::intValue)
                .max()
                .ifPresent(System.out::println);
    }

    private static void testForIntNew() {
        IntStream.rangeClosed(1, 5)
                .max()
                .ifPresent(System.out::println);
    }
}
