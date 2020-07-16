package com.ztu.cloud.cloud.util;

import com.ztu.cloud.cloud.common.vo.ResultBody;
import com.ztu.cloud.cloud.common.vo.ResultResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

/**
 * @author Jager
 * @description 返回体工具
 * @date 2020/06/23-12:11
 **/
public class ResultUtil {
    /**
     * 创建响应体
     *
     * @param status  响应码
     * @param code    状态码
     * @param message 消息
     * @param data    数据
     * @return 响应体
     */
    public static ResultResponseEntity createResult(HttpStatus status, int code, String message, Object data) {
        ResultBody resultBody = new ResultBody(code, message, data);
        return new ResultResponseEntity(resultBody, status);
    }

    /**
     * 创建带token的响应体
     * status默认200,code默认1
     *
     * @param message 消息
     * @param data    数据
     * @param token   Token
     * @return 响应体
     */
    public static ResultResponseEntity createResultWithToken(String message, Object data, String token) {
        ResultBody resultBody = new ResultBody(1, message, data);
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add(TokenUtil.TOKEN_HEADER, TokenUtil.TOKEN_PREFIX + token);
        return new ResultResponseEntity(resultBody, headers, HttpStatus.OK);
    }

    /**
     * 创建响应体
     * status默认200
     *
     * @param code    状态码
     * @param message 消息
     * @param data    数据
     * @return 响应体
     */
    public static ResultResponseEntity createResult(int code, String message, Object data) {
        return createResult(HttpStatus.OK, code, message, data);
    }

    /**
     * 创建响应体
     * status默认200,code默认1
     *
     * @param message 消息
     * @param data    数据
     * @return 响应体
     */
    public static ResultResponseEntity createResult(String message, Object data) {
        return createResult(HttpStatus.OK, 1, message, data);
    }

    /**
     * 创建响应体
     * status默认200
     *
     * @param code    状态码
     * @param message 消息
     * @return 响应体
     */
    public static ResultResponseEntity createResult(int code, String message) {
        return createResult(HttpStatus.OK, code, message, null);
    }
}
