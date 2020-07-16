package com.ztu.cloud.cloud.service.admin;

import com.ztu.cloud.cloud.common.bean.mongodb.UserRepository;
import com.ztu.cloud.cloud.common.bean.mysql.Admin;
import com.ztu.cloud.cloud.common.bean.mysql.User;
import com.ztu.cloud.cloud.common.dao.mongodb.UserRepositoryDao;
import com.ztu.cloud.cloud.common.dao.mysql.AdminMapper;
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
class AdminServiceImplTest {
    @Autowired
    UserMapper userMapper;
    @Autowired
    AdminMapper adminMapper;
    @Autowired
    UserRepositoryDao userRepositoryDao;
    @Autowired
    WebApplicationContext context;
    MockMvc mockMvc;
    Admin admin;
    String token;

    @BeforeEach
    void before() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        //创建测试账户
        this.admin = new Admin("lichen", "000000", "814878826@qq.com", "18600000000", "测试账号", 1);
        this.adminMapper.insertAdmin(admin);
        this.token = TokenUtil.createAdminToken(this.admin.getId(), false);
    }

    @AfterEach
    void after() {
        //删除测试账户
        this.adminMapper.deleteAdminById(this.admin.getId());
    }

    @Test
    void loginByToken() throws Exception {
        String url = "/admin/login/token";
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
    void loginByAccount() throws Exception {
        String url = "/admin/login/account";
        String request = new JSONObject()
                .put("account", this.admin.getAccount())
                .put("password", this.admin.getPassword())
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
        String url = "/admin/forget/email";
        String request = new JSONObject()
                .put("email", this.admin.getEmail())
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
    void registerByAccount() throws Exception {
        String url = "/admin/register/account";
        String request = new JSONObject()
                .put("name", "测试账号")
                .put("account", "testAccount")
                .put("email", "lichen7667@foxmail.com")
                .put("phone", "15800000000")
                .put("password", "123456")
                .put("key", "key")
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
        Admin admin = this.adminMapper.getAdminByPhone("15800000000");
        this.adminMapper.deleteAdminById(admin.getId());
        assert status == 200;
    }

    @Test
    void getAdminInfo() throws Exception {
        String url = "/admin/info/" + this.admin.getId();
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
    void changeAdminInfo() throws Exception {
        String url = "/admin/info";
        String request = new JSONObject()
                .put("id", this.admin.getId())
                .put("account", "testAccount")
                .put("email", "lichen7667@foxmail.com")
                .put("phone", "15900000000")
                .put("name", "修改用户名")
                .toString();
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.patch(url)
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
    void changeAdminPassword() throws Exception {
        String url = "/admin/password";
        String request = new JSONObject()
                .put("id", this.admin.getId())
                .put("oldPassword", this.admin.getPassword())
                .put("newPassword", "newPassword")
                .toString();
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.patch(url)
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
    void createUserManage() throws Exception {
        String url = "/admin/user";
        String request = new JSONObject()
                .put("name", "测试账号")
                .put("account", "testAccount")
                .put("email", "lichen7667@foxmail.com")
                .put("phone", null)
                .put("password", "123456")
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
        User user = this.userMapper.getUserByEmail("lichen7667@foxmail.com");
        this.userRepositoryDao.deleteById(user.getRepoId());
        this.userMapper.deleteUserById(user.getId());
        assert status == 200;
    }

    @Test
    void createAdminManage() throws Exception {
        String url = "/admin/admin";
        String request = new JSONObject()
                .put("name", "测试账号")
                .put("account", "testAccount")
                .put("email", "lichen7667@foxmail.com")
                .put("phone", "15800000000")
                .put("password", "123456")
                .put("key", "key")
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
        Admin admin = this.adminMapper.getAdminByEmail("lichen7667@foxmail.com");
        this.adminMapper.deleteAdminById(admin.getId());
        assert status == 200;
    }

    @Test
    void deleteUserManage() throws Exception {
        String url = "/admin/user";
        User user = new User("lichen", "000000", "814878826@qq.com", "测试账号");
        this.userMapper.insertUser(user);
        UserRepository userRepository = new UserRepository(user.getId());
        this.userRepositoryDao.insert(userRepository);
        user.setRepoId(userRepository.getId());
        this.userMapper.updateUser(user);
        String request = new JSONObject()
                .put("id", user.getId())
                .toString();
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.delete(url)
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
        user = this.userMapper.getUserByEmail("814878826@qq.com");
        this.userRepositoryDao.deleteById(user.getRepoId());
        this.userMapper.deleteUserById(user.getId());
        assert status == 200;
    }

    @Test
    void deleteAdminManage() throws Exception {
        String url = "/admin/admin";
        Admin admin = new Admin("lichen1", "000000", "8148788261@qq.com", "186000000001", "测试账号", 1);
        this.adminMapper.insertAdmin(admin);
        String request = new JSONObject()
                .put("id", admin.getId())
                .toString();
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.delete(url)
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
        admin = this.adminMapper.getAdminByEmail("8148788261@qq.com");
        this.adminMapper.deleteAdminById(admin.getId());
        assert status == 200;
    }

    @Test
    void getUserListManage() throws Exception {
        String url = "/admin/user/list/1";
        User user = new User("lichen", "000000", "814878826@qq.com", "测试账号");
        this.userMapper.insertUser(user);
        UserRepository userRepository = new UserRepository(user.getId());
        this.userRepositoryDao.insert(userRepository);
        user.setRepoId(userRepository.getId());
        this.userMapper.updateUser(user);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(url)
                .header(TokenUtil.TOKEN_HEADER, this.token)
                .accept(MediaType.APPLICATION_JSON))
                .andReturn();
        int status = mvcResult.getResponse().getStatus();
        String content = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
        System.out.println("url:" + url);
        System.out.println("status:" + status);
        System.out.println("content:" + content);
        user = this.userMapper.getUserByEmail("814878826@qq.com");
        this.userRepositoryDao.deleteById(user.getRepoId());
        this.userMapper.deleteUserById(user.getId());
        assert status == 200;
    }

    @Test
    void getAdminListManage() throws Exception {
        String url = "/admin/admin/list/1";
        Admin admin = new Admin("lichen1", "000000", "8148788261@qq.com", "186000000001", "测试账号", 1);
        this.adminMapper.insertAdmin(admin);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(url)
                .header(TokenUtil.TOKEN_HEADER, this.token)
                .accept(MediaType.APPLICATION_JSON))
                .andReturn();
        int status = mvcResult.getResponse().getStatus();
        String content = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
        System.out.println("url:" + url);
        System.out.println("status:" + status);
        System.out.println("content:" + content);
        admin = this.adminMapper.getAdminByEmail("8148788261@qq.com");
        this.adminMapper.deleteAdminById(admin.getId());
        assert status == 200;
    }

    @Test
    void getUserInfoManage() throws Exception {
        User user = new User("lichen", "000000", "814878826@qq.com", "测试账号");
        this.userMapper.insertUser(user);
        UserRepository userRepository = new UserRepository(user.getId());
        this.userRepositoryDao.insert(userRepository);
        user.setRepoId(userRepository.getId());
        this.userMapper.updateUser(user);
        String url = "/admin/user/" + user.getId() + "/info";
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(url)
                .header(TokenUtil.TOKEN_HEADER, this.token)
                .accept(MediaType.APPLICATION_JSON))
                .andReturn();
        int status = mvcResult.getResponse().getStatus();
        String content = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
        System.out.println("url:" + url);
        System.out.println("status:" + status);
        System.out.println("content:" + content);
        user = this.userMapper.getUserByEmail("814878826@qq.com");
        this.userRepositoryDao.deleteById(user.getRepoId());
        this.userMapper.deleteUserById(user.getId());
        assert status == 200;
    }

    @Test
    void getAdminInfoManage() throws Exception {
        Admin admin = new Admin("lichen1", "000000", "8148788261@qq.com", "186000000001", "测试账号", 1);
        this.adminMapper.insertAdmin(admin);
        String url = "/admin/admin/" + admin.getId() + "/info";
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(url)
                .header(TokenUtil.TOKEN_HEADER, this.token)
                .accept(MediaType.APPLICATION_JSON))
                .andReturn();
        int status = mvcResult.getResponse().getStatus();
        String content = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
        System.out.println("url:" + url);
        System.out.println("status:" + status);
        System.out.println("content:" + content);
        admin = this.adminMapper.getAdminByEmail("8148788261@qq.com");
        this.adminMapper.deleteAdminById(admin.getId());
        assert status == 200;
    }

    @Test
    void changeUserInfoManage() throws Exception {
        String url = "/admin/user";
        User user = new User("lichen", "000000", "814878826@qq.com", "测试账号");
        this.userMapper.insertUser(user);
        UserRepository userRepository = new UserRepository(user.getId());
        this.userRepositoryDao.insert(userRepository);
        user.setRepoId(userRepository.getId());
        this.userMapper.updateUser(user);
        System.out.println(user);
        String request = new JSONObject()
                .put("id", user.getId())
                .put("account", "123123")
                .put("email", "814878826@qq.com")
                .put("phone", "1234123")
                .put("name", "1234123")
                .put("password", "1234123142")
                .put("status", "1")
                .put("level", "1")
                .put("repoSize", 123)
                .toString();
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.patch(url)
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
        user = this.userMapper.getUserByEmail("814878826@qq.com");
        System.out.println(user);
        this.userRepositoryDao.deleteById(user.getRepoId());
        this.userMapper.deleteUserById(user.getId());
        assert status == 200;
    }

    @Test
    void changeAdminInfoManage() throws Exception {
        String url = "/admin/admin";
        Admin admin = new Admin("lichen1", "000000", "8148788261@qq.com", "186000000001", "测试账号", 1);
        this.adminMapper.insertAdmin(admin);
        String request = new JSONObject()
                .put("id", admin.getId())
                .put("account", "123123")
                .put("email", "8148788261@qq.com")
                .put("phone", "1234123")
                .put("name", "1234123")
                .put("password", "1234123142")
                .put("status", "1")
                .put("level", "1")
                .toString();
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.patch(url)
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
        admin = this.adminMapper.getAdminByEmail("8148788261@qq.com");
        this.adminMapper.deleteAdminById(admin.getId());
        assert status == 200;
    }
}