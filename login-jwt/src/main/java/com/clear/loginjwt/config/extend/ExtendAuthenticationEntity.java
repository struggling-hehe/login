package com.clear.loginjwt.config.extend;

import lombok.Data;

import java.util.Map;

/**
 * @author jiexipeng@kongfz.com
 * @date 2021/11/20  4:33 下午
 */
@Data
public class ExtendAuthenticationEntity {

    /**
     * 获取token扩展授权类型
     */
    private String authType;

    /**
     * 请求登录认证参数集合
     */
    private Map<String, String[]> authParameters;

    /**
     * 获取参数中的值
     *
     * @param parameter 请求参数
     * @return java.lang.String
     * @author qipp
     */
    public String getAuthParameter(String parameter) {
        String[] values = this.authParameters.get(parameter);
        if (values != null && values.length > 0) {
            return values[0];
        }
        return null;
    }
}
