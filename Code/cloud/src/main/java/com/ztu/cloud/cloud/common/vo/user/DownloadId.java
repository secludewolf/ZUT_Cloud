package com.ztu.cloud.cloud.common.vo.user;

import lombok.Data;

/**
 * @author Jager
 * @description 下载信息
 * @date 2020/06/27-18:08
 **/
@Data
public class DownloadId {
	private String id;

	public DownloadId(String id) {
		this.id = id;
	}
}
