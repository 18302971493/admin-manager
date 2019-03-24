package com.ytain.common.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: 胡国栋
 * @Description:
 * @Date:Create：in 2019/3/23 21:37
 * @Modified By：
 */
@Data
public class Result<T> implements Serializable {
    /**
     * 是否成功
     */
    private boolean success;
    /**
     * 消息
     */
    private String message;
    /**
     * 请求状态码
     */
    private Integer code;
    /**
     * 请求时间
     */
    private long timestamp=System.currentTimeMillis();
    /**
     * 返回结果
     */
    private T result;
}
