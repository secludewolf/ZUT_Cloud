package com.ztu.cloud.cloud.service.user;

import com.ztu.cloud.cloud.common.bean.mongodb.UserRepository;
import com.ztu.cloud.cloud.common.bean.mysql.User;
import com.ztu.cloud.cloud.common.dao.mongodb.UserRepositoryDao;
import com.ztu.cloud.cloud.common.dao.mysql.FileMapper;
import com.ztu.cloud.cloud.common.dao.mysql.UserFileMapper;
import com.ztu.cloud.cloud.common.dao.mysql.UserMapper;
import com.ztu.cloud.cloud.util.Md5Util;
import com.ztu.cloud.cloud.util.TokenUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.File;
import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

@SpringBootTest
class UploadServiceImplTest {
	@Autowired
	UserMapper userMapper;
	@Autowired
	UserRepositoryDao userRepositoryDao;
	@Autowired
	UserFileMapper userFileMapper;
	@Autowired
	FileMapper fileMapper;
	@Autowired
	WebApplicationContext context;
	MockMvc mockMvc;
	User user;
	UserRepository userRepository;
	String token;

	@BeforeEach
	void before() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
		//创建测试账户
		this.user = new User("lichen", "000000", "814878826@qq.com", "测试账号");
		this.userMapper.insertUser(user);
		this.userRepository = new UserRepository(user.getId());
		this.userRepositoryDao.insert(userRepository);
		this.user.setRepoId(userRepository.getId());
		this.userMapper.updateUser(user);
		this.token = TokenUtil.createUserToken(this.user.getId(), false);
	}

	@AfterEach
	void after() {
		this.userMapper.deleteUserById(this.user.getId());
		this.userRepositoryDao.deleteById(this.userRepository.getId());
	}

	@Test
	void uploadSmallFile() throws Exception {
		String url = "/upload";
		File file = new File("./TestFolder/测试小文件.txt");
		String fileMd5 = Md5Util.getMd5(file);
		FileInputStream inputStream = new FileInputStream(file);
		byte[] bytes = new byte[5 * 1024 * 1024];
		int read = inputStream.read(bytes);
		byte[] data = Arrays.copyOf(bytes, read);
		MockMultipartFile multiFile = new MockMultipartFile("block", "测试小文件.txt",
				MediaType.MULTIPART_FORM_DATA_VALUE, data);
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.multipart(url)
				.file(multiFile)
				.param("fileName", "测试小文件.txt")
				.param("blockMd5", fileMd5)
				.param("fileMd5", fileMd5)
				.param("index", "-1")
				.param("length", "1")
				.header(TokenUtil.TOKEN_HEADER, this.token)
				.accept(MediaType.APPLICATION_JSON))
				.andReturn();
		int status = mvcResult.getResponse().getStatus();
		String content = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
		System.out.println("url:" + url);
		System.out.println("status:" + status);
		System.out.println("content:" + content);
		inputStream.close();
		//TODO 需要手动删除生成的数据库记录和文件
	}

	@Test
	void uploadBigFile() throws Exception {
		String url = "/upload";
		File file = new File("./TestFolder/测试大文件.mp4");
		String fileMd5 = Md5Util.getMd5(file);
		System.out.println(fileMd5);
		long length = file.length() / (5 * 1024 * 1024) + 1;
		int index = 0;
		FileInputStream inputStream = new FileInputStream(file);
		byte[] bytes = new byte[5 * 1024 * 1024];
		int read = inputStream.read(bytes);
		while (read != -1) {
			byte[] data = Arrays.copyOf(bytes, read);
			System.out.println(Md5Util.getMd5(data));
			MockMultipartFile multiFile = new MockMultipartFile("block", "测试大文件.txt",
					MediaType.MULTIPART_FORM_DATA_VALUE, data);
			MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.multipart(url)
					.file(multiFile)
					.param("fileName", "测试大文件.mp4")
					.param("blockMd5", Md5Util.getMd5(data))
					.param("fileMd5", fileMd5)
					.param("index", index + "")
					.param("length", length + "")
					.header(TokenUtil.TOKEN_HEADER, this.token)
					.accept(MediaType.APPLICATION_JSON))
					.andReturn();
			int status = mvcResult.getResponse().getStatus();
			String content = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
			System.out.println("url:" + url);
			System.out.println("status:" + status);
			System.out.println("content:" + content);
			read = inputStream.read(bytes);
			index++;
		}
		inputStream.close();
		//TODO 需要手动删除生成的数据库记录和文件
	}
}