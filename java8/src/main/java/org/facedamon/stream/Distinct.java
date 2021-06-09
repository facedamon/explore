package org.facedamon.stream;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * @author damon
 * @desc
 * @date 2021/6/9
 */
public class Distinct {
    public static void main(String[] args) {
        //testForDistinct();
        //testForLimit();
        testForSkip();
    }

    private static void testForDistinct() {
        List<Integer> numbers = Arrays.asList(1, 2, 1, 3, 3, 2, 4);
        numbers.stream()
                .filter(i -> i % 2 == 0)
                .distinct()
                .forEach(System.out::println);
        // 2 4
    }

    private static void testForLimit() {
        List<Integer> numbers = Arrays.asList(1, 2, 1, 3, 3, 2, 4);
        numbers.stream()
                .filter(i -> i % 2 == 0)
                .limit(3)
                .forEach(System.out::println);
        // 2 2 4
    }

    private static void testForSkip() {
        List<Integer> numbers = Arrays.asList(1, 2, 1, 3, 3, 2, 4);
        numbers.stream()
                .filter(i -> i % 2 == 0)
                .skip(2)
                .forEach(System.out::println);
        // 4
    }
}
