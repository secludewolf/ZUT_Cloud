package com.ztu.cloud.cloud.common.vo;

import lombok.Data;

/**
 * @author Jager
 * @description 返回值
 * @date 2019/12/27-22:21
 **/
@Data
public class ResultBody {
    private int code;
    private String message;
    private Object data;

    public ResultBody(int code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }
}
