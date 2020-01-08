package com.my.stream;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author hyl
 * @Package com.my.stream
 * @date 2020/1/6 10:03
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserInfo {
    private int id;
    private String name;
    private String address;
    private float salary;
    private int deptId;
}
