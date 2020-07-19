package com.ztu.cloud.cloud.common.vo.user;

import lombok.Data;

/**
 * @author Jager
 * @description 查看用户信息
 * @date 2020/06/23-19:58
 **/
@Data
public class UserInfo {
	private User user;

	public UserInfo() {
	}

	public UserInfo(com.ztu.cloud.cloud.common.bean.mysql.User user) {
		this.user = new User(user);
	}
}
