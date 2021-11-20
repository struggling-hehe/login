package com.clear.loginjwt.service.interfaces;

/**
 * @author jiexipeng@kongfz.com
 * @date 2021/11/20  11:45 上午
 */
public interface TokenCacheServiceImpl {

    /**
     * 缓存标志位
     */
    String FLAG = "T";

    /**
     * 存储登出令牌与刷新令牌
     *
     * @param accessToken  令牌
     * @param refreshToken 刷新令牌
     * @author qipp
     */
    void storeLogoutToken(String accessToken, String refreshToken);

    /**
     * 令牌或刷新令牌是否已登出
     *
     * @param token 令牌或刷新令牌
     * @return boolean
     * @author qipp
     */
    boolean isLogoutToken(String token);
}
