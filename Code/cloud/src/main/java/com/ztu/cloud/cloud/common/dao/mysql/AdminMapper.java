package com.ztu.cloud.cloud.common.dao.mysql;

import com.ztu.cloud.cloud.common.bean.mysql.Admin;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

/**
 * @author Jager
 * @description 管理员信息表操作
 * @date 2020/06/21-18:00
 **/
@Mapper
@Component
public interface AdminMapper {
	/**
	 * 根据用户id查询用户信息
	 *
	 * @param id 用户id
	 * @return 用户信息实体类
	 */
	Admin getAdminById(int id);

	/**
	 * 根据用户账号查询用户信息
	 *
	 * @param account 用户账号
	 * @return 用户信息实体类
	 */
	Admin getAdminByAccount(String account);

	/**
	 * 根据用户邮箱查询用户信息
	 *
	 * @param email 用户邮箱
	 * @return 用户信息实体类
	 */
	Admin getAdminByEmail(String email);

	/**
	 * 根据用户手机号查询用户信息
	 *
	 * @param phone 用户手机号
	 * @return 用户信息实体类
	 */
	Admin getAdminByPhone(String phone);

	/**
	 * 获取管理员列表
	 *
	 * @return 管理员列表
	 */
	List<Admin> getAdmins();

	/**
	 * 获取管理员数量
	 *
	 * @return 管理员数量
	 */
	Long getAdminCount();

	/**
	 * 更新用户信息
	 *
	 * @param admin 用户信息
	 * @return 影响数量
	 */
	int updateAdmin(Admin admin);

	/**
	 * 插入一个用户信息
	 *
	 * @param admin 用户信息
	 * @return 影响数量
	 */
	int insertAdmin(Admin admin);

	/**
	 * 批量插入用户信息
	 *
	 * @param admins 用户信息集合
	 * @return 影响数量
	 */
	int insertAdmins(Collection<Admin> admins);

	/**
	 * 删除用户信息
	 *
	 * @param id 用户id
	 * @return 影响数量
	 */
	int deleteAdminById(int id);

	/**
	 * 批量删除用户信息
	 *
	 * @param ids 用户id列表
	 * @return 影响数量
	 */
	int deleteAdminsById(List<Integer> ids);

	/**
	 * 更新用户密码
	 *
	 * @param id       用户id
	 * @param password 密码
	 * @return 影响数量
	 */
	int updateAdminPassword(int id, String password);
}
