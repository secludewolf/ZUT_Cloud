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
	 * @throws JSONException JSON解析错误
	 */
	public static JSONObject parseJson(String jsonString) throws JSONException {
		return new JSONObject(jsonString);
	}

	/**
	 * 获取String
	 *
	 * @param json JSON数据
	 * @param key  Key
	 * @return String
	 * @throws JSONException JSON解析错误
	 */
	public static String getString(JSONObject json, String key) throws JSONException {
		return json.getString(key);
	}

	/**
	 * 获取Integer
	 *
	 * @param json JSON数据
	 * @param key  Key
	 * @return Integer
	 * @throws JSONException JSON解析错误
	 */
	public static Integer getInt(JSONObject json, String key) throws JSONException {
		return json.getInt(key);
	}

	/**
	 * 获取Long
	 *
	 * @param json JSON数据
	 * @param key  Key
	 * @return Long
	 * @throws JSONException JSON解析错误
	 */
	public static Long getLong(JSONObject json, String key) throws JSONException {
		return json.getLong(key);
	}

	public static boolean getBoolean(JSONObject json, String key) throws JSONException {
		return json.getBoolean(key);
	}

	public static JSONObject getJSONObject(JSONObject json, String key) throws JSONException {
		return json.getJSONObject(key);
	}
}
