package com.ztu.cloud.chat.controller;

import com.ztu.cloud.chat.common.vo.TestVo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Jager
 * @description 测试Controller
 * @date 2020/8/12-15:55
 **/
@RestController
public class TestController {
	@GetMapping("/test")
	public TestVo Test() {
		TestVo testVo = new TestVo();
		testVo.setUserId(123456);
		testVo.setUsername("UserName");
		testVo.setMessage("Message");
		testVo.setCount(100);
		return testVo;
	}
}
