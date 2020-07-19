package com.ztu.cloud.cloud.common.vo.admin;

import lombok.Data;

/**
 * @author Jager
 * @description 用户信息
 * @date 2020/06/23-12:36
 **/
@Data
public class User {
	private int id;
	private String repoId;
	private String account;
	private String email;
	private String phone;
	private String name;
	private String password;
	private int status;
	private int level;
	private long createTime;
	private long changeTime;

	public User() {
	}

	public User(com.ztu.cloud.cloud.common.bean.mysql.User user) {
		this.id = user.getId();
		this.repoId = user.getRepoId();
		this.account = user.getAccount();
		this.email = user.getEmail();
		this.phone = user.getPhone();
		this.name = user.getName();
		this.password = user.getPassword();
		this.status = user.getStatus();
		this.level = user.getLevel();
		this.createTime = user.getCreateTime();
		this.changeTime = user.getChangeTime();
	}
}
