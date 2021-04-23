package com.ztu.cloud.cloud.util;

import com.google.gson.JsonObject;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

/**
 * @author Jager
 * @description 通用工具类
 * @date 2020/8/3-10:52
 **/
public class CommonUtil {
    /**
     * 生成UUID
     *
     * @return UUID
     */
    public static String getUuid() {
        UUID uuid = UUID.randomUUID();
        String str = uuid.toString();
        return str.replace("-", "");
    }

    /**
     * 返回错误信息
     *
     * @param code     错误类型
     * @param message  错误信息
     * @param response 响应体
     */
    public static void returnError(int code, String message, HttpServletResponse response) {
        try {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");
            ServletOutputStream outputStream = response.getOutputStream();
            JsonObject json = new JsonObject();
            json.addProperty("code", code);
            json.addProperty("message", message);
            String content = json.toString();
            outputStream.write(content.getBytes());
            outputStream.close();
        } catch (IOException ignored) {
        }
    }
}
