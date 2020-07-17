package com.ztu.cloud.cloud.service.user;

import com.ztu.cloud.cloud.common.bean.mongodb.ShareRepository;
import com.ztu.cloud.cloud.common.bean.mongodb.UserRepository;
import com.ztu.cloud.cloud.common.bean.mongodb.inside.File;
import com.ztu.cloud.cloud.common.bean.mongodb.inside.Folder;
import com.ztu.cloud.cloud.common.bean.mysql.Share;
import com.ztu.cloud.cloud.common.bean.mysql.User;
import com.ztu.cloud.cloud.common.bean.mysql.UserFile;
import com.ztu.cloud.cloud.common.dao.mongodb.ShareRepositoryDao;
import com.ztu.cloud.cloud.common.dao.mongodb.UserRepositoryDao;
import com.ztu.cloud.cloud.common.dao.mysql.FileMapper;
import com.ztu.cloud.cloud.common.dao.mysql.ShareMapper;
import com.ztu.cloud.cloud.common.dao.mysql.UserFileMapper;
import com.ztu.cloud.cloud.common.dao.mysql.UserMapper;
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
import java.util.LinkedList;
import java.util.List;

@SpringBootTest
class ShareManageServiceImplTest {

	@Autowired
	UserMapper userMapper;
	@Autowired
	ShareMapper shareMapper;
	@Autowired
	UserRepositoryDao userRepositoryDao;
	@Autowired
	ShareRepositoryDao shareRepositoryDao;
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

