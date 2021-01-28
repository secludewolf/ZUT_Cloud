package com.ztu.cloud.cloud.common.dao.mysql;

import com.ztu.cloud.cloud.common.bean.mysql.Admin;
import com.ztu.cloud.cloud.common.bean.mysql.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jager
 * @description 用户信息数据库操作测试
 * @date 2020/06/21-13:07
 **/

@SpringBootTest
public class UserMapperTest {
    @Autowired
    UserMapper userMapper;


    @Test
    public void getUserById() {
        this.userMapper.insertUser(new User("test", "test1", "test1", "test1", "test1", "test1", 1, 1, System.currentTimeMillis(), System.currentTimeMillis()));
        User user = this.userMapper.getUserByAccount("test1");
        System.out.println(this.userMapper.getUserById(user.getId()));
        this.userMapper.deleteUserById(user.getId());
    }

    @Test
    public void getUserByAccount() {
        this.userMapper.insertUser(new User("test", "test2", "test1", "test1", "test1", "test1", 1, 1, System.currentTimeMillis(), System.currentTimeMillis()));
        User user = this.userMapper.getUserByAccount("test2");
        System.out.println(this.userMapper.getUserByAccount(user.getAccount()));
        this.userMapper.deleteUserById(user.getId());
    }

    @Test
    public void getUserByEmail() {
        this.userMapper.insertUser(new User("test", "test3", "test1", "test1", "test1", "test1", 1, 1, System.currentTimeMillis(), System.currentTimeMillis()));
        User user = this.userMapper.getUserByAccount("test3");
        System.out.println(this.userMapper.getUserByEmail(user.getEmail()));
        this.userMapper.deleteUserById(user.getId());
    }

    @Test
    public void getUserByPhone() {
        this.userMapper.insertUser(new User("test", "test4", "test1", "test1", "test1", "test1", 1, 1, System.currentTimeMillis(), System.currentTimeMillis()));
        User user = this.userMapper.getUserByAccount("test4");
        System.out.println(this.userMapper.getUserByPhone(user.getPhone()));
        this.userMapper.deleteUserById(user.getId());
    }

    @Test
    public void getUser() {
        // this.userMapper.insertUser(new User("test", "test4", "test1", "test1", "test1", "test1", 1, 1, System.currentTimeMillis(), System.currentTimeMillis()));
        // User user = this.userMapper.getUserByAccount("test4");
        // System.out.println(this.userMapper.getUser(0, 2));
        // this.userMapper.deleteUserById(user.getId());
    }

    @Test
    public void getUserCount() {
        this.userMapper.insertUser(new User("test", "test4", "test1", "test1", "test1", "test1", 1, 1, System.currentTimeMillis(), System.currentTimeMillis()));
        User user = this.userMapper.getUserByAccount("test4");
        System.out.println(this.userMapper.getUserCount());
        this.userMapper.deleteUserById(user.getId());
    }

    @Test
    public void insertUser() {
        User user = new User("test", "test5", "test4", "test4", "test4", "test4", 4, 4, System.currentTimeMillis(), System.currentTimeMillis());
        this.userMapper.insertUser(user);
        int id = this.userMapper.getUserByAccount("test5").getId();
        System.out.println(id);
        this.userMapper.deleteUserById(id);
    }

    @Test
    public void insertUsers() {
        List<User> users = new ArrayList<User>();
        List<Integer> ids = new ArrayList<>();
        users.add(new User("test", "test6", "test1", "test1", "test1", "test1", 1, 1, System.currentTimeMillis(), System.currentTimeMillis()));
        users.add(new User("test", "test7", "test2", "test2", "test2", "test2", 2, 2, System.currentTimeMillis(), System.currentTimeMillis()));
        this.userMapper.insertUsers(users);
        ids.add(this.userMapper.getUserByAccount(users.get(0).getAccount()).getId());
        ids.add(this.userMapper.getUserByAccount(users.get(1).getAccount()).getId());
        System.out.println(ids);
        this.userMapper.deleteUsersById(ids);
    }

    @Test
    public void deleteUser() {
        User user = new User("test", "test8", "test4", "test4", "test4", "test4", 4, 4, System.currentTimeMillis(), System.currentTimeMillis());
        this.userMapper.insertUser(user);
        int id = this.userMapper.getUserByAccount("test8").getId();
        System.out.println(id);
        this.userMapper.deleteUserById(id);
        System.out.println(this.userMapper.getUserById(id));
    }

    @Test
    public void deleteUsersById() {
        List<User> users = new ArrayList<User>();
        List<Integer> ids = new ArrayList<>();
        users.add(new User("test", "test9", "test1", "test1", "test1", "test1", 1, 1, System.currentTimeMillis(), System.currentTimeMillis()));
        users.add(new User("test", "test0", "test2", "test2", "test2", "test2", 2, 2, System.currentTimeMillis(), System.currentTimeMillis()));
        this.userMapper.insertUsers(users);
        ids.add(this.userMapper.getUserByAccount(users.get(0).getAccount()).getId());
        ids.add(this.userMapper.getUserByAccount(users.get(1).getAccount()).getId());
        System.out.println(this.userMapper.deleteUsersById(ids));
    }

    @Test
    public void updateUser() {
        this.userMapper.insertUser(new User("test", "test11", "test1", "test1", "test1", "test1", 1, 1, System.currentTimeMillis(), System.currentTimeMillis()));
        User user = this.userMapper.getUserByAccount("test11");
        System.out.println(user);
        user.setPassword("1234qwer");
        this.userMapper.updateUser(user);
        System.out.println(this.userMapper.getUserById(user.getId()));
        this.userMapper.deleteUserById(user.getId());
    }

    @Test
    public void updateUserPassword() {
        this.userMapper.insertUser(new User("test", "test12", "test1", "test1", "test1", "test1", 1, 1, System.currentTimeMillis(), System.currentTimeMillis()));
        User user = this.userMapper.getUserByAccount("test12");
        System.out.println(user);
        user.setPassword("1234qwer");
        this.userMapper.updateUserPassword(user.getId(), user.getPassword());
        System.out.println(this.userMapper.getUserById(user.getId()));
        this.userMapper.deleteUserById(user.getId());
    }

    @Test
    void getUsers() {
        this.userMapper.insertUser(new User("test", "test12", "test1", "test1", "test1", "test1", 1, 1, System.currentTimeMillis(), System.currentTimeMillis()));
        User user = this.userMapper.getUserByAccount("test12");
        List<User> users = this.userMapper.getUsers(new com.ztu.cloud.cloud.common.dto.condition.User("test", "test", "test", 1, null, null));
        for (User user1 : users) {
            System.out.println(user1);
        }
        this.userMapper.deleteUserById(user.getId());
    }
}
