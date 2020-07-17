package com.ztu.cloud.cloud.service.admin;

import com.ztu.cloud.cloud.common.bean.mongodb.AdminInform;
import com.ztu.cloud.cloud.common.bean.mongodb.CommonAdminInform;
import com.ztu.cloud.cloud.common.bean.mysql.Admin;
import com.ztu.cloud.cloud.common.dao.mongodb.AdminInformDao;
import com.ztu.cloud.cloud.common.dao.mongodb.CommonAdminInformDao;
import com.ztu.cloud.cloud.common.dao.mysql.AdminMapper;
import com.ztu.cloud.cloud.common.dao.mysql.FileMapper;
import com.ztu.cloud.cloud.util.TokenUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;

@SpringBootTest
class InformManageServiceImplTest {
	@Autowired
	FileMapper fileMapper;
	@Autowired
	AdminMapper adminMapper;
	@Autowired
	AdminInformDao adminInformDao;
	@Autowired
	CommonAdminInformDao commonAdminInformDao;
	@Autowired
	WebApplicationContext context;
	MockMvc mockMvc;
	Admin admin;
	String token;
	CommonAdminInform commonAdminInform;
	AdminInform adminInform;

	@BeforeEach
	void before() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
		//创建测试账户
		this.admin = new Admin("lichen", "000000", "814878826@qq.com", "18600000000", "测试账号", 1);
		this.adminMapper.insertAdmin(admin);
		this.token = TokenUtil.createAdminToken(this.admin.getId(), false);
		this.commonAdminInform = new CommonAdminInform("测试通知标题", "测试通知内容", System.currentTimeMillis() + 1000000000, 1);
		this.commonAdminInformDao.insert(commonAdminInform);
		this.adminInform = new AdminInform(this.admin.getId(), new HashMap<>());
		this.adminInformDao.insert(adminInform);
	}

	@AfterEach
	void after() {
		//删除测试账户
		this.adminMapper.deleteAdminById(this.admin.getId());
		this.commonAdminInformDao.deleteById(this.commonAdminInform.getId());
		this.adminInformDao.deleteById(this.adminInform.getId());
	}

	@Test
	void createUserInform() throws Exception {
		//TODO 需要手动删除生成的数据
		String url = "/admin/inform/user";
		String request = new JSONObject()
				.put("header", "测试标题")
				.put("content", "测试内容")
				.put("validTime", System.currentTimeMillis())
				.toString();
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.put(url)
				.content(request)
				.header(TokenUtil.TOKEN_HEADER, this.token)
				.accept(MediaType.APPLICATION_JSON))
				.andReturn();
		int status = mvcResult.getResponse().getStatus();
		String content = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
		System.out.println("url:" + url);
		System.out.println("request:" + request);
		System.out.println("status:" + status);
		System.out.println("content:" + content);
		assert status == 200;
	}

	@Test
	void createAdminInform() throws Exception {
		//TODO 需要手动删除生成的数据
		String url = "/admin/inform/admin";
		String request = new JSONObject()
				.put("header", "测试标题")
				.put("content", "测试内容")
				.put("validTime", System.currentTimeMillis())
				.toString();
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.put(url)
				.content(request)
				.header(TokenUtil.TOKEN_HEADER, this.token)
				.accept(MediaType.APPLICATION_JSON))
				.andReturn();
		int status = mvcResult.getResponse().getStatus();
		String content = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
		System.out.println("url:" + url);
		System.out.println("request:" + request);
		System.out.println("status:" + status);
		System.out.println("content:" + content);
		assert status == 200;
	}

	@Test
	void getInformList() throws Exception {
		String url = "/admin/inform";
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(url)
				.header(TokenUtil.TOKEN_HEADER, this.token)
				.accept(MediaType.APPLICATION_JSON))
				.andReturn();
		int status = mvcResult.getResponse().getStatus();
		String content = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
		System.out.println("url:" + url);
		System.out.println("status:" + status);
		System.out.println("content:" + content);
		assert status == 200;
	}

	@Test
	void getInform() throws Exception {
		String url = "/admin/inform/" + this.commonAdminInform.getId();
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(url)
				.header(TokenUtil.TOKEN_HEADER, this.token)
				.accept(MediaType.APPLICATION_JSON))
				.andReturn();
		int status = mvcResult.getResponse().getStatus();
		String content = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
		System.out.println("url:" + url);
		System.out.println("status:" + status);
		System.out.println("content:" + content);
		assert status == 200;
	}

	@Test
	void changeInformStatus() throws Exception {
		String url = "/admin/inform/" + this.commonAdminInform.getId() + "/" + "1";
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.patch(url)
				.header(TokenUtil.TOKEN_HEADER, this.token)
				.accept(MediaType.APPLICATION_JSON))
				.andReturn();
		int status = mvcResult.getResponse().getStatus();
		String content = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
		System.out.println("url:" + url);
		System.out.println("status:" + status);
		System.out.println("content:" + content);
		assert status == 200;
	}
}