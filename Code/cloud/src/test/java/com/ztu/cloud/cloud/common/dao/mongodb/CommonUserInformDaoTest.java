package com.ztu.cloud.cloud.common.dao.mongodb;

import com.ztu.cloud.cloud.common.bean.mongodb.CommonUserInform;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CommonUserInformDaoTest {

    @Autowired
    CommonUserInformDao commonUserInformDao;

    @Test
    void getById() {
        CommonUserInform inform = new CommonUserInform("header", "content", System.currentTimeMillis(), System.currentTimeMillis() + 1000000000, 1, 1);
        this.commonUserInformDao.insert(inform);
        System.out.println(this.commonUserInformDao.getById(inform.getId()));
        this.commonUserInformDao.deleteById(inform.getId());
    }

    @Test
    void getValidList() {
        CommonUserInform inform = new CommonUserInform("header", "content", System.currentTimeMillis() + 1000, System.currentTimeMillis() + 1000, 1, 1);
        CommonUserInform inform2 = new CommonUserInform("header2", "content2", System.currentTimeMillis() - 1000, System.currentTimeMillis() + 1000, 1, 1);
        CommonUserInform inform3 = new CommonUserInform("header3", "content3", System.currentTimeMillis(), System.currentTimeMillis() - 1000, 1, 1);
        this.commonUserInformDao.insert(inform);
        this.commonUserInformDao.insert(inform2);
        this.commonUserInformDao.insert(inform3);
        System.out.println(this.commonUserInformDao.getValidList());
        this.commonUserInformDao.deleteById(inform.getId());
        this.commonUserInformDao.deleteById(inform2.getId());
        this.commonUserInformDao.deleteById(inform3.getId());
    }

    @Test
    void getValidListAfterTime() {
        CommonUserInform inform = new CommonUserInform("header", "content", System.currentTimeMillis() + 1000, System.currentTimeMillis() + 1000, 1, 1);
        CommonUserInform inform2 = new CommonUserInform("header2", "content2", System.currentTimeMillis() - 1000, System.currentTimeMillis() + 1000, 1, 1);
        CommonUserInform inform3 = new CommonUserInform("header3", "content3", System.currentTimeMillis(), System.currentTimeMillis() - 1000, 1, 1);
        this.commonUserInformDao.insert(inform);
        this.commonUserInformDao.insert(inform2);
        this.commonUserInformDao.insert(inform3);
        System.out.println(this.commonUserInformDao.getValidListAfterTime(System.currentTimeMillis()));
        this.commonUserInformDao.deleteById(inform.getId());
        this.commonUserInformDao.deleteById(inform2.getId());
        this.commonUserInformDao.deleteById(inform3.getId());
    }

    @Test
    void getAll() {
        CommonUserInform inform = new CommonUserInform("header", "content", System.currentTimeMillis() + 1000, System.currentTimeMillis() + 1000, 1, 1);
        CommonUserInform inform2 = new CommonUserInform("header2", "content2", System.currentTimeMillis() - 1000, System.currentTimeMillis() + 1000, 1, 1);
        CommonUserInform inform3 = new CommonUserInform("header3", "content3", System.currentTimeMillis(), System.currentTimeMillis() - 1000, 1, 1);
        this.commonUserInformDao.insert(inform);
        this.commonUserInformDao.insert(inform2);
        this.commonUserInformDao.insert(inform3);
        System.out.println(this.commonUserInformDao.getAll());
        this.commonUserInformDao.deleteById(inform.getId());
        this.commonUserInformDao.deleteById(inform2.getId());
        this.commonUserInformDao.deleteById(inform3.getId());
    }

    @Test
    void insert() {
        CommonUserInform inform = new CommonUserInform("header", "content", System.currentTimeMillis(), System.currentTimeMillis() + 1000000000, 1, 1);
        System.out.println(this.commonUserInformDao.insert(inform));
        this.commonUserInformDao.deleteById(inform.getId());
    }

    @Test
    void updateHeaderById() {
        CommonUserInform inform = new CommonUserInform("header", "content", System.currentTimeMillis(), System.currentTimeMillis() + 1000000000, 1, 1);
        this.commonUserInformDao.insert(inform);
        inform.setHeader("header改");
        this.commonUserInformDao.updateHeaderById(inform.getId(), inform.getHeader());
        System.out.println(this.commonUserInformDao.getById(inform.getId()));
        this.commonUserInformDao.deleteById(inform.getId());
    }

    @Test
    void updateContentById() {
        CommonUserInform inform = new CommonUserInform("header", "content", System.currentTimeMillis(), System.currentTimeMillis() + 1000000000, 1, 1);
        this.commonUserInformDao.insert(inform);
        inform.setContent("content改");
        this.commonUserInformDao.updateContentById(inform.getId(), inform.getContent());
        System.out.println(this.commonUserInformDao.getById(inform.getId()));
        this.commonUserInformDao.deleteById(inform.getId());
    }

    @Test
    void updateValidTimeById() {
        CommonUserInform inform = new CommonUserInform("header", "content", System.currentTimeMillis(), System.currentTimeMillis() + 1000000000, 1, 1);
        this.commonUserInformDao.insert(inform);
        inform.setValidTime(0);
        this.commonUserInformDao.updateValidTimeById(inform.getId(), inform.getValidTime());
        System.out.println(this.commonUserInformDao.getById(inform.getId()));
        this.commonUserInformDao.deleteById(inform.getId());
    }

    @Test
    void updateStatusById() {
        CommonUserInform inform = new CommonUserInform("header", "content", System.currentTimeMillis(), System.currentTimeMillis() + 1000000000, 1, 1);
        this.commonUserInformDao.insert(inform);
        inform.setStatus(0);
        this.commonUserInformDao.updateStatusById(inform.getId(), inform.getStatus());
        System.out.println(this.commonUserInformDao.getById(inform.getId()));
        this.commonUserInformDao.deleteById(inform.getId());
    }

    @Test
    void deleteById() {
        CommonUserInform inform = new CommonUserInform("header", "content", System.currentTimeMillis(), System.currentTimeMillis() + 1000000000, 1, 1);
        this.commonUserInformDao.insert(inform);
        System.out.println(this.commonUserInformDao.deleteById(inform.getId()));
    }
}