package com.solan.localfdfs.infrastructure;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: hyl
 * @date: 2020/3/5 16:45
 */
@Data
public class ResultBean<T> {
    private int code;
    private String message;
    private Map<String, String> extras;
    private T data;

    public ResultBean() {
        this.extras = new HashMap<>();
    }

    public ResultBean(int code) {
        this();
        this.code = code;
    }

    public ResultBean(int code, String message) {
        this(code);
        this.message = message;
    }
}