	//TODO 需要手动删除生成的数据
	@BeforeEach
	void before() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
		//创建测试账户
		this.user = new User("lichen", "000000", "814878826@qq.com", "测试账号");
		this.userMapper.insertUser(user);
		/*
		 * 仓库文件树
		 * │  file1
		 * │  file2
		 * │
		 * ├─folder1
		 * │  │  file3
		 * │  │
		 * │  └─folder3
		 * │          file5
		 * │
		 * └─folder2
		 *         file4
		 */
		this.fileMapper.insertFile(new com.ztu.cloud.cloud.common.bean.mysql.File("file1", "file1.txt", "/", 1, "txt"));
		this.fileMapper.insertFile(new com.ztu.cloud.cloud.common.bean.mysql.File("file2", "file2.txt", "/", 1, "txt"));
		this.fileMapper.insertFile(new com.ztu.cloud.cloud.common.bean.mysql.File("file3", "file3.txt", "/", 1, "txt"));
		this.fileMapper.insertFile(new com.ztu.cloud.cloud.common.bean.mysql.File("file4", "file4.txt", "/", 1, "txt"));
		this.fileMapper.insertFile(new com.ztu.cloud.cloud.common.bean.mysql.File("file5", "file5.txt", "/", 1, "txt"));
		UserFile userFile1 = new UserFile(user.getId(), "file1", "/root");
		UserFile userFile2 = new UserFile(user.getId(), "file2", "/root");
		UserFile userFile3 = new UserFile(user.getId(), "file3", "/root/folder1");
		UserFile userFile4 = new UserFile(user.getId(), "file4", "/root/folder2");
		UserFile userFile5 = new UserFile(user.getId(), "file5", "/root/folder1/folder3");
		this.userFileMapper.insertUserFile(userFile1);
		this.userFileMapper.insertUserFile(userFile2);
		this.userFileMapper.insertUserFile(userFile3);
		this.userFileMapper.insertUserFile(userFile4);
		this.userFileMapper.insertUserFile(userFile5);
		File file1 = new File("file1", userFile1.getId(), "file1.txt", "/root", "txt", 1);
		File file2 = new File("file2", userFile2.getId(), "file2.txt", "/root", "txt", 1);
		File file3 = new File("file3", userFile3.getId(), "file3.txt", "/root/folder1", "txt", 1);
		File file4 = new File("file4", userFile4.getId(), "file4.txt", "/root/folder2", "txt", 1);
		File file5 = new File("file5", userFile5.getId(), "file5.txt", "/root/folder1/folder3", "txt", 1);
		Folder folder1 = new Folder("folder1", "/root", 1);
		Folder folder2 = new Folder("folder2", "/root", 1);
		Folder folder3 = new Folder("folder3", "/root/folder1", 1);
		this.userRepository = new UserRepository(user.getId());
		this.userRepository.getFolder().setFiles(new HashMap<>());
		this.userRepository.getFolder().setFolders(new HashMap<>());
		this.userRepository.getFolder().getFiles().put(file1.getName(), file1);
		this.userRepository.getFolder().getFiles().put(file2.getName(), file2);
		this.userRepository.getFolder().getFolders().put(folder1.getName(), folder1);
		this.userRepository.getFolder().getFolders().put(folder2.getName(), folder2);
		folder1.setFiles(new HashMap<>());
		folder1.setFolders(new HashMap<>());
		folder2.setFiles(new HashMap<>());
		folder3.setFiles(new HashMap<>());
		folder1.getFiles().put(file3.getName(), file3);
		folder1.getFolders().put(folder3.getName(), folder3);
		folder2.getFiles().put(file4.getName(), file4);
		folder3.getFiles().put(file5.getName(), file5);
		this.userRepository.setUseSize(5);
		this.userRepositoryDao.insert(userRepository);
		this.user.setRepoId(userRepository.getId());
		this.userMapper.updateUser(user);
		this.token = TokenUtil.createUserToken(this.user.getId(), false);
	}

	@AfterEach
	void after() {
		//删除测试账户
		this.userMapper.deleteUserById(this.user.getId());
		this.userRepositoryDao.deleteById(this.userRepository.getId());
		this.fileMapper.deleteFileById("file1");
		this.fileMapper.deleteFileById("file2");
		this.fileMapper.deleteFileById("file3");
		this.fileMapper.deleteFileById("file4");
		this.fileMapper.deleteFileById("file5");
		this.userFileMapper.deleteUserFile(this.userRepository.getFolder().getFiles().get("file1.txt").getUserFileId());
		this.userFileMapper.deleteUserFile(this.userRepository.getFolder().getFiles().get("file2.txt").getUserFileId());
		this.userFileMapper.deleteUserFile(this.userRepository.getFolder().getFolders().get("folder1").getFiles().get("file3.txt").getUserFileId());
		this.userFileMapper.deleteUserFile(this.userRepository.getFolder().getFolders().get("folder2").getFiles().get("file4.txt").getUserFileId());
		this.userFileMapper.deleteUserFile(this.userRepository.getFolder().getFolders().get("folder1").getFolders().get("folder3").getFiles().get("file5.txt").getUserFileId());
	}

	@Test
	void createShare() throws Exception {
		String url = "/share";
		String request = new JSONObject()
				.put("repositoryId", this.userRepository.getId())
				.put("name", "测试分享")
				.put("path", "/root/folder1")
				.put("password", "123456")
				.put("validTime", System.currentTimeMillis() + 1000000000)
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
		List<Share> shares = this.shareMapper.getShareByUserId(this.user.getId());
		for (Share item : shares) {
			this.shareRepositoryDao.deleteByShareId(item.getId());
			this.shareMapper.deleteShareById(item.getId());
		}
		assert status == 200;
	}

	@Test
	void getShareList() throws Exception {
		String url = "/share/list";
		this.shareMapper.insertShare(new Share("test", this.user.getId(), "test", "test", null, 1, System.currentTimeMillis(), System.currentTimeMillis()));
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(url)
				.header(TokenUtil.TOKEN_HEADER, this.token)
				.accept(MediaType.APPLICATION_JSON))
				.andReturn();
		int status = mvcResult.getResponse().getStatus();
		String content = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
		System.out.println("url:" + url);
		System.out.println("status:" + status);
		System.out.println("content:" + content);
		List<Share> shares = this.shareMapper.getShareByUserId(this.user.getId());
		for (Share item : shares) {
			this.shareMapper.deleteShareById(item.getId());
		}
		assert status == 200;
	}

	@Test
	void deleteShare() throws Exception {
		String url = "/share/test";
		this.shareMapper.insertShare(new Share("test", this.user.getId(), "test", "test", null, 1, System.currentTimeMillis(), System.currentTimeMillis()));
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.delete(url)
				.header(TokenUtil.TOKEN_HEADER, this.token)
				.accept(MediaType.APPLICATION_JSON))
				.andReturn();
		int status = mvcResult.getResponse().getStatus();
		String content = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
		System.out.println("url:" + url);
		System.out.println("status:" + status);
		System.out.println("content:" + content);
		List<Share> shares = this.shareMapper.getShareByUserId(this.user.getId());
		for (Share item : shares) {
			this.shareMapper.deleteShareById(item.getId());
		}
		assert status == 200;
	}

	@Test
	void getShare() throws Exception {
		String url = "/share";
		String request = new JSONObject()
				.put("shareId", "test")
				.put("password", "123456")
				.toString();
		ShareRepository shareRepository = new ShareRepository("test", 1, new LinkedList<Long>(), new LinkedList<String>(), new HashMap<Integer, List<Long>>(), new HashMap<Integer, List<Long>>(), this.userRepository.getFolder());
		this.shareRepositoryDao.insert(shareRepository);
		this.shareMapper.insertShare(new Share("test", this.user.getId(), shareRepository.getId(), "test", "123456", 1, System.currentTimeMillis(), System.currentTimeMillis()));
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(url)
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
		List<Share> shares = this.shareMapper.getShareByUserId(this.user.getId());
		for (Share item : shares) {
			this.shareRepositoryDao.deleteByShareId(item.getId());
			this.shareMapper.deleteShareById(item.getId());
		}
		assert status == 200;
	}

	@Test
	void saveShare() throws Exception {
		String url = "/share/save";
		String request = new JSONObject()
				.put("shareId", "test")
				.put("password", "123456")
				.put("path", "/root")
				.toString();
		ShareRepository shareRepository = new ShareRepository("test", 1, new LinkedList<Long>(), new LinkedList<String>(), new HashMap<Integer, List<Long>>(), new HashMap<Integer, List<Long>>(), this.userRepository.getFolder());
		this.shareRepositoryDao.insert(shareRepository);
		this.shareMapper.insertShare(new Share("test", this.user.getId(), shareRepository.getId(), "test", "123456", 1, System.currentTimeMillis(), System.currentTimeMillis()));
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(url)
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
		// System.out.println(this.userRepositoryDao.getById(this.user.getRepoId()).getFolder().getFolders().get("root"));
		List<Share> shares = this.shareMapper.getShareByUserId(this.user.getId());
		//TODO 删除UserFile数据
		for (Share item : shares) {
			this.shareRepositoryDao.deleteByShareId(item.getId());
			this.shareMapper.deleteShareById(item.getId());
		}
		assert status == 200;
	}
}