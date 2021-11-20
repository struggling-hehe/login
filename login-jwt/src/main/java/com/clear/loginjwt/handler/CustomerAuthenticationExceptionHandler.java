package com.clear.loginjwt.handler;

import com.clear.loginjwt.config.result.CommonCode;
import com.clear.loginjwt.constant.AuthConstants;
import com.clear.loginjwt.exception.CustomerAuthenticationException;
import com.clear.loginjwt.exception.InvalidTokenException;
import com.clear.loginjwt.exception.TokenExpireException;
import com.clear.loginjwt.pojo.core.Result;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author jiexipeng@kongfz.com
 * @date 2021/11/20  4:55 下午
 *
 * 自定义通用认证异常处理器
 */
@Component
public class CustomerAuthenticationExceptionHandler implements AuthenticationExceptionHandler {


    /**
     * springmvc启动时自动装配json处理类
     */
    private final ObjectMapper objectMapper;

    public CustomerAuthenticationExceptionHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void onAuthenticationException(HttpServletRequest request, HttpServletResponse response, CustomerAuthenticationException ex) throws IOException {
        // 设置响应http状态码
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        // 令牌过期或令牌失效情况返回401
        if(ex instanceof TokenExpireException || ex instanceof InvalidTokenException){
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
        }
        response.setContentType(AuthConstants.CONTENT_TYPE);
        // 返回认证错误与具体认证异常消息
        Result<Object> result = Result.failed(CommonCode.AUTH_ERROR.getResCode(), ex.getMessage());
        response.getWriter().write(objectMapper.writeValueAsString(result));
        response.getWriter().flush();
        response.getWriter().close();
    }
}
