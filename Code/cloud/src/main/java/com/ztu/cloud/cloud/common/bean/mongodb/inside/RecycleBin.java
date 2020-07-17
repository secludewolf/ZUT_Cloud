package com.ztu.cloud.cloud.common.bean.mongodb.inside;

import lombok.Data;

import java.util.Map;

/**
 * @author Jager
 * @description 回收站信息
 * @date 2020/06/22-10:23
 **/
@Data
public class RecycleBin {
	private Map<String, Folder> folders;
	private Map<String, File> files;

	public RecycleBin() {
		this.folders = null;
		this.files = null;
	}
}
