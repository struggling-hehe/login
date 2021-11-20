package com.clear.loginjwt.config.result;

import lombok.Getter;

/**
 * @author jiexipeng@kongfz.com
 * @date 2021/11/20  2:28 下午
 */

@Getter
public enum CommonCode {

    /**
     * 请求成功
     */
    SUCCESS("SUCCESS","请求成功" ),

    /**
     * 内部业务错误
     */
    FAIL("FAIL","内部业务错误"),

    /**
     * 异常
     */
    ERROR("ERROR","内部业务异常"),

    /**
     * http 请求方式不支持异常
     */
    HTTP_METHOD_ERROR("HTTP_METHOD_ERROR","请求方式不支持"),



    /**
     * 业务请求成功
     */
    SUB_SUCCESS("OK", "业务请求成功"),

    /**
     * 参数校验失败
     */
    SUB_VERIFICATION_FAILED("VERIFICATION_FAILED","格式校验失败"),

    /**
     * 未定义业务失败
     */
    SUB_FAIL("UNDEFINED_FAIL", "未定义业务失败"),

    /**
     * 未知异常
     */
    UNKNOWN_ERROR("UNKNOWN_ERROR","未知异常"),

    /**
     * 入参校验异常
     */
    JSR303_ERROR("JSR303_ERROR","入参校验异常"),

    /**
     * 请求超时
     */
    FEIGN_TIMED_OUT("TIMED_OUT", "Feign调用请求超时"),

    /**
     * 认证相关异常
     */
    AUTH_ERROR("AUTH_ERROR", "认证相关异常"),
    ;

    /**
     * 通用响应码
     */
    private final String resCode;

    /**
     * 通用响应消息
     */
    private final String resMsg;

    CommonCode(String resCode, String resMsg) {
        this.resCode = resCode;
        this.resMsg = resMsg;
    }

    /**
     * 根据通用响应码返回通用响应消息
     * @param resCode 通用响应码
     * @return java.lang.String 通用响应消息
     * @author qipp
     */
    public static String getResMsgByResCode(String resCode) {
        CommonCode[] responseConstants = CommonCode.values();
        for (CommonCode ret : responseConstants) {
            if (ret.getResCode().equals(resCode)) {
                return ret.getResMsg();
            }
        }
        return "";
    }
}
