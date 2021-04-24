package com.ztu.cloud.cloud.util;

import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;

/**
 * @author Jager
 * @description JSON工具
 * @date 2020/06/23-17:09
 **/
public class JsonUtil {
    /**
     * 解析JSON字符串
     *
     * @param jsonString JSON字符串
     * @return JSONObject
     */
    public static JSONObject parseJson(String jsonString) {
        try {
            return new JSONObject(jsonString);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取String
     *
     * @param json JSON数据
     * @param key  Key
     * @return String
     */
    public static String getString(JSONObject json, String key) {
        try {
            return json.getString(key);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取Integer
     *
     * @param json JSON数据
     * @param key  Key
     * @return Integer
     */
    public static Integer getInt(JSONObject json, String key) {
        try {
            return json.getInt(key);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取Long
     *
     * @param json JSON数据
     * @param key  Key
     * @return Long
     */
    public static Long getLong(JSONObject json, String key) {
        try {
            return json.getLong(key);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取Boolean
     *
     * @param json JSON数据
     * @param key  Key
     * @return Boolean
     */
    public static Boolean getBoolean(JSONObject json, String key) {
        try {
            return json.getBoolean(key);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static JSONObject getJSONObject(JSONObject json, String key) {
        try {
            return json.getJSONObject(key);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
