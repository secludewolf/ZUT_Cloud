package com.ztu.cloud.cloud.common.dao.mongodb;

import com.ztu.cloud.cloud.common.bean.mongodb.UserInform;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest
class UserInformDaoTest {
    @Autowired
    UserInformDao userInformDao;

    @Test
    void getById() {
        Map<String, Integer> status = new HashMap<>();
        status.put("test", 1);
        UserInform userInform = new UserInform(1, status);
        this.userInformDao.insert(userInform);
        System.out.println(this.userInformDao.getById(userInform.getId()));
        this.userInformDao.deleteById(userInform.getId());
    }

    @Test
    void insert() {
        Map<String, Integer> status = new HashMap<>();
        status.put("test", 1);
        UserInform userInform = new UserInform(1, status);
        this.userInformDao.insert(userInform);
        System.out.println(this.userInformDao.getById(userInform.getId()));
        this.userInformDao.deleteById(userInform.getId());
    }

    @Test
    void updateStatusById() {
        Map<String, Integer> status = new HashMap<>();
        status.put("test", 1);
        UserInform userInform = new UserInform(1, status);
        this.userInformDao.insert(userInform);
        System.out.println(this.userInformDao.getById(userInform.getId()));
        status.put("test2",2);
        this.userInformDao.updateStatusById(userInform.getId(),status);
        System.out.println(this.userInformDao.getById(userInform.getId()));
        this.userInformDao.deleteById(userInform.getId());
    }

    @Test
    void deleteById() {
        Map<String, Integer> status = new HashMap<>();
        status.put("test", 1);
        UserInform userInform = new UserInform(1, status);
        this.userInformDao.insert(userInform);
        System.out.println(this.userInformDao.deleteById(userInform.getId()));
    }
}