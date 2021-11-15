package com.clear.loginjwt.config;

import com.clear.loginjwt.constant.AuthConstants;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @author jiexipeng@kongfz.com
 * @date 2021/11/15  2:19 下午
 *
 * spring security配置
 */

@Configuration
@EnableWebSecurity
@EnableConfigurationProperties()
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{

    /**
     * 角色/权限 分隔符{@value}
     */
    private static final String TO = "2";

    /**
     * 无需授权即可访问的接口{@value}
     */
    private static final String UN_AUTHENTICATED = "unAuthenticated";

    /**
     * 指定角色{@value}
     */
    private static final String ROLE = "role";

    /**
     * 指定权限{@value}
     */
    private static final String AUTH = "auth";

    /**
     * 配置接口的认证授权
     *
     * @author qipp
     * @param http {@link HttpSecurity}
     */


    @Override
    protected void configure(HttpSecurity http) throws Exception {

        // 配置token验证过滤器  自定义 jwt 拦截器的过滤器
//        http.addFilterBefore(extendAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        // 开启授权认证  Spring Security 后，引入了CSRF，默认是开启。不得不说，CSRF和RESTful技术有冲突。CSRF默认支持的方法： GET|HEAD|TRACE|OPTIONS，不支持POST。
        http.csrf().disable()
                // rest 无状态 无session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        // 读取验证配置 设置不受保护路径 设置受保护路径需要的角色权限
//        this.readSecurityProperties(http);

        //spring security 放行注册中心健康检查
        http.authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .anyRequest().authenticated()
                // 登录配置
                .and().formLogin().loginProcessingUrl(AuthConstants.LOGIN_URL);
//                .failureForwardUrl();
                // 登录成功后的处理
//                .successHandler(authenticationSuccessHandler)
                // 登录失败后的处理
//                .failureHandler(loginFailureHandler);

        // 登录过期/未登录 、权限不足 处理
//        http.exceptionHandling().authenticationEntryPoint(loginExpireHandler).accessDeniedHandler(customAccessDeniedHandler);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // 覆盖DaoAuthenticationProvider#additionalAuthenticationChecks() 实现（扩展登录不需要验证密码）
//        auth.authenticationProvider(new ExtendAuthDaoAuthenticationProvider(userDetailsService));
//        auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder());
    }

}
