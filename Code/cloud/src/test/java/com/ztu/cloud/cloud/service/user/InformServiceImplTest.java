package com.ztu.cloud.cloud.service.user;

import com.ztu.cloud.cloud.common.bean.mongodb.CommonUserInform;
import com.ztu.cloud.cloud.common.bean.mongodb.UserInform;
import com.ztu.cloud.cloud.common.bean.mongodb.UserRepository;
import com.ztu.cloud.cloud.common.bean.mysql.User;
import com.ztu.cloud.cloud.common.dao.mongodb.CommonUserInformDao;
import com.ztu.cloud.cloud.common.dao.mongodb.UserInformDao;
import com.ztu.cloud.cloud.common.dao.mongodb.UserRepositoryDao;
import com.ztu.cloud.cloud.common.dao.mysql.UserMapper;
import com.ztu.cloud.cloud.util.TokenUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
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
class InformServiceImplTest {
    @Autowired
    UserMapper userMapper;
    @Autowired
    UserRepositoryDao userRepositoryDao;
    @Autowired
    UserInformDao userInformDao;
    @Autowired
    CommonUserInformDao commonUserInformDao;
    @Autowired
    WebApplicationContext context;
    MockMvc mockMvc;
    User user;
    UserRepository userRepository;
    CommonUserInform commonUserInform;
    UserInform userInform;
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
        this.commonUserInform = new CommonUserInform("测试通知标题", "测试通知内容", System.currentTimeMillis() + 1000000000, 1);
        this.commonUserInformDao.insert(commonUserInform);
        this.userInform = new UserInform(this.user.getId(), new HashMap<>());
        this.userInformDao.insert(userInform);
        this.token = TokenUtil.createUserToken(this.user.getId(), false);
    }

    @AfterEach
    void after() {
        //删除测试账户
        this.userMapper.deleteUserById(this.user.getId());
        this.userRepositoryDao.deleteById(this.userRepository.getId());
        this.commonUserInformDao.deleteById(this.commonUserInform.getId());
        this.userInformDao.deleteById(this.userInform.getId());
    }

    @Test
    void getInformList() throws Exception {
        String url = "/inform";
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
        String url = "/inform/" + this.commonUserInform.getId();
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
        String url = "/inform/" + this.commonUserInform.getId() + "/" + "1";
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