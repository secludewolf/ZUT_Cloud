package com.ztu.cloud.cloud.common.vo.admin;

import com.ztu.cloud.cloud.common.vo.user.User;
import lombok.Data;

/**
 * @author Jager
 * @description 管理员登陆数据
 * @date 2020/06/24-10:59
 **/
@Data
public class UserInfo {
	User user;

	public UserInfo() {
	}

	public UserInfo(com.ztu.cloud.cloud.common.bean.mysql.User user) {
		this.user = new User(user);
	}
}
