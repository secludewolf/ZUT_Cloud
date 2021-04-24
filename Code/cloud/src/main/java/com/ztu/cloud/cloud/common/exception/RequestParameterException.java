package com.ztu.cloud.cloud.common.exception;

/**
 * @author Jager
 * @description 参数错误
 * @date 2019/12/26-19:35
 **/
public class RequestParameterException extends Exception {
    public RequestParameterException(String message) {
        super(message);
    }

    public RequestParameterException() {
        super("请求参数错误");
    }
}
