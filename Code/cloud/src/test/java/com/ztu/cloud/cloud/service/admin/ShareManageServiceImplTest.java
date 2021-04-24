package com.ztu.cloud.cloud.service.admin;

import com.ztu.cloud.cloud.common.bean.mongodb.ShareRepository;
import com.ztu.cloud.cloud.common.bean.mongodb.inside.Folder;
import com.ztu.cloud.cloud.common.bean.mysql.Admin;
import com.ztu.cloud.cloud.common.bean.mysql.Share;
import com.ztu.cloud.cloud.common.dao.mongodb.ShareRepositoryDao;
import com.ztu.cloud.cloud.common.dao.mongodb.UserRepositoryDao;
import com.ztu.cloud.cloud.common.dao.mysql.*;
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
import java.util.LinkedList;
import java.util.List;

@SpringBootTest
class ShareManageServiceImplTest {
    @Autowired
    FileMapper fileMapper;
    @Autowired
    AdminMapper adminMapper;
    @Autowired
    UserRepositoryDao userRepositoryDao;
    @Autowired
    UserMapper userMapper;
    @Autowired
    ShareMapper shareMapper;
    @Autowired
    ShareRepositoryDao shareRepositoryDao;
    @Autowired
    UserFileMapper userFileMapper;
    @Autowired
    WebApplicationContext context;
    MockMvc mockMvc;
    Admin admin;
    String token;

    @BeforeEach
    void before() {
        //需要上传文件生成的测试文件
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
    void getShareList() throws Exception {
        String url = "/admin/share/list/1";
        ShareRepository shareRepository = new ShareRepository("test", 1, new LinkedList<Long>(), new LinkedList<String>(), new HashMap<Integer, List<Long>>(), new HashMap<Integer, List<Long>>(), new Folder());
        this.shareRepositoryDao.insert(shareRepository);
        this.shareMapper.insertShare(new Share("test", 1, shareRepository.getId(), "test", "123456", 1, System.currentTimeMillis(), System.currentTimeMillis()));
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(url)
                .header(TokenUtil.TOKEN_HEADER, this.token)
                .accept(MediaType.APPLICATION_JSON))
                .andReturn();
        int status = mvcResult.getResponse().getStatus();
        String content = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
        System.out.println("url:" + url);
        System.out.println("status:" + status);
        System.out.println("content:" + content);
        List<Share> shares = this.shareMapper.getShareByUserId(1);
        for (Share item : shares) {
            this.shareRepositoryDao.deleteByShareId(item.getId());
            this.shareMapper.deleteShareById(item.getId());
        }
        assert status == 200;
    }

    @Test
    void getShare() throws Exception {
        String url = "/admin/share/test";
        ShareRepository shareRepository = new ShareRepository("test", 1, new LinkedList<Long>(), new LinkedList<String>(), new HashMap<Integer, List<Long>>(), new HashMap<Integer, List<Long>>(), new Folder());
        this.shareRepositoryDao.insert(shareRepository);
        this.shareMapper.insertShare(new Share("test", 1, shareRepository.getId(), "test", "123456", 1, System.currentTimeMillis(), System.currentTimeMillis()));
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(url)
                .header(TokenUtil.TOKEN_HEADER, this.token)
                .accept(MediaType.APPLICATION_JSON))
                .andReturn();
        int status = mvcResult.getResponse().getStatus();
        String content = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
        System.out.println("url:" + url);
        System.out.println("status:" + status);
        System.out.println("content:" + content);
        List<Share> shares = this.shareMapper.getShareByUserId(1);
        for (Share item : shares) {
            this.shareRepositoryDao.deleteByShareId(item.getId());
            this.shareMapper.deleteShareById(item.getId());
        }
        assert status == 200;
    }

    @Test
    void deleteShare() throws Exception {
        String url = "/admin/share/test";
        ShareRepository shareRepository = new ShareRepository("test", 1, new LinkedList<Long>(), new LinkedList<String>(), new HashMap<Integer, List<Long>>(), new HashMap<Integer, List<Long>>(), new Folder());
        this.shareRepositoryDao.insert(shareRepository);
        this.shareMapper.insertShare(new Share("test", 1, shareRepository.getId(), "test", "123456", 1, System.currentTimeMillis(), System.currentTimeMillis()));
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.delete(url)
                .header(TokenUtil.TOKEN_HEADER, this.token)
                .accept(MediaType.APPLICATION_JSON))
                .andReturn();
        int status = mvcResult.getResponse().getStatus();
        String content = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
        System.out.println("url:" + url);
        System.out.println("status:" + status);
        System.out.println("content:" + content);
        List<Share> shares = this.shareMapper.getShareByUserId(1);
        for (Share item : shares) {
            this.shareRepositoryDao.deleteByShareId(item.getId());
            this.shareMapper.deleteShareById(item.getId());
        }
        assert status == 200;
    }
}