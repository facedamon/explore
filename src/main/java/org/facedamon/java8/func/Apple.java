package org.facedamon.java8.func;

import com.alibaba.fastjson.JSON;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

/**
 * @author damon
 * @desc 简易lambda和函数形参
 * @date 2021/5/1
 */
@Getter
@Builder
public class Apple {

    private String color;
    private Integer weight;

    public static boolean isGreenApple(Apple apple) {
        return "green".equals(apple.getColor());
    }

    public static boolean isHeavyApple(Apple apple) {
        return apple.getWeight() > 150;
    }

    static List<Apple> filterApples(List<Apple> inventory,
                                    Predicate<Apple> p) {
        List<Apple> result = new ArrayList<>();
        for (Apple apple : inventory) {
            if (p.test(apple)) {
                result.add(apple);
            }
        }
        return result;
    }

    public static void main(String[] args) {
        List<Apple> inventory = Arrays.asList(
                Apple.builder().color("green").weight(110).build(),
                Apple.builder().color("red").weight(155).build());

        // 函数作为形参
        System.out.println(JSON.toJSONString(filterApples(inventory, Apple::isGreenApple)));
        System.out.println(JSON.toJSONString(filterApples(inventory, Apple::isHeavyApple)));

        // lambda匿名函数
        filterApples(inventory, a -> "green".equals(a.getColor()));
        filterApples(inventory, a -> a.getWeight() > 150);
        filterApples(inventory, a -> "green".equals(a.getColor()) || "brown".equals(a.getColor()));
    }
}
