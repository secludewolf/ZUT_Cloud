package com.ztu.cloud.cloud.common.dao.redis;

import com.ztu.cloud.cloud.common.bean.redis.Download;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author Jager
 * @description 下载信息
 * @date 2020/06/27-11:03
 **/
@Component
public class DownLoadDao {
	RedisTemplate<String, String> redisTemplate;

	public DownLoadDao(RedisTemplate<String, String> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	public int insert(Download download) {
		BoundHashOperations<String, Object, Object> operations = this.redisTemplate.boundHashOps(download.getId());
		operations.expire(1, TimeUnit.DAYS);
		operations.put("id", download.getId());
		operations.put("fileId", download.getFileId());
		operations.put("name", download.getName());
		if (download.getShareId() != null) {
			operations.put("shareId", download.getShareId());
		}
		return 1;
	}

	public int delete(String id) {
		this.redisTemplate.delete(id);
		return 1;
	}

	public int update(Download download) {
		BoundHashOperations<String, Object, Object> operations = this.redisTemplate.boundHashOps(download.getId());
		operations.expire(1, TimeUnit.DAYS);
		operations.put("id", download.getId());
		operations.put("fileId", download.getFileId());
		operations.put("name", download.getName());
		if (download.getShareId() != null) {
			operations.put("shareId", download.getShareId());
		}
		return 1;
	}

	public Download get(String id) {
		BoundHashOperations<String, Object, Object> operations = this.redisTemplate.boundHashOps(id);
		Download download = new Download();
		if (operations.get("fileId") == null) {
			return null;
		}
		download.setId((String) operations.get("id"));
		download.setFileId((String) operations.get("fileId"));
		download.setName((String) operations.get("name"));
		if (operations.get("shareId") != null) {
			download.setShareId((String) operations.get("shareId"));
		}
		return download;
	}
}
