package com.ztu.cloud.cloud.util;

import java.util.UUID;

/**
 * @author Jager
 * @description 通用工具类
 * @date 2020/8/3-10:52
 **/
public class CommonUtil {
	/**
	 * 生成UUID
	 * @return UUID
	 */
	public static String getUuid() {
		UUID uuid = UUID.randomUUID();
		String str = uuid.toString();
		return str.replace("-", "");
	}
}
