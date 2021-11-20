package com.clear.loginjwt.pojo.dto;

import lombok.Data;

/**
 * @author jiexipeng@kongfz.com
 * @date 2021/11/20  10:53 上午
 */

@Data
public class JwtDTO {
    /**
     * 令牌
     */
    private String accessToken;

    /**
     * 刷新令牌
     */
    private String refreshToken;

    /**
     * 令牌类型
     */
    private String tokenType;

    /**
     * 过期时间
     */
    private Long expiresIn;

    /**
     * JWT ID
     */
    private String jti;
}
