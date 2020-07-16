package com.ztu.cloud.cloud.common.dao.mongodb;

import com.ztu.cloud.cloud.common.bean.mongodb.ShareRepository;
import com.ztu.cloud.cloud.common.bean.mongodb.inside.Folder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

@SpringBootTest
class ShareRepositoryDaoTest {
    @Autowired
    ShareRepositoryDao shareRepositoryDao;

    @Test
    void getById() {
        ShareRepository shareRepository = new ShareRepository("test", 1, new LinkedList<Long>(), new LinkedList<String>(), new HashMap<Integer, List<Long>>(), new HashMap<Integer, List<Long>>(), new Folder());
        this.shareRepositoryDao.insert(shareRepository);
        shareRepository = this.shareRepositoryDao.getByShareId("test");
        System.out.println(this.shareRepositoryDao.getById(shareRepository.getId()));
        this.shareRepositoryDao.deleteByShareId("test");
    }

    @Test
    void getByShareId() {
        ShareRepository shareRepository = new ShareRepository("test", 1, new LinkedList<Long>(), new LinkedList<String>(), new HashMap<Integer, List<Long>>(), new HashMap<Integer, List<Long>>(), new Folder());
        this.shareRepositoryDao.insert(shareRepository);
        System.out.println(this.shareRepositoryDao.getByShareId("test"));
        this.shareRepositoryDao.deleteByShareId("test");
    }

    @Test
    void getByUserId() {
        ShareRepository shareRepository = new ShareRepository("test", 1, new LinkedList<Long>(), new LinkedList<String>(), new HashMap<Integer, List<Long>>(), new HashMap<Integer, List<Long>>(), new Folder());
        ShareRepository shareRepository2 = new ShareRepository("test2", 1, new LinkedList<Long>(), new LinkedList<String>(), new HashMap<Integer, List<Long>>(), new HashMap<Integer, List<Long>>(), new Folder());
        this.shareRepositoryDao.insert(shareRepository);
        this.shareRepositoryDao.insert(shareRepository2);
        System.out.println(this.shareRepositoryDao.getByUserId(1));
        this.shareRepositoryDao.deleteByShareId("test");
        this.shareRepositoryDao.deleteByShareId("test2");
    }

    @Test
    void updateSaveUserIdMapById() {
        ShareRepository shareRepository = new ShareRepository("test", 1, new LinkedList<Long>(), new LinkedList<String>(), new HashMap<Integer, List<Long>>(), new HashMap<Integer, List<Long>>(), new Folder());
        this.shareRepositoryDao.insert(shareRepository);
        shareRepository = this.shareRepositoryDao.getByShareId("test");
        System.out.println(shareRepository);
        List<Long> list = new LinkedList<>();
        list.add(System.currentTimeMillis());
        shareRepository.getSaveUserIdList().put(1,list);
        this.shareRepositoryDao.updateSaveUserIdMapById(shareRepository.getId(),shareRepository.getSaveUserIdList());
        System.out.println(this.shareRepositoryDao.getByShareId("test"));
        this.shareRepositoryDao.deleteByShareId("test");
    }

    @Test
    void updateDownloadUserIdMapById() {
        ShareRepository shareRepository = new ShareRepository("test", 1, new LinkedList<Long>(), new LinkedList<String>(), new HashMap<Integer, List<Long>>(), new HashMap<Integer, List<Long>>(), new Folder());
        this.shareRepositoryDao.insert(shareRepository);
        shareRepository = this.shareRepositoryDao.getByShareId("test");
        System.out.println(shareRepository);
        List<Long> list = new LinkedList<>();
        list.add(System.currentTimeMillis());
        shareRepository.getDownloadUserIdList().put(1,list);
        this.shareRepositoryDao.updateDownloadUserIdMapById(shareRepository.getId(),shareRepository.getDownloadUserIdList());
        System.out.println(this.shareRepositoryDao.getByShareId("test"));
        this.shareRepositoryDao.deleteByShareId("test");
    }

    @Test
    void insert() {
        ShareRepository shareRepository = new ShareRepository("test", 1, new LinkedList<Long>(), new LinkedList<String>(), new HashMap<Integer, List<Long>>(), new HashMap<Integer, List<Long>>(), new Folder());
        System.out.println(this.shareRepositoryDao.insert(shareRepository));
        this.shareRepositoryDao.deleteByShareId("test");
    }

    @Test
    void deleteById() {
        ShareRepository shareRepository = new ShareRepository("test", 1, new LinkedList<Long>(), new LinkedList<String>(), new HashMap<Integer, List<Long>>(), new HashMap<Integer, List<Long>>(), new Folder());
        this.shareRepositoryDao.insert(shareRepository);
        shareRepository = this.shareRepositoryDao.getByShareId("test");
        this.shareRepositoryDao.deleteById(shareRepository.getId()+"");
    }

    @Test
    void deleteByShareId() {
        ShareRepository shareRepository = new ShareRepository("test", 1, new LinkedList<Long>(), new LinkedList<String>(), new HashMap<Integer, List<Long>>(), new HashMap<Integer, List<Long>>(), new Folder());
        this.shareRepositoryDao.insert(shareRepository);
        System.out.println(this.shareRepositoryDao.deleteByShareId("test")+"");
    }
}