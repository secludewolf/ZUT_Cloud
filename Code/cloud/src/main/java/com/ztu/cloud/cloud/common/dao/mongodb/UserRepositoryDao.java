package com.ztu.cloud.cloud.common.dao.mongodb;

import com.ztu.cloud.cloud.common.bean.mongodb.UserRepository;
import com.ztu.cloud.cloud.common.bean.mongodb.inside.Folder;
import com.ztu.cloud.cloud.common.bean.mongodb.inside.RecycleBin;
import lombok.ToString;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

/**
 * @author Jager
 * @description 用户仓库信息操作
 * @date 2020/06/22-11:16
 **/
@ToString
@Component
public class UserRepositoryDao {
    MongoTemplate mongoTemplate;

    public UserRepositoryDao(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public UserRepository getById(String id) {
        return this.mongoTemplate.findById(new ObjectId(id), UserRepository.class);
    }

    public UserRepository getByUserId(int userId) {
        Query query = new Query(Criteria.where("userId").is(userId));
        return this.mongoTemplate.findOne(query, UserRepository.class);
    }

    public int updateRepoSizeById(String id, long repoSize) {
        Query query = new Query(Criteria.where("id").is(new ObjectId(id)));
        Update update = new Update();
        update.set("repoSize", repoSize);
        return (int) this.mongoTemplate.updateFirst(query, update, UserRepository.class).getMatchedCount();
    }

    public int updateUseSizeById(String id, long useSize) {
        Query query = new Query(Criteria.where("id").is(new ObjectId(id)));
        Update update = new Update();
        update.set("useSize", useSize);
        return (int) this.mongoTemplate.updateFirst(query, update, UserRepository.class).getMatchedCount();
    }

    public int updateFolderById(String id, Folder folder) {
        Query query = new Query(Criteria.where("id").is(new ObjectId(id)));
        Update update = new Update();
        update.set("folder", folder);
        return (int) this.mongoTemplate.updateFirst(query, update, UserRepository.class).getMatchedCount();
    }

    public int updateRecycleBinById(String id, RecycleBin recycleBin) {
        Query query = new Query(Criteria.where("id").is(new ObjectId(id)));
        Update update = new Update();
        update.set("recycleBin", recycleBin);
        return (int) this.mongoTemplate.updateFirst(query, update, UserRepository.class).getMatchedCount();
    }

    public int updateStatusById(String id, int status) {
        Query query = new Query(Criteria.where("id").is(new ObjectId(id)));
        Update update = new Update();
        update.set("status", status);
        return (int) this.mongoTemplate.updateFirst(query, update, UserRepository.class).getMatchedCount();
    }

    public int insert(UserRepository userRepository) {
        try {
            this.mongoTemplate.insert(userRepository);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
        return 1;
    }

    public int deleteById(String id) {
        Query query = new Query(Criteria.where("id").is(new ObjectId(id)));
        return (int) this.mongoTemplate.remove(query, UserRepository.class).getDeletedCount();
    }

    public int deleteByUserId(int userId) {
        Query query = new Query(Criteria.where("userId").is(userId));
        return (int) this.mongoTemplate.remove(query, UserRepository.class).getDeletedCount();
    }
}
