package com.ztu.cloud.cloud.common.dao.mongodb;

import com.ztu.cloud.cloud.common.bean.mongodb.UserInform;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author Jager
 * @description 用户通知关系表
 * @date 2020/06/23-09:50
 **/
@Component
public class UserInformDao {
	MongoTemplate mongoTemplate;

	public UserInformDao(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}

	public UserInform getById(int id) {
		return this.mongoTemplate.findById(id, UserInform.class);
	}

	public int insert(UserInform userInform) {
		try {
			this.mongoTemplate.insert(userInform);
		} catch (Exception e) {
			return 0;
		}
		return 1;
	}

	public int updateStatusById(int id, Map<String, Integer> status) {
		Query query = new Query(Criteria.where("id").is(id));
		Update update = new Update();
		update.set("status", status);
		return (int) this.mongoTemplate.updateFirst(query, update, UserInform.class).getMatchedCount();
	}

	public int deleteById(int id) {
		Query query = new Query(Criteria.where("id").is(id));
		return (int) this.mongoTemplate.remove(query, UserInform.class).getDeletedCount();
	}
}
