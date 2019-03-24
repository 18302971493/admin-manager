package com.ytain.config.exception;

import lombok.Data;

/**
 * @Author: 胡国栋
 * @Description:
 * @Date:Create：in 2019/3/24 13:01
 * @Modified By：
 */
@Data
public class BaseException extends RuntimeException {
    private String msg;

    public BaseException(String msg){
        super(msg);
        this.msg=msg;
    }
}
