package com.ztu.cloud.cloud.util;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class EmailUtilTest {
	@Autowired
	EmailUtil emailUtil;

	@Test
	void sendPasswordToEmail() {
		this.emailUtil.sendPasswordToEmail("814878826@qq.com", "测试邮件");
	}
}