package com.ztu.cloud.cloud.util;

import com.ztu.cloud.cloud.common.bean.mongodb.inside.Folder;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Jager
 * @description 仓库操作工具类
 * @date 2020/8/3-10:51
 **/
public class RepositoryUtil {
	/**
	 * 判断文件是否存在
	 *
	 * @param name   文件名
	 * @param parent 父文件夹
	 * @return 是否存在
	 */
	public static boolean nameIsExist(String name, Folder parent) {
		Set<String> names = new HashSet<>();
		if (parent.getFolders() != null) {
			names.addAll(parent.getFolders().keySet());
		}
		if (parent.getFiles() != null) {
			names.addAll(parent.getFiles().keySet());
		}
		for (String item : names) {
			if (item.equalsIgnoreCase(name)) {
				return true;
			}
		}
		return false;
	}

	public static boolean pathIsNested(String source, String target) {
		return target.length() >= source.length() && source.equals(source.substring(0, target.length()));
	}
}
