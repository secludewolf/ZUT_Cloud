package com.ztu.cloud.cloud.common.bean.mysql;

import lombok.Data;

/**
 * @author Jager
 * @description 文件信息表
 * @date 2020/06/21-12:42
 **/
@Data
public class File {
	private String id;
	private String name;
	private String path;
	private long size;
	private int status;
	private String type;
	private long quoteNumber;
	private long createTime;
	private long changeTime;

	public File() {
	}

	public File(String id, String name, String path, long size, int status, String type, long quoteNumber, long createTime, long changeTime) {
		this.id = id;
		this.name = name;
		this.path = path;
		this.size = size;
		this.status = status;
		this.type = type;
		this.quoteNumber = quoteNumber;
		this.createTime = createTime;
		this.changeTime = changeTime;
	}

	public File(String id, String name, String path, long size, String type) {
		this.id = id;
		this.name = name;
		this.path = path;
		this.size = size;
		this.type = type;
		this.status = 1;
		this.quoteNumber = 0;
		this.createTime = System.currentTimeMillis();
		this.changeTime = System.currentTimeMillis();
	}
}
