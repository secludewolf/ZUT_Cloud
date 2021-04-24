package com.ztu.cloud.cloud.common.dao.mongodb;

import com.ztu.cloud.cloud.common.bean.mongodb.UserRepository;
import com.ztu.cloud.cloud.common.bean.mongodb.inside.Folder;
import com.ztu.cloud.cloud.common.bean.mongodb.inside.RecycleBin;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UserRepositoryDaoTest {
    @Autowired
    UserRepositoryDao userRepositoryDao;

    @Test
    void getById() {
        UserRepository userRepository = new UserRepository(1, 1, 1, 1, new Folder(), new RecycleBin());
        this.userRepositoryDao.insert(userRepository);
        userRepository = this.userRepositoryDao.getByUserId(1);
        System.out.println(this.userRepositoryDao.getById(userRepository.getId()));
        this.userRepositoryDao.deleteByUserId(1);
    }

    @Test
    void getByUserId() {
        UserRepository userRepository = new UserRepository(1, 1, 1, 1, new Folder(), new RecycleBin());
        this.userRepositoryDao.insert(userRepository);
        System.out.println(this.userRepositoryDao.getByUserId(1));
        this.userRepositoryDao.deleteByUserId(1);
    }

    @Test
    void updateRepoSizeById() {
        UserRepository userRepository = new UserRepository(1, 1, 1, 1, new Folder(), new RecycleBin());
        this.userRepositoryDao.insert(userRepository);
        userRepository = this.userRepositoryDao.getByUserId(1);
        System.out.println(this.userRepositoryDao.updateRepoSizeById(userRepository.getId(), 2));
        this.userRepositoryDao.deleteByUserId(1);
    }

    @Test
    void updateUseSizeById() {
        UserRepository userRepository = new UserRepository(1, 1, 1, 1, new Folder(), new RecycleBin());
        this.userRepositoryDao.insert(userRepository);
        userRepository = this.userRepositoryDao.getByUserId(1);
        System.out.println(this.userRepositoryDao.updateUseSizeById(userRepository.getId(), 2));
        this.userRepositoryDao.deleteByUserId(1);
    }

    @Test
    void updateFolderById() {
        UserRepository userRepository = new UserRepository(1, 1, 1, 1, new Folder(), new RecycleBin());
        this.userRepositoryDao.insert(userRepository);
        userRepository = this.userRepositoryDao.getByUserId(1);
        System.out.println(this.userRepositoryDao.updateFolderById(userRepository.getId(), new Folder()));
        this.userRepositoryDao.deleteByUserId(1);
    }

    @Test
    void updateRecycleBinById() {
        UserRepository userRepository = new UserRepository(1, 1, 1, 1, new Folder(), new RecycleBin());
        this.userRepositoryDao.insert(userRepository);
        userRepository = this.userRepositoryDao.getByUserId(1);
        System.out.println(this.userRepositoryDao.updateRecycleBinById(userRepository.getId(), new RecycleBin()));
        this.userRepositoryDao.deleteByUserId(1);
    }

    @Test
    void updateStatusById() {
        UserRepository userRepository = new UserRepository(1, 1, 1, 1, new Folder(), new RecycleBin());
        this.userRepositoryDao.insert(userRepository);
        userRepository = this.userRepositoryDao.getByUserId(1);
        System.out.println(this.userRepositoryDao.updateStatusById(userRepository.getId(), 2));
        this.userRepositoryDao.deleteByUserId(1);
    }

    @Test
    void insert() {
        UserRepository userRepository = new UserRepository(1, 1, 1, 1, new Folder(), new RecycleBin());
        System.out.println(this.userRepositoryDao.insert(userRepository));
        this.userRepositoryDao.deleteByUserId(1);
    }

    @Test
    void deleteById() {
        UserRepository userRepository = new UserRepository(1, 1, 1, 1, new Folder(), new RecycleBin());
        this.userRepositoryDao.insert(userRepository);
        userRepository = this.userRepositoryDao.getByUserId(1);
        System.out.println(this.userRepositoryDao.deleteById(userRepository.getId()));
    }

    @Test
    void deleteByUserId() {
        UserRepository userRepository = new UserRepository(1, 1, 1, 1, new Folder(), new RecycleBin());
        this.userRepositoryDao.insert(userRepository);
        System.out.println(this.userRepositoryDao.deleteByUserId(1));
    }
}