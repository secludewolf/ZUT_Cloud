package com.ztu.cloud.cloud.service.admin;

import com.ztu.cloud.cloud.common.bean.mysql.Admin;
import com.ztu.cloud.cloud.common.dao.mongodb.UserRepositoryDao;
import com.ztu.cloud.cloud.common.dao.mysql.AdminMapper;
import com.ztu.cloud.cloud.common.dao.mysql.FileMapper;
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

@SpringBootTest
class FileServiceImplTest {
    @Autowired
    FileMapper fileMapper;
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
        //TODO 需要上传文件生成的测试文件
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
    void download() throws Exception {
        String url = "/admin/download/a2f83d96f9f0cfda99af9567f3a5c4e0";
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
    void getFileList() throws Exception {
        String url = "/admin/file/list/1";
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
    void getFileInfo() throws Exception {
        String url = "/admin/file/a2f83d96f9f0cfda99af9567f3a5c4e0/info";
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
    void deleteFile() throws Exception {
        String url = "/admin/file/a2f83d96f9f0cfda99af9567f3a5c4e0";
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.delete(url)
                .header(TokenUtil.TOKEN_HEADER, this.token)
                .accept(MediaType.APPLICATION_JSON))
                .andReturn();
        int status = mvcResult.getResponse().getStatus();
        String content = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
        System.out.println("url:" + url);
        System.out.println("status:" + status);
        System.out.println("content:" + content);
        //TODO 将文件状态修改回来
        assert status == 200;
    }
}