package com.clear.loginjwt.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @author jiexipeng@kongfz.com
 * @date 2021/11/20  3:48 下午
 */

public class PassWordEncryptor {

    private static final BCryptPasswordEncoder ENCODER = new BCryptPasswordEncoder();

    /**
     * 用BCryptPasswordEncoder
     *
     * @param password 密码
     * @return java.lang.String 加密后密码
     * @author qipp
     */
    public static String bCryptPassword(String password) {
        return ENCODER.encode(password);
    }

    /**
     * 密码匹配
     *
     * @param rawPassword     未加工密码
     * @param encodedPassword 编码后密码
     * @return boolean        是否匹配
     * @author qipp
     */
    public static boolean matches(CharSequence rawPassword, String encodedPassword) {
        return ENCODER.matches(rawPassword, encodedPassword);
    }
}
