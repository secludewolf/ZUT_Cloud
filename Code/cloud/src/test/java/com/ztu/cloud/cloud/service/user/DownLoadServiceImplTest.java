package com.ztu.cloud.cloud.service.user;

import com.ztu.cloud.cloud.common.bean.mongodb.UserRepository;
import com.ztu.cloud.cloud.common.bean.mysql.User;
import com.ztu.cloud.cloud.common.bean.mysql.UserFile;
import com.ztu.cloud.cloud.common.dao.mongodb.UserRepositoryDao;
import com.ztu.cloud.cloud.common.dao.mysql.FileMapper;
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

@SpringBootTest
class DownLoadServiceImplTest {
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
    UserFile userFile;
    String token;

    /*
    此测试文件与上传测试文件相关
     */

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
        this.userFile = new UserFile(user.getId(), "a2f83d96f9f0cfda99af9567f3a5c4e0", "");
        this.userFileMapper.insertUserFile(userFile);
        this.token = TokenUtil.createUserToken(this.user.getId(), false);
    }

    @AfterEach
    void after() {
        this.userMapper.deleteUserById(this.user.getId());
        this.userRepositoryDao.deleteById(this.userRepository.getId());
        this.userFileMapper.deleteUserFile(this.userFile.getId());
    }

    @Test
    void getDownloadId() throws Exception {
        String url = "/download";
        String request = new JSONObject()
                .put("repositoryId", this.userRepository.getId())
                .put("userFileId", this.userFile.getId())
                .put("fileName", "textFile")
                .put("shareId", null)
                .put("folder", null)
                .toString();
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
        assert status == 200;
    }

    @Test
    void download() throws Exception {
        /*
         * 需要上面生成的下载ID
         */
        String url = "/download/" + "fdd26cec1b864f758550a5647674653b";
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
}