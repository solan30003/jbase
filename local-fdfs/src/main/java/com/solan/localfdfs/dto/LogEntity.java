package com.solan.localfdfs.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 日志实体
 *
 * @author: hyl
 * @date: 2020/3/30 15:47
 */
@Data
public class LogEntity implements Serializable {
    private String traceId;
    private String project;
    private String product;
    private String module;
    private String fun;
    private String optType;
    private String classFullName;
    private String message;
    private String level;
    private Exception ex;
    private String timestmp;
    private String loggerName;
    private String threadName;
}
