package com.clear.loginjwt.handler;

import com.clear.loginjwt.constant.AuthConstants;
import com.clear.loginjwt.pojo.core.Result;
import com.clear.loginjwt.pojo.dto.JwtDTO;
import com.clear.loginjwt.service.JwtTokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author jiexipeng@kongfz.com
 * @date 2021/11/20  10:50 上午
 */

@Component
public class CustomerAuthenticationSuccessHandler implements AuthenticationSuccessHandler {


    /**
     * 令牌相关操作Service
     */
    private final JwtTokenService jwtTokenService;

    /**
     * springmvc启动时自动装配json处理类
     */
    private final ObjectMapper objectMapper;

    public CustomerAuthenticationSuccessHandler(JwtTokenService jwtTokenService, ObjectMapper objectMapper) {
        this.jwtTokenService = jwtTokenService;
        this.objectMapper = objectMapper;
    }

    /**
     * 认证成功触发此接口
     *
     * @param httpServletRequest  请求对象
     * @param httpServletResponse 响应对象
     * @param authentication      认证对象
     * @author
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        // 生成token
        JwtDTO jwtDTO = jwtTokenService.generateToken(userDetails);
        // 返回令牌对象
        httpServletResponse.setStatus(HttpStatus.OK.value());
        httpServletResponse.setContentType(AuthConstants.CONTENT_TYPE);
        httpServletResponse.getWriter().write(objectMapper.writeValueAsString(Result.succeed(jwtDTO)));
        httpServletResponse.getWriter().flush();
        httpServletResponse.getWriter().close();
    }
}
