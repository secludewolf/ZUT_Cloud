package com.ztu.cloud.cloud.common.dao.mysql;

import com.ztu.cloud.cloud.common.bean.mysql.File;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

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
}