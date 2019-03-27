package com.ytain.config.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * @author Administrator
 */
public class ValidateCodeException extends AuthenticationException {
    public ValidateCodeException(String msg) {
        super(msg);
    }
}
