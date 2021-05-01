package org.facedamon.java8.stream;

import com.alibaba.fastjson.JSON;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author damon
 * @desc 简易stream
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

        //顺序处理
        System.out.println(JSON.toJSONString(inventory.stream()
                .filter(a -> a.getWeight() > 150)
                .collect(Collectors.toList())));
        //并行处理
        System.out.println(JSON.toJSONString(inventory.parallelStream()
                .filter(Apple::isGreenApple)
                .collect(Collectors.toList())));
    }
}
