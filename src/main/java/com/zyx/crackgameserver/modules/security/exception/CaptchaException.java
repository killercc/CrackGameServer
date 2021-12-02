package com.zyx.crackgameserver.modules.security.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * 自定义验证码错误异常
 */
public class CaptchaException extends AuthenticationException {

    public CaptchaException(String msg) {
        super(msg);
    }
}
