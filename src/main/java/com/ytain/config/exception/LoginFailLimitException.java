package com.ytain.config.exception;

import com.ytain.modules.sys.entity.Log;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.AuthenticationException;

/**
 * @Author: 胡国栋
 * @Description: 登陆异常类
 * @Date:Create：in 2019/3/23 20:45
 * @Modified By：
 */
public class LoginFailLimitException extends AuthenticationException {
    @Getter
    @Setter
    private String msg;
    public LoginFailLimitException(String msg){
        super(msg);
        this.msg=msg;
    }
    public LoginFailLimitException(String msg, Throwable t){
        super(msg,t);
        this.msg=msg;
    }
}
