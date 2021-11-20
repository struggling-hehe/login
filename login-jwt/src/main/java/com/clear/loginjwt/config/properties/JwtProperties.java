package com.clear.loginjwt.config.properties;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author jiexipeng@kongfz.com
 * @date 2021/11/17  4:45 下午
 */

@Getter
@ConfigurationProperties(value = "jwt")
public class JwtProperties {
    /**
     * 密钥
     */
    private String secret = "123456";

    /**
     * 令牌过期时间 毫秒
     */
    private Long accessExpiration = 7200000L;

    /**
     * 刷新令牌过期时间 毫秒
     */
    private Long refreshExpiration = 36000000L;

    /**
     * 请求头
     */
    private String header = "Authorization";

    /**
     * 失效令牌缓存name
     */
    private String logoutAccessTokenName = "logoutAccessToken";

    /**
     * 失效刷新令牌缓存name
     */
    private String logoutRefreshTokenName = "logoutRefreshToken";
}
