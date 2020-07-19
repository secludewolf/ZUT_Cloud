package com.ztu.cloud.cloud.common.vo.admin;

import lombok.Data;

/**
 * @author Jager
 * @description 管理员登陆数据
 * @date 2020/06/24-10:59
 **/
@Data
public class AdminLogin {
	Admin admin;

	public AdminLogin() {
	}

	public AdminLogin(com.ztu.cloud.cloud.common.bean.mysql.Admin admin) {
		this.admin = new Admin(admin);
	}
}
