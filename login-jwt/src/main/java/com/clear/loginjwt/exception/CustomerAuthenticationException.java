package com.clear.loginjwt.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;

/**
 * @author jiexipeng@kongfz.com
 * @date 2021/11/20  4:48 下午
 */
@Slf4j
public class CustomerAuthenticationException  extends AuthenticationException {

    public CustomerAuthenticationException(String msg, Throwable t) {
        super(msg, t);
        log.error("自定义认证异常 msg -> {}, {}", msg, t);
    }

    public CustomerAuthenticationException(String msg) {
        super(msg);
        log.error("自定义认证异常 msg -> {}", msg);
    }
}
