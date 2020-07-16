package com.ztu.cloud.cloud.service.user;

import com.ztu.cloud.cloud.common.bean.mongodb.UserRepository;
import com.ztu.cloud.cloud.common.bean.mysql.User;
import com.ztu.cloud.cloud.common.dao.mongodb.UserRepositoryDao;
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
class UserServiceImplTest {

    @Autowired
    UserMapper userMapper;
    @Autowired
    UserRepositoryDao userRepositoryDao;
    @Autowired
    WebApplicationContext context;
    MockMvc mockMvc;
    User user;
    UserRepository userRepository;

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
    }

    @AfterEach
    void after() {
        //删除测试账户
        this.userMapper.deleteUserById(this.user.getId());
        this.userRepositoryDao.deleteById(this.userRepository.getId());
    }


    @Test
    void loginByToken() throws Exception {
        String url = "/login/token";
        String token = TokenUtil.createUserToken(this.user.getId(), false);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(url)
                .header(TokenUtil.TOKEN_HEADER, token)
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
    void loginByEmail() throws Exception {
        String url = "/login/email";
        String request = new JSONObject()
                .put("email", this.user.getEmail())
                .put("password", this.user.getPassword())
                .toString();
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(url)
                .content(request)
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
    void loginByAccount() throws Exception {
        String url = "/login/account";
        String request = new JSONObject()
                .put("account", this.user.getAccount())
                .put("password", this.user.getPassword())
                .toString();
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(url)
                .content(request)
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
    void forgetEmail() throws Exception {
        String url = "/forget/email";
        String request = new JSONObject()
                .put("email", this.user.getEmail())
                .toString();
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(url)
                .content(request)
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
    void registerByEmail() throws Exception {
        String url = "/register/email";
        String request = new JSONObject()
                .put("name", "测试账号")
                .put("account", "account")
                .put("email", "lichen7667@foxmail.com")
                .put("password", "123456")
                .toString();
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.put(url)
                .content(request)
                .accept(MediaType.APPLICATION_JSON))
                .andReturn();
        int status = mvcResult.getResponse().getStatus();
        String content = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
        System.out.println("url:" + url);
        System.out.println("request:" + request);
        System.out.println("status:" + status);
        System.out.println("content:" + content);
        User user = this.userMapper.getUserByEmail("lichen7667@foxmail.com");
        this.userMapper.deleteUserById(user.getId());
        this.userRepositoryDao.deleteById(user.getRepoId());
        assert status == 200;
    }

    @Test
    void registerByAccount() throws Exception {
        String url = "/register/account";
        String request = new JSONObject()
                .put("name", "测试账号")
                .put("account", "testAccount")
                .put("password", "123456")
                .toString();
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.put(url)
                .content(request)
                .accept(MediaType.APPLICATION_JSON))
                .andReturn();
        int status = mvcResult.getResponse().getStatus();
        String content = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
        System.out.println("url:" + url);
        System.out.println("request:" + request);
        System.out.println("status:" + status);
        System.out.println("content:" + content);
        User user = this.userMapper.getUserByAccount("testAccount");
        this.userMapper.deleteUserById(user.getId());
        this.userRepositoryDao.deleteById(user.getRepoId());
        assert status == 200;
    }

    @Test
    void getUserInfo() throws Exception {
        System.out.println(1);
        String url = "/user/info/" + this.user.getId();
        String token = TokenUtil.createUserToken(this.user.getId(), false);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(url)
                .header(TokenUtil.TOKEN_HEADER, token)
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
    void changeUserInfo() throws Exception {
        String url = "/user/info";
        String token = TokenUtil.createUserToken(this.user.getId(), false);
        String request = new JSONObject()
                .put("id", this.user.getId())
                .put("account", "testAccount")
                .put("email", "lichen7667@foxmail.com")
                .put("phone", null)
                .put("name", "修改用户名")
                .toString();
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.patch(url)
                .content(request)
                .header(TokenUtil.TOKEN_HEADER, token)
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
    void changeUserPassword() throws Exception {
        String url = "/user/password";
        String token = TokenUtil.createUserToken(this.user.getId(), false);
        String request = new JSONObject()
                .put("id", this.user.getId())
                .put("oldPassword", this.user.getPassword())
                .put("newPassword", "newPassword")
                .toString();
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.patch(url)
                .content(request)
                .header(TokenUtil.TOKEN_HEADER, token)
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
}