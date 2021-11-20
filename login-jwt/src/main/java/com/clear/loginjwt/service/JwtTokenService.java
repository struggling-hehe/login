package com.clear.loginjwt.service;

import com.alibaba.fastjson.JSON;
import com.clear.loginjwt.config.properties.JwtProperties;
import com.clear.loginjwt.constant.AuthConstants;
import com.clear.loginjwt.constant.ExceptionMsgConstants;
import com.clear.loginjwt.exception.CustomerAuthenticationException;
import com.clear.loginjwt.exception.InvalidTokenException;
import com.clear.loginjwt.exception.TokenExpireException;
import com.clear.loginjwt.pojo.core.CustomerTokenSubject;
import com.clear.loginjwt.pojo.dto.JwtDTO;
import com.clear.loginjwt.service.interfaces.TokenCacheServiceImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author jiexipeng@kongfz.com
 * @date 2021/11/17  4:52 下午
 */
@EnableConfigurationProperties(value = JwtProperties.class)
@Component
public class JwtTokenService {

    /**
     * JWT相关配置
     */
    private final JwtProperties jwtProperties;
    /**
     * 令牌缓存操作类
     */
    private final TokenCacheServiceImpl tokenCacheService;

    public JwtTokenService(JwtProperties jwtProperties, TokenCacheServiceImpl tokenCacheService) {
        this.jwtProperties = jwtProperties;
        this.tokenCacheService = tokenCacheService;
    }

    /**
     * 从令牌中获取用户名
     *
     * @param token 令牌
     * @return java.lang.String 用户名
     * @author qipp
     */
    public String getSubjectFromToken(String token) {
        // 获取subject
        String subject = isTokenExpired(token);
        CustomerTokenSubject customerTokenSubject = JSON.parseObject(subject, CustomerTokenSubject.class);
        // 如果不是普通令牌抛出异常
        if (!CustomerTokenSubject.TokenType.ACCESS_TOKEN.getType().equals(customerTokenSubject.getTokenType())) {
            // 刷新TOKEN不可当作TOKEN使用
            throw new CustomerAuthenticationException("刷新TOKEN不可当作TOKEN使用！");
        }
        return customerTokenSubject.getUsername();
    }

    /**
     * 判断令牌是否过期未过期返回subject
     *
     * @param token 令牌
     * @return java.lang.String subject
     * @author qipp
     */
    private String isTokenExpired(String token) {
        // 去除Bearer
        token = token.replace(AuthConstants.AUTH_REQ_TOKEN_TYPE, "");
        // 从令牌中获取数据声明
        Claims claims = getClaimsFromToken(token);
        // 是否登出token
        if (tokenCacheService.isLogoutToken(token)) {
            throw new InvalidTokenException("无效的令牌！" + token);
        }
        // 获取subject
        return claims.getSubject();
    }

    /**
     * 从令牌中获取数据声明
     *
     * @param token 令牌
     * @return io.jsonwebtoken.Claims 令牌数据声明
     * @author qipp
     */
    private Claims getClaimsFromToken(String token) {
        Claims claims;
        try {
            claims = Jwts.parser().setSigningKey(jwtProperties.getSecret()).parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {
            throw new TokenExpireException(ExceptionMsgConstants.EXPIRE_TOKEN_ERROR_MSG, e);
        } catch (Exception e) {
            throw new InvalidTokenException(ExceptionMsgConstants.INVALID_TOKEN_ERROR_MSG, e);
        }
        return claims;
    }

    /**
     * 生成令牌
     *
     * @param userDetails 用户对象
     * @return com.mogu.succulent.self.domain.dto.JwtDTO 令牌对象
     * @author qipp
     */
    public JwtDTO generateToken(UserDetails userDetails) {

        // JTI
        String jti = UUID.randomUUID().toString();
        // 构建返回DTO
        JwtDTO jwtDTO = new JwtDTO();
        // 当前时间
        Date date = new Date();
        // 令牌
        jwtDTO.setAccessToken(generateToken(userDetails.getUsername(), jti, date));
        // 刷新令牌
        jwtDTO.setRefreshToken(generateRefreshToken(userDetails.getUsername(), jti, date));
        // 过期时间
        jwtDTO.setExpiresIn(jwtProperties.getAccessExpiration() / 1000);
        // 令牌类型
        jwtDTO.setTokenType(AuthConstants.AUTH_TOKEN_TYPE);
        // JWT ID
        jwtDTO.setJti(jti);
        return jwtDTO;
    }

    /**
     * 根据数据声明生成普通令牌
     *
     * @param username 自定义Subject
     * @param jti      令牌ID
     * @param date     当前时间
     * @return java.lang.String
     */
    private String generateToken(String username, String jti, Date date) {
        Map<String, Object> claims = new HashMap<>(8);
        // 创建自定义Subject
        CustomerTokenSubject customerTokenSubject = new CustomerTokenSubject();
        customerTokenSubject.setUsername(username);
        // 类型为普通令牌
        customerTokenSubject.setTokenType(CustomerTokenSubject.TokenType.ACCESS_TOKEN.getType());
        claims.put(Claims.SUBJECT, JSON.toJSONString(customerTokenSubject));
        claims.put(Claims.ISSUED_AT, date);
        claims.put(Claims.ID, jti);
        Date expirationDate = new Date(System.currentTimeMillis() + jwtProperties.getAccessExpiration());
        return generateToken(claims, expirationDate);
    }

    /**
     * 根据数据声明生成刷新令牌
     *
     * @param username 自定义Subject
     * @param jti      令牌ID
     * @param date     当前时间
     * @return java.lang.String
     * @author qipp
     */
    private String generateRefreshToken(String username, String jti, Date date) {
        Map<String, Object> claims = new HashMap<>(8);
        // 创建自定义Subject
        CustomerTokenSubject customerTokenSubject = new CustomerTokenSubject();
        customerTokenSubject.setUsername(username);
        // 类型为刷新令牌
        customerTokenSubject.setTokenType(CustomerTokenSubject.TokenType.REFRESH_TOKEN.getType());
        claims.put(Claims.SUBJECT, JSON.toJSONString(customerTokenSubject));
        claims.put(Claims.ISSUED_AT, date);
        claims.put(Claims.ID, jti);
        Date expirationDate = new Date(System.currentTimeMillis() + jwtProperties.getRefreshExpiration());
        return generateToken(claims, expirationDate);
    }

    /**
     * 生成令牌
     *
     * @param claims         令牌数据声明Map
     * @param expirationDate 过期时间
     * @return java.lang.String
     * @author qipp
     */
    private String generateToken(Map<String, Object> claims, Date expirationDate) {
        return Jwts.builder().setClaims(claims).setExpiration(expirationDate).signWith(SignatureAlgorithm.HS512, jwtProperties.getSecret()).compact();
    }

}
