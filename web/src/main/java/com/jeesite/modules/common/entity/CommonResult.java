package com.jeesite.modules.common.entity;

import com.jeesite.common.constant.CodeConstant;

/**
 * 公共返回结果
 * @author lvchangwei
 * @version 2019-06-27
 */
public class CommonResult<T> {
    /**
     * 返回编码
     */
    private String code = CodeConstant.REQUEST_SUCCESSFUL;

    /**
     * 返回信息
     */
    private String msg = "请求成功";

    /**
     * 返回数据
     */
    private T data;

    public CommonResult() {

    }

    public CommonResult(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public CommonResult(T data) {
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public CommonResult setCode(String code) {
        this.code = code;
        return null;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
