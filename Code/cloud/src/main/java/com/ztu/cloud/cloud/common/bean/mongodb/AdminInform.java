package com.ztu.cloud.cloud.common.bean.mongodb;

import lombok.Data;

import java.util.Map;

/**
 * @author Jager
 * @description 管理员通知文档
 * @date 2020/06/22-10:19
 **/
@Data
public class AdminInform {
	private int id;
	private Map<String, Integer> status;

	public AdminInform() {
	}

	public AdminInform(int id, Map<String, Integer> status) {
		this.id = id;
		this.status = status;
	}
}
