package org.facedamon.func;

import java.util.Arrays;
import java.util.List;

/**
 * @author damon
 * @desc 比较演化
 * @date 2021/5/1
 */
public class Comparator2 {
    public static void main(String[] args) {
        List<Apple> inventory = Arrays.asList(
                Apple.builder().color("green").weight(110).build(),
                Apple.builder().color("red").weight(155).build());

        // 使用匿名类
        inventory.sort(new java.util.Comparator<Apple>() {
            @Override
            public int compare(Apple o1, Apple o2) {
                return o1.getWeight().compareTo(o2.getWeight());
            }
        });

        // Lambda
        inventory.sort((Apple a1, Apple a2) -> a1.getWeight().compareTo(a2.getWeight()));

        // Comparator 方法引用
        inventory.sort(java.util.Comparator.comparing(Apple::getColor));
    }
}
