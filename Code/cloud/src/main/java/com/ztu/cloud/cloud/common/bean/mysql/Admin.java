package com.ztu.cloud.cloud.common.bean.mysql;

import lombok.Data;

/**
 * @author Jager
 * @description 管理员信息表
 * @date 2020/06/21-12:40
 **/
@Data
public class Admin {
	private int id;
	private String account;
	private String password;
	private String email;
	private String phone;
	private String name;
	private int status;
	private int level;
	private long createTime;
	private long changeTime;

	public Admin() {
	}

	public Admin(String account, String password, String email, String phone, String name, int status, int level, long createTime, long changeTime) {
		this.account = account;
		this.password = password;
		this.email = email;
		this.phone = phone;
		this.name = name;
		this.status = status;
		this.level = level;
		this.createTime = createTime;
		this.changeTime = changeTime;
	}

	public Admin(String account, String password, String email, String phone, String name, int level) {
		this.account = account;
		this.password = password;
		this.email = email;
		this.phone = phone;
		this.name = name;
		this.status = 1;
		this.level = level;
		this.createTime = System.currentTimeMillis();
		this.changeTime = System.currentTimeMillis();
	}
}
