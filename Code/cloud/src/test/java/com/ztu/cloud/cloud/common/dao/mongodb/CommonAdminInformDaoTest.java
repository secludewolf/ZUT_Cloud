package com.ztu.cloud.cloud.common.dao.mongodb;

import com.ztu.cloud.cloud.common.bean.mongodb.CommonAdminInform;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CommonAdminInformDaoTest {

	@Autowired
	CommonAdminInformDao commonAdminInformDao;

	@Test
	void getById() {
		CommonAdminInform inform = new CommonAdminInform("header", "content", System.currentTimeMillis(), System.currentTimeMillis() + 1000000000, 1, 1);
		this.commonAdminInformDao.insert(inform);
		System.out.println(this.commonAdminInformDao.getById(inform.getId()));
		this.commonAdminInformDao.deleteById(inform.getId());
	}

	@Test
	void getValidList() {
		CommonAdminInform inform = new CommonAdminInform("header", "content", System.currentTimeMillis() + 1000, System.currentTimeMillis() + 1000, 1, 1);
		CommonAdminInform inform2 = new CommonAdminInform("header2", "content2", System.currentTimeMillis() - 1000, System.currentTimeMillis() + 1000, 1, 1);
		CommonAdminInform inform3 = new CommonAdminInform("header3", "content3", System.currentTimeMillis(), System.currentTimeMillis() - 1000, 1, 1);
		this.commonAdminInformDao.insert(inform);
		this.commonAdminInformDao.insert(inform2);
		this.commonAdminInformDao.insert(inform3);
		System.out.println(this.commonAdminInformDao.getValidList());
		this.commonAdminInformDao.deleteById(inform.getId());
		this.commonAdminInformDao.deleteById(inform2.getId());
		this.commonAdminInformDao.deleteById(inform3.getId());
	}

	@Test
	void getValidListAfterTime() {
		CommonAdminInform inform = new CommonAdminInform("header", "content", System.currentTimeMillis() + 1000, System.currentTimeMillis() + 1000, 1, 1);
		CommonAdminInform inform2 = new CommonAdminInform("header2", "content2", System.currentTimeMillis() - 1000, System.currentTimeMillis() + 1000, 1, 1);
		CommonAdminInform inform3 = new CommonAdminInform("header3", "content3", System.currentTimeMillis(), System.currentTimeMillis() - 1000, 1, 1);
		this.commonAdminInformDao.insert(inform);
		this.commonAdminInformDao.insert(inform2);
		this.commonAdminInformDao.insert(inform3);
		System.out.println(this.commonAdminInformDao.getValidListAfterTime(System.currentTimeMillis()));
		this.commonAdminInformDao.deleteById(inform.getId());
		this.commonAdminInformDao.deleteById(inform2.getId());
		this.commonAdminInformDao.deleteById(inform3.getId());
	}

	@Test
	void getAll() {
		CommonAdminInform inform = new CommonAdminInform("header", "content", System.currentTimeMillis() + 1000, System.currentTimeMillis() + 1000, 1, 1);
		CommonAdminInform inform2 = new CommonAdminInform("header2", "content2", System.currentTimeMillis() - 1000, System.currentTimeMillis() + 1000, 1, 1);
		CommonAdminInform inform3 = new CommonAdminInform("header3", "content3", System.currentTimeMillis(), System.currentTimeMillis() - 1000, 1, 1);
		this.commonAdminInformDao.insert(inform);
		this.commonAdminInformDao.insert(inform2);
		this.commonAdminInformDao.insert(inform3);
		System.out.println(this.commonAdminInformDao.getAll());
		this.commonAdminInformDao.deleteById(inform.getId());
		this.commonAdminInformDao.deleteById(inform2.getId());
		this.commonAdminInformDao.deleteById(inform3.getId());
	}

	@Test
	void insert() {
		CommonAdminInform inform = new CommonAdminInform("header", "content", System.currentTimeMillis(), System.currentTimeMillis() + 1000000000, 1, 1);
		System.out.println(this.commonAdminInformDao.insert(inform));
		this.commonAdminInformDao.deleteById(inform.getId());
	}

	@Test
	void updateHeaderById() {
		CommonAdminInform inform = new CommonAdminInform("header", "content", System.currentTimeMillis(), System.currentTimeMillis() + 1000000000, 1, 1);
		this.commonAdminInformDao.insert(inform);
		inform.setHeader("header改");
		this.commonAdminInformDao.updateHeaderById(inform.getId(), inform.getHeader());
		System.out.println(this.commonAdminInformDao.getById(inform.getId()));
		this.commonAdminInformDao.deleteById(inform.getId());
	}

	@Test
	void updateContentById() {
		CommonAdminInform inform = new CommonAdminInform("header", "content", System.currentTimeMillis(), System.currentTimeMillis() + 1000000000, 1, 1);
		this.commonAdminInformDao.insert(inform);
		inform.setContent("content改");
		this.commonAdminInformDao.updateContentById(inform.getId(), inform.getContent());
		System.out.println(this.commonAdminInformDao.getById(inform.getId()));
		this.commonAdminInformDao.deleteById(inform.getId());
	}

	@Test
	void updateValidTimeById() {
		CommonAdminInform inform = new CommonAdminInform("header", "content", System.currentTimeMillis(), System.currentTimeMillis() + 1000000000, 1, 1);
		this.commonAdminInformDao.insert(inform);
		inform.setValidTime(0);
		this.commonAdminInformDao.updateValidTimeById(inform.getId(), inform.getValidTime());
		System.out.println(this.commonAdminInformDao.getById(inform.getId()));
		this.commonAdminInformDao.deleteById(inform.getId());
	}

	@Test
	void updateStatusById() {
		CommonAdminInform inform = new CommonAdminInform("header", "content", System.currentTimeMillis(), System.currentTimeMillis() + 1000000000, 1, 1);
		this.commonAdminInformDao.insert(inform);
		inform.setStatus(0);
		this.commonAdminInformDao.updateStatusById(inform.getId(), inform.getStatus());
		System.out.println(this.commonAdminInformDao.getById(inform.getId()));
		this.commonAdminInformDao.deleteById(inform.getId());
	}

	@Test
	void deleteById() {
		CommonAdminInform inform = new CommonAdminInform("header", "content", System.currentTimeMillis(), System.currentTimeMillis() + 1000000000, 1, 1);
		this.commonAdminInformDao.insert(inform);
		System.out.println(this.commonAdminInformDao.deleteById(inform.getId()));
	}
}