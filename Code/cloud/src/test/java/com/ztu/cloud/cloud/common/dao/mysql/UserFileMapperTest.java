package com.ztu.cloud.cloud.common.dao.mysql;

import com.ztu.cloud.cloud.common.bean.mysql.UserFile;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UserFileMapperTest {
    @Autowired
    UserFileMapper userFileMapper;
    @Test
    void insertUserFile() {
        UserFile userFile = new UserFile(1,"test","test");
        System.out.println(this.userFileMapper.insertUserFile(userFile));
        this.userFileMapper.deleteUserFile(userFile.getId());
    }

    @Test
    void getUserFile() {
        UserFile userFile = new UserFile(1,"test","test");
        this.userFileMapper.insertUserFile(userFile);
        System.out.println(this.userFileMapper.getUserFile(userFile.getId()));
        this.userFileMapper.deleteUserFile(userFile.getId());
    }

    @Test
    void deleteUserFile() {
        UserFile userFile = new UserFile(1,"test","test");
        this.userFileMapper.insertUserFile(userFile);
        System.out.println(this.userFileMapper.deleteUserFile(userFile.getId()));
    }

    @Test
    void updateUserFilePath() {
        UserFile userFile = new UserFile(1,"test","test");
        this.userFileMapper.insertUserFile(userFile);
        System.out.println(this.userFileMapper.updateUserFilePath(userFile.getId(), "test2"));
        this.userFileMapper.deleteUserFile(userFile.getId());
    }
}