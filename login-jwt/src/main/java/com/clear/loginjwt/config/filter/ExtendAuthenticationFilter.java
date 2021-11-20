package com.clear.loginjwt.config.filter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.clear.loginjwt.config.extend.ExtendAuthenticationEntity;
import com.clear.loginjwt.config.properties.JwtProperties;
import com.clear.loginjwt.constant.AuthConstants;
import com.clear.loginjwt.exception.CustomerAuthenticationException;
import com.clear.loginjwt.handler.AuthenticationExceptionHandler;
import com.clear.loginjwt.service.JwtTokenService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * @author jiexipeng@kongfz.com
 * @date 2021/11/17  4:36 下午
 */

@Slf4j
@Component
public class ExtendAuthenticationFilter extends OncePerRequestFilter {

    /**
     * 请求匹配器
     */
    private RequestMatcher requestMatcher;

    /**
     * 用户详情Service
     */
//    private final AbstractUserDetailsService abstractUserDetailsService;

    /**
     * 令牌相关操作Service
     */
    private final JwtTokenService jwtTokenService;

    /**
     * 自定义通用认证异常以及子类异常处理器
     */
    private final AuthenticationExceptionHandler authenticationExceptionHandler;

    /**
     * JWT相关配置
     */
    private final JwtProperties jwtProperties;


    public ExtendAuthenticationFilter(JwtTokenService jwtTokenService, JwtProperties jwtProperties,AuthenticationExceptionHandler authenticationExceptionHandler) {
        this.jwtTokenService = jwtTokenService;
        this.jwtProperties = jwtProperties;
        this.authenticationExceptionHandler = authenticationExceptionHandler;
        this.requestMatcher = new OrRequestMatcher(
                new AntPathRequestMatcher(AuthConstants.LOGIN_URL, "GET"),
                new AntPathRequestMatcher(AuthConstants.LOGIN_URL, "POST")
        );
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            if (requestMatcher.matches(request)) {
                RequestParameterWrapper requestParameterWrapper = new RequestParameterWrapper(request);
                ExtendAuthenticationEntity entity = new ExtendAuthenticationEntity();
                // 获取授权模式
                entity.setAuthType(requestParameterWrapper.getParameter(AuthConstants.AUTH_TYPE));
                entity.setAuthParameters(requestParameterWrapper.getParameterMap());
//                ExtendAuthenticationContext.set(entity);
                try {
                    filterChain.doFilter(requestParameterWrapper, response);
                } finally {
                    // 必须回收自定义的ThreadLocal变量
//                    ExtendAuthenticationContext.clear();
                }
            } else {
                String token = request.getHeader(jwtProperties.getHeader());
                // token 存在
                if (StringUtils.isNoneEmpty(token)){
                    // (过期抛出异常)获取token 中的subject 目前存储的是用户username
                    String username = jwtTokenService.getSubjectFromToken(token);
                    if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//                        UserDetails userDetails = abstractUserDetailsService.loadUserByUsername(username);
                        // 将用户信息存入 authentication，方便后续校验
//                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
//                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        // 将 authentication 存入 ThreadLocal，方便后续获取用户信息
//                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                }
                filterChain.doFilter(request, response);
            }
        } catch (CustomerAuthenticationException ex) {
            authenticationExceptionHandler.onAuthenticationException(request, response, ex);
        }

    }

    /**
     * 用途：在拦截时给Request添加参数
     * Cloud OAuth2 密码模式需要判断Request是否存在password参数，
     * 如果不存在会抛异常结束认证
     * 所以在调用doFilter方法前添加password参数
     */
    static class RequestParameterWrapper extends HttpServletRequestWrapper {
        private final Map<String, String[]> params = new HashMap<>();

        RequestParameterWrapper(HttpServletRequest request) {
            super(request);
            this.params.putAll(getRequestBody(request));
        }

        /**
         * 获取请求JSON对象
         *
         * @param request 请求体
         * @return java.util.Map 参数map
         */
        private Map<String, String[]> getRequestBody(HttpServletRequest request) {
            StringBuilder stringBuilder = new StringBuilder();
            try (InputStream inputStream = request.getInputStream();
                 BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {
                char[] charBuffer = new char[1024];
                int bytesRead;
                while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
                    stringBuilder.append(charBuffer, 0, bytesRead);
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            // 请求对象
            String body = stringBuilder.toString();
            JSONObject jsonObject = JSON.parseObject(body);
            Map<String, String[]> map = new HashMap<>(8);

            for (String key : jsonObject.keySet()) {
                map.put(key, new String[]{jsonObject.getString(key)});
            }
            return map;
        }


        @Override
        public String getParameter(String name) {
            String[] values = params.get(name);
            if (values == null || values.length == 0) {
                return null;
            }
            return values[0];
        }

        @Override
        public String[] getParameterValues(String name) {
            return params.get(name);
        }

        @Override
        public Map<String, String[]> getParameterMap() {
            return params;
        }

    }
}
