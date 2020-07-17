package com.ztu.cloud.cloud.common.vo.admin;

import com.ztu.cloud.cloud.common.bean.mysql.File;
import lombok.Data;

/**
 * @author Jager
 * @description 文件信息
 * @date 2020/06/28-09:28
 **/
@Data
public class FileInfo {
	private File file;

	public FileInfo() {
	}

	public FileInfo(File file) {
		this.file = file;
	}
}
