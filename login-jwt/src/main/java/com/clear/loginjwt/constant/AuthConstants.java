package com.clear.loginjwt.constant;

/**
 * @author jiexipeng@kongfz.com
 * @date 2021/11/15  3:14 下午
 */


public interface AuthConstants {

    /**
     * 认证路径{@value}
     */
    String LOGIN_URL = "/auth/**";

    /**
     * 认证模式{@value}
     */
    String AUTH_TYPE = "authType";

    /**
     * 认证参数用户名/账号{@value}
     */
    String AUTH_USERNAME = "username";

    /**
     * 认证参数密码{@value}
     */
    String AUTH_PASSWORD = "password";

    /**
     * 认证参数刷新token {@value}
     */
    String AUTH_REFRESH_TOKEN = "refreshToken";
}
