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
public class UserList {
	List<User> userList;
	long userCount;

	public UserList() {
	}

	public UserList(long userCount, List<com.ztu.cloud.cloud.common.bean.mysql.User> userList) {
		this.userCount = userCount;
		this.userList = new LinkedList<User>();
		for (com.ztu.cloud.cloud.common.bean.mysql.User user : userList) {
			this.userList.add(new User(user));
		}
	}
}
