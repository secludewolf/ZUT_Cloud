package com.ztu.cloud.cloud.util;

import com.sun.org.apache.regexp.internal.RE;
import org.apache.hadoop.fs.Stat;
import org.apache.hadoop.yarn.webapp.hamlet.Hamlet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.print.attribute.standard.MediaSize;
import java.util.*;

/**
 * @author Jager
 * @description 敏感词检测工具类
 * @date 2020/7/17-11:18
 **/
@Component
public class ForbiddenUtil {
	private static final String delimiter = ",";
	private static Set<String> fileNameWords;

	/**
	 * 检测文件名是否合法
	 * @param name 文件名
	 * @return 是否合法
	 */
	public static boolean isFileNameValid(String name) {
		for (String item : fileNameWords) {
			if (name.contains(item)) return false;
		}
		return true;
	}

	@Value("${forbidden.fileName}")
	public void setBlackWords(String words) {
		ForbiddenUtil.fileNameWords = new HashSet<>(Arrays.asList(words.split(delimiter)));
	}

}
