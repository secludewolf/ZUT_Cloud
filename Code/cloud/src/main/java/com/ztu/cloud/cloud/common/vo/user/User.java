package com.ztu.cloud.cloud.common.vo.user;

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
	private String status;
	private String level;
	private long createTime;
	private long changeTime;
	private long repoSize;

	public User() {
	}

	public User(com.ztu.cloud.cloud.common.bean.mysql.User user) {
		this.id = user.getId();
		this.repoId = user.getRepoId();
		this.account = user.getAccount();
		this.email = user.getEmail();
		this.phone = user.getPhone();
		this.name = user.getName();
		this.status = user.getStatus() == 1 ? "正常" : "异常";
		this.level = user.getLevel() + "级";
		this.createTime = user.getCreateTime();
		this.changeTime = user.getChangeTime();
	}

	public User(com.ztu.cloud.cloud.common.bean.mysql.User user,long repoSize) {
		this.id = user.getId();
		this.repoId = user.getRepoId();
		this.account = user.getAccount();
		this.email = user.getEmail();
		this.phone = user.getPhone();
		this.name = user.getName();
		this.status = user.getStatus() == 1 ? "正常" : "异常";
		this.level = user.getLevel() + "级";
		this.createTime = user.getCreateTime();
		this.changeTime = user.getChangeTime();
		this.repoSize = repoSize;
	}
}
