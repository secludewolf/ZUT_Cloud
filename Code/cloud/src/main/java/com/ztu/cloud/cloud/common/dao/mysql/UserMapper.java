package com.ztu.cloud.cloud.common.dao.mysql;

import com.ztu.cloud.cloud.common.bean.mysql.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

/**
 * @author Jager
 * @description 用户信息操作
 * @date 2020/06/21-12:52
 **/
@Mapper()
@Component
public interface UserMapper {

    /**
     * 根据用户id查询用户信息
     *
     * @param id 用户id
     * @return 用户信息实体类
     */
    User getUserById(int id);

    /**
     * 根据用户账号查询用户信息
     *
     * @param account 用户账号
     * @return 用户信息实体类
     */
    User getUserByAccount(String account);

    /**
     * 根据用户邮箱查询用户信息
     *
     * @param email 用户邮箱
     * @return 用户信息实体类
     */
    User getUserByEmail(String email);

    /**
     * 根据用户手机号查询用户信息
     *
     * @param phone 用户手机号
     * @return 用户信息实体类
     */
    User getUserByPhone(String phone);

    /**
     * 获取用户列表
     *
     * @param condition 查找条件
     * @return 用户列表
     */
    List<User> getUsers(com.ztu.cloud.cloud.common.dto.condition.User condition);

    /**
     * 获取用户数量
     *
     * @return 用户数量
     */
    Long getUserCount();

    /**
     * 更新用户信息
     *
     * @param user 用户信息
     * @return 影响数量
     */
    int updateUser(User user);

    /**
     * 插入一个用户信息
     *
     * @param user 用户信息
     * @return 影响数量
     */
    int insertUser(User user);

    /**
     * 批量插入用户信息
     *
     * @param users 用户信息集合
     * @return 影响数量
     */
    int insertUsers(Collection<User> users);

    /**
     * 删除用户信息
     *
     * @param id 用户id
     * @return 影响数量
     */
    int deleteUserById(int id);

    /**
     * 批量删除用户信息
     *
     * @param ids 用户id列表
     * @return 影响数量
     */
    int deleteUsersById(List<Integer> ids);

    /**
     * 更新用户密码
     *
     * @param id       用户id
     * @param password 密码
     * @return 影响数量
     */
    int updateUserPassword(int id, String password);
}
