package com.ztu.cloud.cloud.common.vo.admin;

import lombok.Data;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Jager
 * @description 用户列表信息
 * @date 2020/06/24-16:10
 **/
@Data
public class AdminList {
	List<Admin> adminList;
	long pageLength;

	public AdminList() {
	}

	public AdminList(long pageLength, List<com.ztu.cloud.cloud.common.bean.mysql.Admin> adminList) {
		this.pageLength = pageLength;
		this.adminList = new LinkedList<Admin>();
		for (com.ztu.cloud.cloud.common.bean.mysql.Admin admin : adminList) {
			this.adminList.add(new Admin(admin));
		}
	}
}
