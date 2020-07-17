package com.ztu.cloud.cloud.common.bean.mysql;

import lombok.Data;

/**
 * @author Jager
 * @description 用户文件表
 * @date 2020/06/21-12:44
 **/
@Data
public class UserFile {
	private Long id;
	private int userId;
	private String fileId;
	private String path;

	public UserFile() {
	}

	public UserFile(int userId, String fileId, String path) {
		this.userId = userId;
		this.fileId = fileId;
		this.path = path;
	}
}
