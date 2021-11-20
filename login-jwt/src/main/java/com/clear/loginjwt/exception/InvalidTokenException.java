package com.clear.loginjwt.exception;

import lombok.extern.slf4j.Slf4j;

/**
 * @author jiexipeng@kongfz.com
 * @date 2021/11/20  4:51 下午
 */

@Slf4j
public class InvalidTokenException extends CustomerAuthenticationException{
    public InvalidTokenException(String msg, Throwable t) {
        super(msg, t);
        log.error("无效的Token异常 msg -> {}, {}", msg, t);
    }

    public InvalidTokenException(String msg) {
        super(msg);
        log.error("无效的Token异常 msg -> {}", msg);
    }
}
