package com.ztu.cloud.cloud.common.vo.user;


import lombok.Data;

/**
 * @author Jager
 * @description 仓库信息
 * @date 2020/06/25-17:36
 **/
@Data
public class RepositoryInfo {
	private UserRepository repository;

	public RepositoryInfo(com.ztu.cloud.cloud.common.bean.mongodb.UserRepository repository) {
		this.repository = new UserRepository(repository);
	}
}
