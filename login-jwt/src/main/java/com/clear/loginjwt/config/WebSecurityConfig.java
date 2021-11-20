package com.clear.loginjwt.config;

import com.clear.loginjwt.config.filter.ExtendAuthenticationFilter;
import com.clear.loginjwt.config.properties.SecurityProperties;
import com.clear.loginjwt.constant.AuthConstants;
import com.clear.loginjwt.handler.CustomerAuthenticationSuccessHandler;
import com.clear.loginjwt.handler.LoginFailureHandler;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.List;
import java.util.Map;

/**
 * @author jiexipeng@kongfz.com
 * @date 2021/11/15  2:19 下午
 *
 * spring security配置
 */

@Configuration
@EnableWebSecurity
@EnableConfigurationProperties(SecurityProperties.class)
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
     * security 验证配置
     */
    private final SecurityProperties securityProperties;

    /**
     * 登录成功
     * */
    private final CustomerAuthenticationSuccessHandler customerAuthenticationSuccessHandler;

    /**
     * 登录失败
     * */
    private final LoginFailureHandler loginFailureHandler;

    /**
     * 扩展认证拦截器
     */
    private final ExtendAuthenticationFilter extendAuthenticationFilter;


    public WebSecurityConfig(SecurityProperties securityProperties, CustomerAuthenticationSuccessHandler customerAuthenticationSuccessHandler,
                             LoginFailureHandler loginFailureHandler, ExtendAuthenticationFilter extendAuthenticationFilter) {
        this.securityProperties = securityProperties;
        this.customerAuthenticationSuccessHandler = customerAuthenticationSuccessHandler;
        this.loginFailureHandler = loginFailureHandler;
        this.extendAuthenticationFilter = extendAuthenticationFilter;
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {

        // 指定新的过滤器  配置token验证过滤器  自定义 jwt 拦截器的过滤器
        http.addFilterBefore(extendAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        // 开启授权认证  Spring Security 后，引入了CSRF，默认是开启。不得不说，CSRF和RESTful技术有冲突。CSRF默认支持的方法： GET|HEAD|TRACE|OPTIONS，不支持POST。
        http.csrf().disable()
                // rest 无状态 无session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        // 读取验证配置 设置不受保护路径 设置受保护路径需要的角色权限
//        this.readSecurityProperties(http);
        //spring security 放行注册中心健康检查
        http.authorizeRequests()
                // 表单认证页面不需要权限
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                // 其他页面需要用户登录才可以访问
                .anyRequest().authenticated()
                // 登录配置
                .and().formLogin().loginProcessingUrl(AuthConstants.LOGIN_URL)
                // 登录成功后的处理
                .successHandler(customerAuthenticationSuccessHandler)
                // 登录失败后的处理
                .failureHandler(loginFailureHandler);

        // 登录过期/未登录 、权限不足 处理
//        http.exceptionHandling().authenticationEntryPoint(loginExpireHandler).accessDeniedHandler(customAccessDeniedHandler);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // 覆盖DaoAuthenticationProvider#additionalAuthenticationChecks() 实现（扩展登录不需要验证密码）
//        auth.authenticationProvider(new ExtendAuthDaoAuthenticationProvider(userDetailsService));
//        auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder());
    }

    /**
     * 读取验证配置
     * 设置不受保护路径 设置受保护路径需要的角色权限 对不同的路由设置不同的处理方式
     * @param http 请求配置对象
     * @throws Exception 获取请求异常
     */
    private void readSecurityProperties(HttpSecurity http) throws Exception {
        // 设置不受保护路径 设置受保护路径需要的角色权限
        Map<String, List<String>> antMatchers = securityProperties.getAntMatchers();
        for (Map.Entry<String, List<String>> entry : antMatchers.entrySet()) {
            String key = entry.getKey();
            List<String> urls = entry.getValue();
            if (key.equals(UN_AUTHENTICATED)) {
                for (String url : urls) {
                    http.authorizeRequests().antMatchers(url).permitAll();
                }
            }else{
                String type = key.split(TO)[0];
                String str = key.split(TO)[1];
                if (type.equals(ROLE)) {
                    for (String url : urls) {
                        http.authorizeRequests().antMatchers(url).hasRole(str);
                    }
                }else if (type.equals(AUTH)) {
                    for (String url : urls) {
                        http.authorizeRequests().antMatchers(url).hasAuthority(str);
                    }
                }
            }
        }
    }

}
