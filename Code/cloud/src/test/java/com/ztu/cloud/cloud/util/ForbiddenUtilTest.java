package com.ztu.cloud.cloud.util;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Jager
 * @description
 * @date 2020/7/17-14:39
 **/
@SpringBootTest
class ForbiddenUtilTest {

	@Test
	void isFileNameValid() {
		String[] names = {"file123",
				"file/123",
				"file\\123",
				"file:123",
				"file*123",
				"file\"123",
				"file<123",
				"file>123",
				"file|123",
				"file?123"};
		for (String name : names) {
			System.out.println(name + ":" + ForbiddenUtil.isFileNameValid(name));
		}
	}
}