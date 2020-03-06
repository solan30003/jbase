package com.solan.jbase.stream.collect;

import com.solan.jbase.stream.UserInfo;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * @author: hyl
 * @date: 2020/1/22 16:11
 */
public class StreamCollectApp {
    public static void main(String[] args) {
        List<UserInfo> list = UserInfo.build(100);
        Map<Integer, String> map = list.stream().collect(Collectors.toMap(UserInfo::getId, UserInfo::getName));
        System.out.println(map.keySet().size());
        Map<Integer, Long> map1 = list.stream().collect(Collectors.groupingBy(UserInfo::getDeptId, Collectors.counting()));
        System.out.println(map1);
    }
}
