package com.ztu.cloud.cloud.common.dao.mongodb;
import com.ztu.cloud.cloud.common.bean.mongodb.CommonAdminInform;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Jager
 * @description 公共用户通知信息操作
 * @date 2020/06/23-08:55
 **/
@Component
public class CommonAdminInformDao {
    MongoTemplate mongoTemplate;

    public CommonAdminInformDao(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public CommonAdminInform getById(String id) {
        return this.mongoTemplate.findById(new ObjectId(id), CommonAdminInform.class);
    }

    public List<CommonAdminInform> getValidList() {
        return getValidListAfterTime(0);
    }

    public List<CommonAdminInform> getValidListAfterTime(long time) {
        Query query = new Query(Criteria.where("createTime").gt(time)
                .and("validTime").gt(System.currentTimeMillis())
                .and("status").is(1));
        return this.mongoTemplate.find(query, CommonAdminInform.class);
    }

    public List<CommonAdminInform> getAll(){
        return this.mongoTemplate.findAll(CommonAdminInform.class);
    }

    public int insert(CommonAdminInform inform) {
        try {
            this.mongoTemplate.insert(inform);
        } catch (Exception e) {
            return 0;
        }
        return 1;
    }

    public int updateHeaderById(String id, String header) {
        Query query = new Query(Criteria.where("id").is(new ObjectId(id)));
        Update update = new Update();
        update.set("header", header);
        return (int) this.mongoTemplate.updateFirst(query, update, CommonAdminInform.class).getMatchedCount();
    }

    public int updateContentById(String id, String content) {
        Query query = new Query(Criteria.where("id").is(new ObjectId(id)));
        Update update = new Update();
        update.set("content", content);
        return (int) this.mongoTemplate.updateFirst(query, update, CommonAdminInform.class).getMatchedCount();
    }

    public int updateValidTimeById(String id, long validTime) {
        Query query = new Query(Criteria.where("id").is(new ObjectId(id)));
        Update update = new Update();
        update.set("validTime", validTime);
        return (int) this.mongoTemplate.updateFirst(query, update, CommonAdminInform.class).getMatchedCount();
    }

    public int updateStatusById(String id, int status) {
        Query query = new Query(Criteria.where("id").is(new ObjectId(id)));
        Update update = new Update();
        update.set("status", status);
        return (int) this.mongoTemplate.updateFirst(query, update, CommonAdminInform.class).getMatchedCount();
    }

    public int deleteById(String id) {
        Query query = new Query(Criteria.where("id").is(new ObjectId(id)));
        return (int) this.mongoTemplate.remove(query, CommonAdminInform.class).getDeletedCount();
    }
}
