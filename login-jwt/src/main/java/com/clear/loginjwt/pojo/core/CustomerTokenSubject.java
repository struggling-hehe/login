package com.clear.loginjwt.pojo.core;

import lombok.Data;
import lombok.Getter;

/**
 * @author jiexipeng@kongfz.com
 * @date 2021/11/20  2:19 下午
 */
@Data
public class CustomerTokenSubject {

    /**
     * 用户名
     */
    private String username;

    /**
     * 令牌类型
     */
    private String tokenType;

    /**
     * <p><em>Created by qipp on 2020/6/2 11:05</em></p>
     * 令牌类型枚举
     */
    @Getter
    public enum TokenType {

        /**
         * 令牌
         */
        ACCESS_TOKEN("access_token"),

        /**
         * 刷新令牌
         */
        REFRESH_TOKEN("refresh_token"),
        ;

        /**
         * 令牌类型
         */
        private final String type;

        TokenType(String type) {
            this.type = type;
        }
    }
}
