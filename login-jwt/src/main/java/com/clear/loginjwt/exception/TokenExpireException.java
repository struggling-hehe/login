package com.clear.loginjwt.exception;

import lombok.extern.slf4j.Slf4j;

/**
 * @author jiexipeng@kongfz.com
 * @date 2021/11/20  4:50 下午
 */
@Slf4j
public class TokenExpireException extends CustomerAuthenticationException{
    public TokenExpireException(String msg, Throwable t) {
        super(msg, t);
        log.error("Token过期异常 msg -> {}", msg, t);
    }

    public TokenExpireException(String msg) {
        super(msg);
        log.error("Token过期异常 msg -> {}", msg);
    }
}
