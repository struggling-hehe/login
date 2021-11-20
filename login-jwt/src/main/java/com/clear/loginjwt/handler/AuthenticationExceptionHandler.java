package com.clear.loginjwt.handler;

import com.clear.loginjwt.exception.CustomerAuthenticationException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author jiexipeng@kongfz.com
 * @date 2021/11/20  4:54 下午
 */
public interface AuthenticationExceptionHandler {
    /**
     * 自定义通用认证异常以及子类异常处理器
     *
     * @param request                         HttpServletRequest
     * @param response                        HttpServletResponse
     * @param customerAuthenticationException 自定义通用认证异常以及子类
     * @throws IOException      IO异常
     * @throws ServletException Servlet 异常
     */
    void onAuthenticationException(HttpServletRequest request,
                                   HttpServletResponse response,
                                   CustomerAuthenticationException customerAuthenticationException) throws IOException, ServletException;
}
