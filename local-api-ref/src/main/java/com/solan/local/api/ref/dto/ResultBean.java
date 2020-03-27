package com.solan.local.api.ref.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 *
 * @author: hyl
 * @date: 2020/3/23 15:32
 */
@ApiModel("ResultBean")
@Data
public class ResultBean<T> {
    @ApiModelProperty("code")
    private int code;
    @ApiModelProperty("message")
    private String message;
    @ApiModelProperty("data")
    private T data;

}
