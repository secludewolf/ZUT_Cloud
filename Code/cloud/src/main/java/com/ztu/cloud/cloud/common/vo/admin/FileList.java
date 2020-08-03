package com.ztu.cloud.cloud.common.vo.admin;

import com.ztu.cloud.cloud.common.bean.mysql.File;
import lombok.Data;

import java.util.List;

/**
 * @author Jager
 * @description 用户列表信息
 * @date 2020/06/24-16:10
 **/
@Data
public class FileList {
	List<File> fileList;
	long pageLength;

	public FileList() {
	}

	public FileList(long pageLength, List<File> fileList) {
		this.pageLength = pageLength;
		this.fileList = fileList;
	}
}
