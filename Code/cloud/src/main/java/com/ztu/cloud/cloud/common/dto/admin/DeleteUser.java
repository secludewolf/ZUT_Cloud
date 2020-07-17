package com.ztu.cloud.cloud.common.dto.admin;

import lombok.Data;

/**
 * @author Jager
 * @description 删除用户数据
 * @date 2020/06/24-15:25
 **/
@Data
public class DeleteUser {
	private int id;
	private String account;
	private String email;
	private String phone;
}
