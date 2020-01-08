package com.my.lambda;

import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;
import java.util.function.*;

/**
 * @author hyl
 * @Package com.my.lambda
 * @date 2020/1/3 21:02
 */
public class LambdaApp {
    public static void main(String[] args) {
        Consumer<String> consumer = (name) -> System.out.println("hello " + name);
        consumer.accept("sloan");
        Supplier<String> supplier = () -> LocalDateTime.now().toString();
        System.out.println(supplier.get());
        Function<String, Integer> function = new Function<String, Integer>() {
            @Override
            public Integer apply(String s) {
                return s.length();
            }
        };
        System.out.println(function.apply("hello"));
        Predicate<String> predicate = new Predicate<String>() {
            @Override
            public boolean test(String s) {
                return StringUtils.isNotEmpty(s);
            }
        };
        System.out.println(predicate.test(null));
        System.out.println("Hello World!");
    }
}
