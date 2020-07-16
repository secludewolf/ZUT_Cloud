package com.ztu.cloud.cloud.common.vo.user;

import lombok.Data;

/**
 * @author Jager
 * @description 用户登录数据体
 * @date 2020/06/23-12:32
 **/
@Data
public class UserLogin {
    private User user;
    private UserRepository repository;

    public UserLogin() {
    }

    public UserLogin(com.ztu.cloud.cloud.common.bean.mysql.User user, com.ztu.cloud.cloud.common.bean.mongodb.UserRepository repository) {
        this.user = new User(user);
        this.repository = new UserRepository(repository);
    }
}
