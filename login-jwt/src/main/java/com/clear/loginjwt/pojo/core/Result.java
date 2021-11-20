package com.clear.loginjwt.pojo.core;

import com.alibaba.fastjson.JSON;
import com.clear.loginjwt.config.result.CommonCode;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

/**
 * @author jiexipeng@kongfz.com
 * @date 2021/11/20  2:26 下午
 */

@Slf4j
public class Result<T> implements Serializable {

    private Result(String code, String msg, String subCode, String subMsg, T data) {
        this.code = code;
        this.msg = msg;
        this.subCode = subCode;
        this.subMsg = subMsg;
//        this.verificationFailedMsgList = verificationFailedMsgList;
        this.data = data;
    }

    /**
     * 响应状态码
     */
//    @ApiModelProperty("响应状态码")
    private String code;

    /**
     * 响应状态消息
     */
//    @ApiModelProperty("响应状态消息")
    private String msg;

    /**
     * 业务响应状态码
     */
//    @ApiModelProperty("业务响应状态消息")
    private String subCode;

    /**
     * 业务响应状态消息
     */
//    @ApiModelProperty("业务响应状态消息")
    private String subMsg;

    /**
     * JSR303校验错误显示融合进错误响应
     */
//    @ApiModelProperty("响应状态消息")
//    private transient List<VerificationFailedMsg> verificationFailedMsgList;

    /**
     * 返回结果
     */
//    @ApiModelProperty("返回结果")
    private T data;


    /**
     * 常用成功无结果
     *
     * @param <T> 结果泛型
     * @return top.surgeqi.security.demo.bean.result.Result 结果
     * @author qipp
     */
    public static <T> Result<T> succeed() {
        return new Result<>(
                CommonCode.SUCCESS.getResCode(), CommonCode.SUCCESS.getResMsg(),
                CommonCode.SUB_SUCCESS.getResCode(), CommonCode.SUB_SUCCESS.getResMsg(), null);
    }

    /**
     * 常用成功返回结果
     *
     * @param <T>   结果泛型
     * @param model 返回结果
     * @return top.surgeqi.security.demo.bean.result.Result 结果
     * @author qipp
     */
    public static <T> Result<T> succeed(T model) {
        return new Result<>(
                CommonCode.SUCCESS.getResCode(), CommonCode.SUCCESS.getResMsg(),
                CommonCode.SUB_SUCCESS.getResCode(), CommonCode.SUB_SUCCESS.getResMsg(), model);
    }

    /**
     * 常用业务错误返回结果
     *
     * @param subCode 业务错误码
     * @param subMsg  业务错误消息
     * @param <T>     结果泛型
     * @return top.surgeqi.security.demo.bean.result.Result 结果
     * @author qipp
     */
    public static <T> Result<T> failed(String subCode, String subMsg) {
        log.warn("业务错误返回结果 subCode -> {},subMsg -> {}", subCode, subMsg);
        return new Result<>(
                CommonCode.FAIL.getResCode(), CommonCode.FAIL.getResMsg(),
                subCode, subMsg, null);
    }

//    /**
//     * 常用业务错误返回枚举结果
//     *
//     * @param serviceCode 业务错误枚举
//     * @param <T>         结果泛型
//     * @return top.surgeqi.security.demo.bean.result.Result 结果
//     * @author qipp
//     */
//    public static <T> Result<T> failed(ServiceCode serviceCode) {
//        // 业务错误码
//        String subCode = serviceCode.getCode();
//        // 业务错误消息
//        String subMsg = serviceCode.getMsg();
//        log.warn("业务错误返回结果 subCode -> {},subMsg -> {}", subCode, subMsg);
//        return new Result<>(
//                CommonCode.FAIL.getResCode(), CommonCode.FAIL.getResMsg(),
//                subCode, subMsg,
//                null, null);
//    }

    /**
     * 业务异常返回结果错误结果、错误消息
     *
     * @param subCode 业务异常码
     * @param subMsg  业务异常消息
     * @return top.surgeqi.security.demo.bean.result.Result 结果
     * @author qipp
     */
    public static <T> Result<T> exception(String subCode, String subMsg) {
        log.error("业务异常返回结果 subCode -> {},subMsg -> {}", subCode, subMsg);
        return new Result<>(
                CommonCode.ERROR.getResCode(), CommonCode.ERROR.getResMsg(),
                subCode, subMsg, null);
    }

//    /**
//     * JSR303入参校验错误
//     *
//     * @param verificationFailedMsgList 入参校验错误列表
//     * @return top.surgeqi.security.demo.bean.result.Result 结果
//     * @author qipp
//     */
//    public static <T> Result<T> verificationFailed(List<VerificationFailedMsg> verificationFailedMsgList) {
//        log.warn("JSR303入参校验错误 subCode -> {},subMsg -> {}", CommonCode.JSR303_ERROR.getResCode(), CommonCode.JSR303_ERROR.getResMsg());
//        return new Result<>(
//                CommonCode.ERROR.getResCode(), CommonCode.ERROR.getResMsg(),
//                CommonCode.JSR303_ERROR.getResCode(), CommonCode.JSR303_ERROR.getResMsg(),
//                verificationFailedMsgList, null);
//    }

    /**
     * 请求是否成功
     * <p> 结构体增加success字段 </p>
     *
     * @return boolean  是否成功
     * @author qipp
     */
    public boolean isSuccess() {
        return this.subCode.equals(CommonCode.SUB_SUCCESS.getResCode());
    }

    /**
     * 转化为JSON字符串
     *
     * @return java.lang.String 结果JSON
     * @author qipp
     */
    public String toJsonString() {
        return JSON.toJSONString(this);
    }

}
