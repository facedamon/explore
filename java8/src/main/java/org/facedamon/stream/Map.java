package org.facedamon.stream;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Arrays;
import java.util.List;

/**
 * @author damon
 * @desc 映射
 * @date 2021/6/9
 */
public class Map {
    public static void main(String[] args) {
        //testForMap();
        //testForMultiMap();
        //testForMapWithArray();
        //testForFlatmapWithArray();
        testForMapWithPow();
    }

    private static void testForMap() {
        List<String> words = Arrays.asList("Java 8", "Lambdas", "In", "Action");
        words.stream()
                .map(String::length)
                .forEach(System.out::println);
    }

    private static void testForMultiMap() {
        List<Menu> menus = Arrays.asList(new Menu("first", 13.00), new Menu("second", 14.00));
        menus.stream()
                .map(Menu::getName)
                .map(String::length)
                .forEach(System.out::println);
    }

    @Data
    @AllArgsConstructor
    private static class Menu {
        private String name;
        private Double price;
    }

    /**
     * @author damon
     * @desc 去重反例
     * @date 13:39 2021/6/9
     **/
    private static void testForMapWithArray() {
        List<String> words = Arrays.asList("Java 8", "Lambdas", "In", "Action");
        words.stream()
                .map(word -> word.split(""))
                .map(Arrays::stream)
                .distinct()
                .forEach(System.out::println);
    }

    /**
     * @author damon
     * @desc 去重正例
     * flatMap 把一个流中的每个值都换成另一个流，然后把所有的流链接起来成为一个流
     * @date 13:40 2021/6/9
     **/
    private static void testForFlatmapWithArray() {
        List<String> words = Arrays.asList("Java 8", "Lambdas", "In", "Action");
        words.stream()
                .map(word -> word.split(""))
                .flatMap(Arrays::stream)
                .distinct()
                .forEach(System.out::println);
    }

    private static void testForMapWithPow() {
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
        numbers.stream()
                .map(n -> (int)Math.pow(n, 2))
                .map(Integer::longValue)
                .forEach(System.out::println);
    }
}
