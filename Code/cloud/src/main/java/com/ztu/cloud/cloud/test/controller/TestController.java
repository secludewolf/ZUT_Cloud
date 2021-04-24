package com.ztu.cloud.cloud.test.controller;

import com.ztu.cloud.cloud.common.dao.mysql.UserMapper;
import com.ztu.cloud.cloud.common.vo.ResultResponseEntity;
import com.ztu.cloud.cloud.util.ResultUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Jager
 * @description test
 * @date 2020/8/13-15:49
 **/
@RestController
public class TestController {
    UserMapper userDao;

    public TestController(UserMapper userDao) {
        this.userDao = userDao;
    }

    @GetMapping("/test/page/{pageNumber}/{pageSize}/{sortKey}/{sortType}")
    public ResultResponseEntity test(@PathVariable Integer pageNumber,
                                     @PathVariable Integer pageSize,
                                     @PathVariable String sortKey,
                                     @PathVariable String sortType) {
//		PageHelper.startPage(pageNumber, pageSize, sortKey + " " + sortType);
//		List<User> result = this.userDao.getUsers();
//		PageInfo<User> pageInfo = new PageInfo<>(result);
//		return ResultUtil.createResult("分页测试", pageInfo);
        return ResultUtil.createResult("分页测试", null);
    }
}
