package com.solan.jbase.stream;

import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.time.StopWatch;

import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author hyl
 * @Package com.my.stream
 * @date 2020/1/6 10:00
 */
public class ListStreamApp {
    private static List<UserInfo> userList = new ArrayList<>();

    static {
        for (int i = 1; i <= 20; i++) {
            UserInfo user = new UserInfo(i, "solan" + i, "上海市",
                    RandomUtils.nextFloat(100.00f, 10000.00f),
                    RandomUtils.nextInt(1, 5));
            userList.add(user);
        }
    }

    private static PersonSupplier personSupplier = new PersonSupplier(5);

    public static void main(String[] args) throws InterruptedException {
        // CountDownLatch countDownLatch=new CountDownLatch(10000);
        Map<Long, String> map = new HashMap<>();
        userList.parallelStream().forEach(u -> {
            map.putIfAbsent(Thread.currentThread().getId(), Thread.currentThread().getName());
            System.out.println(String.format("%s: %s", Thread.currentThread().getId(), u.toString()));
        });
        map.forEach((k, v) -> {
            System.out.println(String.format("%s -> %s", k, v));
        });
        userList.stream().skip(2).limit(3).forEach(System.out::println);
        String[] arr = new String[]{"a,b,c", "1,2", "d,3,e,4"};
        Stream<String> words = Stream.of(arr).flatMap(line -> Stream.of(line.split(",")));
        String str = words.map(e -> e.toUpperCase()).sorted().reduce("", String::concat);
        System.out.println(str);

        //generate
        Stream<UserInfo> userStream = Stream.generate(personSupplier).limit(10);
        userStream.forEach(System.out::println);
        Map<Integer, List<UserInfo>> map1 = Stream.generate(personSupplier).limit(10).collect(Collectors.groupingBy(UserInfo::getDeptId));
        map1.entrySet().forEach(entry -> System.out.println(String.format("%s: %s", entry.getKey(), entry.getValue())));
        Stream.generate(personSupplier).limit(10).collect(Collectors.groupingBy(UserInfo::getDeptId, Collectors.counting()))
                .entrySet().forEach(e -> System.out.println(String.format("%s: %s", e.getKey(), e.getValue())));
        //iterate
        str = Stream.iterate(0, n -> n + 2).limit(5).map(i -> String.valueOf(i) + ",")
                .reduce("", String::concat);
        System.out.println(str);


    }

    private static class PersonSupplier implements Supplier<UserInfo> {
        private int index = 0;

        public PersonSupplier(int index) {
            this.index = index;
        }

        public int getIndex() {
            return this.index;
        }

        @Override
        public UserInfo get() {
            this.index++;
            UserInfo user = new UserInfo(this.index, "solan" + this.index, "上海市",
                    RandomUtils.nextFloat(100.00f, 10000.00f),
                    RandomUtils.nextInt(1, 5));
            return user;
        }
    }
}
