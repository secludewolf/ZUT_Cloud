package com.ztu.cloud.cloud.common.dto.common;

import lombok.Data;

/**
 * @author Jager
 * @description 修改用户信息请求数据
 * @date 2020/06/23-20:02
 **/
@Data
public class ChangeUserInfo {
	private int id;
	private String account;
	private String email;
	private String phone;
	private String name;

	public ChangeUserInfo() {
	}

	public ChangeUserInfo(int id, String account, String name) {
		this.id = id;
		this.account = account;
		this.name = name;
	}
}
