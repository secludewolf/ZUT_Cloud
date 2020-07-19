package com.ztu.cloud.cloud.common.dao.mongodb;

import com.ztu.cloud.cloud.common.bean.mongodb.ShareRepository;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @author Jager
 * @description 分享仓库信息操作
 * @date 2020/06/22-21:48
 **/
@Component
public class ShareRepositoryDao {
	MongoTemplate mongoTemplate;

	public ShareRepositoryDao(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}

	public ShareRepository getById(String id) {
		return this.mongoTemplate.findById(new ObjectId(id), ShareRepository.class);
	}

	public ShareRepository getByShareId(String shareId) {
		Query query = new Query(Criteria.where("shareId").is(shareId));
		return this.mongoTemplate.findOne(query, ShareRepository.class);
	}

	public List<ShareRepository> getByUserId(int userId) {
		Query query = new Query(Criteria.where("userId").is(userId));
		return this.mongoTemplate.find(query, ShareRepository.class);
	}

	public int updateSaveUserIdMapById(String id, Map<Integer, List<Long>> saveUserIdList) {
		Query query = new Query(Criteria.where("id").is(new ObjectId(id)));
		Update update = new Update();
		update.set("saveUserIdList", saveUserIdList);
		return (int) this.mongoTemplate.updateFirst(query, update, ShareRepository.class).getMatchedCount();
	}

	public int updateDownloadUserIdMapById(String id, Map<Integer, List<Long>> downloadUserIdList) {
		Query query = new Query(Criteria.where("id").is(new ObjectId(id)));
		Update update = new Update();
		update.set("downloadUserIdList", downloadUserIdList);
		return (int) this.mongoTemplate.updateFirst(query, update, ShareRepository.class).getMatchedCount();
	}

	public int insert(ShareRepository shareRepository) {
		try {
			this.mongoTemplate.insert(shareRepository);
		} catch (Exception e) {
			return 0;
		}
		return 1;
	}

	public int deleteById(String id) {
		Query query = new Query(Criteria.where("id").is(new ObjectId(id)));
		return (int) this.mongoTemplate.remove(query, ShareRepository.class).getDeletedCount();
	}

	public int deleteByShareId(String shareId) {
		Query query = new Query(Criteria.where("shareId").is(shareId));
		return (int) this.mongoTemplate.remove(query, ShareRepository.class).getDeletedCount();
	}
}
