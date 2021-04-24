package com.ztu.cloud.cloud.common.dao.mysql;

import com.ztu.cloud.cloud.common.bean.mysql.File;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class FileMapperTest {

    @Autowired
    FileMapper fileMapper;

    @Test
    void insertFile() {
        File file = new File("test1", "test1", "test1", 1, 1, "test1", 1, System.currentTimeMillis(), System.currentTimeMillis());
        this.fileMapper.insertFile(file);
        System.out.println(this.fileMapper.getFileById(file.getId()));
        this.fileMapper.deleteFileById(file.getId());
    }

    @Test
    void updateFile() {
        File file = new File("test2", "test1", "test1", 1, 1, "test1", 1, System.currentTimeMillis(), System.currentTimeMillis());
        this.fileMapper.insertFile(file);
        System.out.println(this.fileMapper.getFileById(file.getId()));
        file.setName("test2");
        this.fileMapper.updateFile(file);
        System.out.println(this.fileMapper.getFileById(file.getId()));
        this.fileMapper.deleteFileById(file.getId());
    }

    @Test
    void getFileById() {
        File file = new File("test3", "test1", "test1", 1, 1, "test1", 1, System.currentTimeMillis(), System.currentTimeMillis());
        this.fileMapper.insertFile(file);
        System.out.println(this.fileMapper.getFileById(file.getId()));
        this.fileMapper.deleteFileById(file.getId());
    }

    @Test
    public void getFile() {
        // File file = new File("test3", "test1", "test1", 1, 1, "test1", 1, System.currentTimeMillis(), System.currentTimeMillis());
        // this.fileMapper.insertFile(file);
        // System.out.println(this.fileMapper.getFile(0, 2));
        // this.fileMapper.deleteFileById(file.getId());
    }

    @Test
    public void getFileCount() {
        File file = new File("test3", "test1", "test1", 1, 1, "test1", 1, System.currentTimeMillis(), System.currentTimeMillis());
        this.fileMapper.insertFile(file);
        System.out.println(this.fileMapper.getFileCount());
        this.fileMapper.deleteFileById(file.getId());
    }

    @Test
    void fileQuoteNumberAdd() {
        File file = new File("test4", "test1", "test1", 1, 1, "test1", 1, System.currentTimeMillis(), System.currentTimeMillis());
        this.fileMapper.insertFile(file);
        System.out.println(this.fileMapper.getFileById(file.getId()));
        this.fileMapper.fileQuoteNumberAdd(file.getId(), 1);
        System.out.println(this.fileMapper.getFileById(file.getId()));
        this.fileMapper.deleteFileById(file.getId());
    }

    @Test
    void fileQuoteNumberSub() {
        File file = new File("test5", "test1", "test1", 1, 1, "test1", 1, System.currentTimeMillis(), System.currentTimeMillis());
        this.fileMapper.insertFile(file);
        System.out.println(this.fileMapper.getFileById(file.getId()));
        this.fileMapper.fileQuoteNumberSub(file.getId(), 1);
        System.out.println(this.fileMapper.getFileById(file.getId()));
        this.fileMapper.deleteFileById(file.getId());
    }

    @Test
    void getFiles() {
        File file = new File("test5", "test1", "test1", 1, 1, "test1", 1, System.currentTimeMillis(), System.currentTimeMillis());
        this.fileMapper.insertFile(file);
        List<File> files = this.fileMapper.getFiles(new com.ztu.cloud.cloud.common.dto.condition.File("新", null, null, null));
        for (File file1 : files) {
            System.out.println(file1);
        }
        this.fileMapper.deleteFileById(file.getId());
    }
}