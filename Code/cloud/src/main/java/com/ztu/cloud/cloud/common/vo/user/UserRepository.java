package com.ztu.cloud.cloud.common.vo.user;

import com.ztu.cloud.cloud.common.bean.mongodb.inside.Folder;
import com.ztu.cloud.cloud.common.bean.mongodb.inside.RecycleBin;
import lombok.Data;

/**
 * @author Jager
 * @description 用户仓库信息
 * @date 2020/06/23-12:42
 **/
@Data
public class UserRepository {

	private String id;
	private int userId;
	private int status;
	private long repoSize;
	private long useSize;
	private Folder folder;
	private RecycleBin recycleBin;

	public UserRepository() {
	}

	public UserRepository(com.ztu.cloud.cloud.common.bean.mongodb.UserRepository userRepository) {
		this.id = userRepository.getId();
		this.userId = userRepository.getUserId();
		this.status = userRepository.getStatus();
		this.repoSize = userRepository.getRepoSize();
		this.useSize = userRepository.getUseSize();
		this.folder = userRepository.getFolder();
		this.recycleBin = userRepository.getRecycleBin();
	}
}