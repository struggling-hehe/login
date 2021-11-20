package com.clear.loginjwt.service;

import com.clear.loginjwt.config.properties.JwtProperties;
import com.clear.loginjwt.service.interfaces.TokenCacheServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author jiexipeng@kongfz.com
 * @date 2021/11/20  12:02 下午
 *
 * 登出令牌缓存操作类
 */

@Slf4j
@Component
public class TokenRedisCacheService implements TokenCacheServiceImpl {
    /**
     * redis
     */
    private final RedisTemplate<String, String> redisTemplate;
    /**
     * JWT相关配置
     */
    private final JwtProperties jwtProperties;

    public TokenRedisCacheService( RedisTemplate<String, String> redisTemplate, JwtProperties jwtProperties) {
        log.info("使用redis缓存存储用户登出令牌");
        this.redisTemplate = redisTemplate;
        this.jwtProperties = jwtProperties;
    }

    /**
     * 存储登出令牌与刷新令牌
     *
     * @param accessToken  令牌
     * @param refreshToken 刷新令牌
     * @author qipp
     */
    @Override
    public void storeLogoutToken(String accessToken, String refreshToken) {
        if (StringUtils.isNoneEmpty(accessToken)){
            redisTemplate.opsForValue().set(accessToken,FLAG,jwtProperties.getAccessExpiration(), TimeUnit.MILLISECONDS);
            log.info("用户登出令牌->{}", accessToken);
        }
        if (StringUtils.isNoneEmpty(refreshToken)){
            redisTemplate.opsForValue().set(refreshToken,FLAG,jwtProperties.getRefreshExpiration(), TimeUnit.MILLISECONDS);
            log.info("用户登出刷新令牌->{}",refreshToken);
        }
    }

    /**
     * 令牌或刷新令牌是否已登出
     *
     * @param token 令牌或刷新令牌
     * @return boolean
     * @author qipp
     */
    @Override
    public boolean isLogoutToken(String token) {
        log.info("判断令牌是否被用户登出->{}", token);
        if (StringUtils.isNoneEmpty(token)){
            String value = redisTemplate.opsForValue().get(token);
            return StringUtils.isNoneEmpty(value) && FLAG.equals(value);
        }
        return false;
    }

}
