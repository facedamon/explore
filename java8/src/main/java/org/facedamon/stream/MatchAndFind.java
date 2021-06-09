package org.facedamon.stream;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Arrays;
import java.util.List;

/**
 * @author damon
 * @desc 匹配
 * @date 2021/6/9
 */
public class MatchAndFind {

    @Data
    @AllArgsConstructor
    private static class Menu {
        private String name;
        private Double price;
        private Boolean isVegetarian;
        //热量
        private Double calories;
    }

    public static void main(String[] args) {
        //testForAnyMatch();
        //testForAllMatch();
        //testForNoneMatch();
        testForFindFirst();
    }

    /**
     * @author damon
     * @desc 至少匹配一个
     * @date 13:59 2021/6/9
     **/
    private static void testForAnyMatch() {
        List<Menu> menus =
                Arrays.asList(new Menu("males", 13.79, Boolean.TRUE, 1298.00),
                        new Menu("vegetarian", 15.89, Boolean.FALSE, 487.87));
        if (menus.stream().anyMatch(Menu::getIsVegetarian)) {
            System.out.println("The menu is vegetarian friendly!");
        }
    }

    /**
     * @author damon
     * @desc 全部匹配
     * @date 13:59 2021/6/9
     **/
    private static void testForAllMatch() {
        List<Menu> menus =
                Arrays.asList(new Menu("males", 13.79, Boolean.TRUE, 1298.00),
                        new Menu("vegetarian", 15.89, Boolean.FALSE, 487.87));
        if (menus.stream().allMatch(d -> d.getCalories() < 1000)) {
            System.out.println("The menu is all friendly!");
        }
    }

    /**
     * @author damon
     * @desc 没有一个匹配
     * @date 14:02 2021/6/9
     **/
    private static void testForNoneMatch() {
        List<Menu> menus =
                Arrays.asList(new Menu("males", 13.79, Boolean.TRUE, 1298.00),
                        new Menu("vegetarian", 15.89, Boolean.FALSE, 487.87));
        if (menus.stream().noneMatch(d -> d.getCalories() > 2000)) {
            System.out.println("The menus have none 2000 calories!");
        }
    }

    /**
     * @author damon
     * @desc 如果你不关心返回的元素是哪个，请使用findAny
     * @date 14:19 2021/6/9
     **/
    private static void testForFindFirst() {
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
        numbers.stream()
                .map(x -> x * x)
                .filter(x -> x % 3 == 0)
                .findFirst()
                .ifPresent(System.out::println);
        // 9
    }
}
