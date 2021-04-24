package com.ztu.cloud.cloud.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.convert.DbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;

/**
 * @author Jager
 * @description mongo数据库配置文件
 * @date 2020/06/26-17:30
 **/
@Configuration
public class MongodbConfig {
    public MongoDbFactory mongoFactory;
    public MongoMappingContext mongoMappingContext;

    public MongodbConfig(MongoDbFactory mongoFactory, MongoMappingContext mongoMappingContext) {
        this.mongoFactory = mongoFactory;
        this.mongoMappingContext = mongoMappingContext;
    }

    @Bean
    public MappingMongoConverter mongoConverter() {
        DbRefResolver dbRefResolver = new DefaultDbRefResolver(mongoFactory);
        MappingMongoConverter mongoConverter = new MappingMongoConverter(dbRefResolver, mongoMappingContext);
        //配置.替换符
        mongoConverter.setMapKeyDotReplacement(":");
        mongoConverter.afterPropertiesSet();
        return mongoConverter;
    }
}
