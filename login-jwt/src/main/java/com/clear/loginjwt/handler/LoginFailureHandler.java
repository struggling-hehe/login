package com.clear.loginjwt.handler;

import com.clear.loginjwt.config.result.CommonCode;
import com.clear.loginjwt.constant.AuthConstants;
import com.clear.loginjwt.constant.ExceptionMsgConstants;
import com.clear.loginjwt.pojo.core.Result;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author jiexipeng@kongfz.com
 * @date 2021/11/20  2:38 下午
 */

@Slf4j
@Component
public class LoginFailureHandler implements AuthenticationFailureHandler {
    /**
     * springmvc启动时自动装配json处理类
     */
    private final ObjectMapper objectMapper;

    public LoginFailureHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        // 返回认证错误与具体认证异常消息
        Result<Object> rsp = Result.failed(CommonCode.AUTH_ERROR.getResCode(), exception.getMessage());
        log.info("loginFailureHandler ---> {}", rsp);
        // 设置响应http状态码
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        // 刷新令牌过期或令牌失效情况返回401
        String message = exception.getMessage();
        if(ExceptionMsgConstants.EXPIRE_TOKEN_ERROR_MSG.equals(message) || ExceptionMsgConstants.INVALID_TOKEN_ERROR_MSG.equals(message) ){
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
        }
        response.setContentType(AuthConstants.CONTENT_TYPE);
        response.getWriter().write(objectMapper.writeValueAsString(rsp));
        response.getWriter().flush();
        response.getWriter().close();
    }
}
