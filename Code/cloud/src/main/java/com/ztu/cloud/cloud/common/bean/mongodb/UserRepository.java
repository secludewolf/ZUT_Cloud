package com.ztu.cloud.cloud.common.bean.mongodb;

import com.ztu.cloud.cloud.common.bean.mongodb.inside.Folder;
import com.ztu.cloud.cloud.common.bean.mongodb.inside.RecycleBin;
import lombok.Data;
import org.bson.types.ObjectId;

/**
 * @author Jager
 * @description 用户仓库信息文档
 * @date 2020/06/22-10:17
 **/
@Data
public class UserRepository {

	private ObjectId id;
	private int userId;
	private int status;
	private long repoSize;
	private long useSize;
	private Folder folder;
	private RecycleBin recycleBin;

	public UserRepository() {
	}

	public UserRepository(int userId, int status, long repoSize, long useSize, Folder folder, RecycleBin recycleBin) {
		this.userId = userId;
		this.status = status;
		this.repoSize = repoSize;
		this.useSize = useSize;
		this.folder = folder;
		this.recycleBin = recycleBin;
	}

	public UserRepository(int userId) {
		this.userId = userId;
		this.status = 1;
		this.repoSize = 1099511627776L;
		this.useSize = 0;
		this.folder = new Folder("root", "", 0);
		this.recycleBin = new RecycleBin();
	}


	public String getId() {
		return id.toString();
	}

	public void setId(String id) {
		this.id = new ObjectId(id);
	}
}
