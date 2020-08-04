package com.ztu.cloud.cloud.service.admin;

import com.ztu.cloud.cloud.common.bean.mongodb.UserRepository;
import com.ztu.cloud.cloud.common.bean.mysql.Admin;
import com.ztu.cloud.cloud.common.bean.mysql.User;
import com.ztu.cloud.cloud.common.constant.ResultConstant;
import com.ztu.cloud.cloud.common.dao.mongodb.UserRepositoryDao;
import com.ztu.cloud.cloud.common.dao.mysql.AdminMapper;
import com.ztu.cloud.cloud.common.dao.mysql.UserMapper;
import com.ztu.cloud.cloud.common.dto.admin.*;
import com.ztu.cloud.cloud.common.dto.common.ChangePassword;
import com.ztu.cloud.cloud.common.dto.user.user.ChangeUserInfo;
import com.ztu.cloud.cloud.common.dto.common.ForgetEmail;
import com.ztu.cloud.cloud.common.dto.common.LoginAccount;
import com.ztu.cloud.cloud.common.vo.ResultResponseEntity;
import com.ztu.cloud.cloud.common.vo.admin.*;
import com.ztu.cloud.cloud.util.EmailUtil;
import com.ztu.cloud.cloud.util.RequestUtil;
import com.ztu.cloud.cloud.util.ResultUtil;
import com.ztu.cloud.cloud.util.TokenUtil;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Jager
 * @description 管理员信息管理业务实现
 * @date 2020/06/24-10:09
 **/
@Component
public class AdminServiceImpl implements AdminService {

	UserMapper userDao;
	AdminMapper adminDao;
	UserRepositoryDao userRepositoryDao;
	EmailUtil emailUtil;

	public AdminServiceImpl(UserMapper userDao, AdminMapper adminDao, UserRepositoryDao userRepositoryDao, EmailUtil emailUtil) {
		this.userDao = userDao;
		this.adminDao = adminDao;
		this.userRepositoryDao = userRepositoryDao;
		this.emailUtil = emailUtil;
	}

	/**
	 * 通过Token登陆,并获取管理员信息
	 *
	 * @param token 管理员Token
	 * @return 管理员信息以及更新后的Token
	 */
	@Override
	public ResultResponseEntity loginByToken(String token) {
		if (!TokenUtil.isAdmin(token)) {
			return ResultConstant.TOKEN_INVALID;
		}
		int id = TokenUtil.getId(token);
		Admin admin = this.adminDao.getAdminById(id);
		if (admin == null) {
			return ResultConstant.USER_NOT_FOUND;
		}
		if (admin.getStatus() != 1) {
			return ResultConstant.USER_STATUS_ABNORMAL;
		}
		return ResultUtil.createResultWithToken("登陆成功", new AdminLogin(admin), TokenUtil.refreshToken(token));
	}

	/**
	 * 通过账号,密码登陆账户
	 *
	 * @param data 请求数据
	 *             account 账号
	 *             password 密码
	 * @return 管理员信息以及更新后的Token
	 */
	@Override
	public ResultResponseEntity loginByAccount(String data) {
		LoginAccount loginAccount = RequestUtil.getLoginAccount(data);
		if (loginAccount == null) {
			return ResultConstant.REQUEST_PARAMETER_ERROR;
		}
		Admin admin = this.adminDao.getAdminByAccount(loginAccount.getAccount());
		if (admin == null) {
			return ResultConstant.USER_NOT_FOUND;
		}
		if (admin.getStatus() != 1) {
			return ResultConstant.USER_STATUS_ABNORMAL;
		}
		if (!admin.getPassword().equals(loginAccount.getPassword())) {
			return ResultConstant.WRONG_PASSWORD;
		}
		return ResultUtil.createResultWithToken("登陆成功", new AdminLogin(admin), TokenUtil.createAdminToken(admin.getId(), loginAccount.isRememberMe()));
	}

	/**
	 * 通过邮箱重置密码
	 *
	 * @param data 请求数据
	 *             email 邮箱
	 * @return 成功或失败
	 */
	@Override
	public ResultResponseEntity forgetEmail(String data) {
		ForgetEmail forgetEmail = RequestUtil.getForgetEmail(data);
		if (forgetEmail == null) {
			return ResultConstant.REQUEST_PARAMETER_ERROR;
		}
		Admin admin = this.adminDao.getAdminByEmail(forgetEmail.getEmail());
		if (admin == null) {
			return ResultConstant.USER_NOT_FOUND;
		}
		if (admin.getStatus() != 1) {
			return ResultConstant.USER_STATUS_ABNORMAL;
		}
		String password = (System.currentTimeMillis() + "").substring(3);
		if (this.adminDao.updateAdminPassword(admin.getId(), password) != 1) {
			return ResultConstant.SERVER_ERROR;
		}
		this.emailUtil.sendPasswordToEmail(admin.getEmail(), password);
		return ResultConstant.SUCCESS;
	}

	/**
	 * 通过账号,密码注册账号
	 *
	 * @param data 请求数据
	 *             name 用户名
	 *             account 账号
	 *             password 密码
	 *             email 邮箱
	 *             phone 手机
	 *             key 授权码
	 * @return 成功或失败
	 */
	@Override
	public ResultResponseEntity registerByAccount(String data) {
		RegisterAdmin registerAdmin = RequestUtil.getRegisterAdmin(data);
		if (registerAdmin == null) {
			return ResultConstant.REQUEST_PARAMETER_ERROR;
		}
		//授权码验证接口
		if (registerAdmin.getKey() == null) {
			return ResultConstant.INVALID_KEY;
		}
		//通过授权码获取对应权限等级
		int level = 1;
		if (registerAdmin.getPassword().length() < 6 || registerAdmin.getPassword().length() > 15) {
			return ResultConstant.PASSWORD_INVALID;
		}
		if (registerAdmin.getName().length() < 2
				|| registerAdmin.getName().length() > 16
				|| registerAdmin.getName().replaceAll("[\u4e00-\u9fa5]*[a-z]*[A-Z]*\\d*-*_*\\s*", "").length() != 0) {
			return ResultConstant.NAME_INVALID;
		}
		if (!registerAdmin.getEmail().matches("^[a-z0-9A-Z]+[- | a-z0-9A-Z . _]+@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-z]{2,3}$")) {
			return ResultConstant.EMAIL_INVALID;
		}
		if (this.adminDao.getAdminByAccount(registerAdmin.getAccount()) != null) {
			return ResultConstant.ACCOUNT_EXISTED;
		}
		if (this.adminDao.getAdminByEmail(registerAdmin.getEmail()) != null) {
			return ResultConstant.EMAIL_EXISTED;
		}
		if (this.adminDao.getAdminByPhone(registerAdmin.getPhone()) != null) {
			return ResultConstant.EMAIL_EXISTED;
		}
		Admin admin = new Admin(registerAdmin.getAccount(), registerAdmin.getPassword(), registerAdmin.getEmail(), registerAdmin.getPhone(), registerAdmin.getName(), level);
		this.adminDao.insertAdmin(admin);
		this.emailUtil.sendRegisterSuccess(admin.getEmail(), admin.getAccount());
		return ResultConstant.SUCCESS;
	}

	/**
	 * 获取管理员信息
	 *
	 * @param token   管理员Token
	 * @param adminId 管理员ID
	 * @return 管理员信息
	 */
	@Override
	public ResultResponseEntity getAdminInfo(String token, int adminId) {
		if (!TokenUtil.isAdmin(token)) {
			return ResultConstant.TOKEN_INVALID;
		}
		int id = TokenUtil.getId(token);
		Admin admin = this.adminDao.getAdminById(id);
		if (admin == null) {
			return ResultConstant.USER_NOT_FOUND;
		}
		if (admin.getStatus() != 1) {
			return ResultConstant.USER_STATUS_ABNORMAL;
		}
		admin = this.adminDao.getAdminById(adminId);
		if (admin == null) {
			return ResultConstant.TARGET_NOT_EXISTED;
		}
		return ResultUtil.createResult(1, "查询成功", new AdminInfo(admin));
	}

	/**
	 * 修改管理员信息
	 *
	 * @param token 管理员Token
	 * @param data  请求数据
	 *              id 管理员ID
	 *              account 账号
	 *              email 邮箱
	 *              phone 手机
	 *              name 用户名
	 * @return 管理员信息
	 */
	@Override
	public ResultResponseEntity changeAdminInfo(String token, String data) {
		if (!TokenUtil.isAdmin(token)) {
			return ResultConstant.TOKEN_INVALID;
		}
		int id = TokenUtil.getId(token);
		Admin admin = this.adminDao.getAdminById(id);
		if (admin == null) {
			return ResultConstant.USER_NOT_FOUND;
		}
		if (admin.getId() != id) {
			return ResultConstant.NO_ACCESS;
		}
		if (admin.getStatus() != 1) {
			return ResultConstant.USER_STATUS_ABNORMAL;
		}
		ChangeUserInfo changeUserInfo = RequestUtil.getChangeUserInfo(data);
		if (changeUserInfo == null) {
			return ResultConstant.REQUEST_PARAMETER_ERROR;
		}
		if (!admin.getName().equals(changeUserInfo.getName())
				&& (changeUserInfo.getName().length() < 2
				|| changeUserInfo.getName().length() > 16
				|| changeUserInfo.getName().replaceAll("[\u4e00-\u9fa5]*[a-z]*[A-Z]*\\d*-*_*\\s*", "").length() != 0)) {
			return ResultConstant.NAME_INVALID;
		}
		if (!admin.getAccount().equals(changeUserInfo.getAccount()) && this.adminDao.getAdminByAccount(changeUserInfo.getAccount()) != null) {
			return ResultConstant.ACCOUNT_EXISTED;
		}
		if (changeUserInfo.getEmail() != null && !changeUserInfo.getEmail().equals(admin.getEmail()) && !changeUserInfo.getEmail().matches("^[a-z0-9A-Z]+[- | a-z0-9A-Z . _]+@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-z]{2,}$")) {
			return ResultConstant.EMAIL_INVALID;
		}
		if (changeUserInfo.getEmail() != null && !changeUserInfo.getEmail().equals(admin.getEmail()) && this.adminDao.getAdminByEmail(changeUserInfo.getEmail()) != null) {
			return ResultConstant.EMAIL_EXISTED;
		}
		if (changeUserInfo.getPhone() != null && !changeUserInfo.getPhone().equals(admin.getPhone()) && this.adminDao.getAdminByPhone(changeUserInfo.getPhone()) != null) {
			return ResultConstant.PHONE_EXISTED;
		}
		admin.setName(changeUserInfo.getName());
		admin.setAccount(changeUserInfo.getAccount());
		admin.setEmail(changeUserInfo.getEmail());
		admin.setPhone(changeUserInfo.getPhone());
		admin.setChangeTime(System.currentTimeMillis());
		if (this.adminDao.updateAdmin(admin) != 1) {
			return ResultConstant.SERVER_ERROR;
		}
		admin = this.adminDao.getAdminById(admin.getId());
		return ResultUtil.createResult(1, "修改成功", new AdminInfo(admin));
	}

	/**
	 * 修改管理员密码
	 *
	 * @param token 管理员Token
	 * @param data  请求数据
	 *              id 管理员ID
	 *              oldPassword 原密码
	 *              newPassword 新密码
	 * @return 成功或失败
	 */
	@Override
	public ResultResponseEntity changeAdminPassword(String token, String data) {
		if (!TokenUtil.isAdmin(token)) {
			return ResultConstant.TOKEN_INVALID;
		}
		int id = TokenUtil.getId(token);
		Admin admin = this.adminDao.getAdminById(id);
		if (admin == null) {
			return ResultConstant.USER_NOT_FOUND;
		}
		if (admin.getId() != id) {
			return ResultConstant.NO_ACCESS;
		}
		if (admin.getStatus() != 1) {
			return ResultConstant.USER_STATUS_ABNORMAL;
		}
		ChangePassword changePassword = RequestUtil.getChangePassword(data);
		if (changePassword == null) {
			return ResultConstant.REQUEST_PARAMETER_ERROR;
		}
		if (!admin.getPassword().equals(changePassword.getOldPassword())) {
			return ResultConstant.WRONG_PASSWORD;
		}
		if (changePassword.getNewPassword().length() < 6 || changePassword.getNewPassword().length() > 15) {
			return ResultConstant.PASSWORD_INVALID;
		}
		if (this.adminDao.updateAdminPassword(admin.getId(), changePassword.getNewPassword()) != 1) {
			return ResultConstant.SERVER_ERROR;
		}
		return ResultUtil.createResult(1, "修改成功");
	}

	/**
	 * 创建用户
	 *
	 * @param token 管理员Token
	 * @param data  请求数据
	 *              name 用户名
	 *              account 账号
	 *              password 密码
	 *              email 邮箱
	 *              phone 手机
	 * @return 成功或失败
	 */
	@Override
	public ResultResponseEntity createUserManage(String token, String data) {
		if (!TokenUtil.isAdmin(token)) {
			return ResultConstant.TOKEN_INVALID;
		}
		CreateUser createUser = RequestUtil.getCreateUser(data);
		if (createUser == null) {
			return ResultConstant.REQUEST_PARAMETER_ERROR;
		}
		if (createUser.getPassword().length() < 6 || createUser.getPassword().length() > 15) {
			return ResultConstant.PASSWORD_INVALID;
		}
		if (createUser.getName().length() < 2
				|| createUser.getName().length() > 16
				|| createUser.getName().replaceAll("[\u4e00-\u9fa5]*[a-z]*[A-Z]*\\d*-*_*\\s*", "").length() != 0) {
			return ResultConstant.NAME_INVALID;
		}
		if (createUser.getEmail() != null && !createUser.getEmail().matches("^[a-z0-9A-Z]+[- | a-z0-9A-Z . _]+@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-z]{2,}$")) {
			return ResultConstant.EMAIL_INVALID;
		}
		int id = TokenUtil.getId(token);
		Admin admin = this.adminDao.getAdminById(id);
		if (admin == null) {
			return ResultConstant.USER_NOT_FOUND;
		}
		if (admin.getId() != id) {
			return ResultConstant.NO_ACCESS;
		}
		if (admin.getStatus() != 1) {
			return ResultConstant.USER_STATUS_ABNORMAL;
		}
		//权限验证
		if (admin.getLevel() <= 0) {
			return ResultConstant.NO_ACCESS;
		}
		if (this.userDao.getUserByAccount(createUser.getAccount()) != null) {
			return ResultConstant.ACCOUNT_EXISTED;
		}
		if (createUser.getEmail() != null && this.userDao.getUserByEmail(createUser.getEmail()) != null) {
			return ResultConstant.EMAIL_EXISTED;
		}
		if (createUser.getPhone() != null && this.userDao.getUserByPhone(createUser.getPhone()) != null) {
			return ResultConstant.PHONE_EXISTED;
		}
		User user = new User(createUser.getAccount(), createUser.getPassword(), createUser.getEmail(), createUser.getPhone(), createUser.getName());
		this.userDao.insertUser(user);
		UserRepository userRepository = new UserRepository(user.getId());
		this.userRepositoryDao.insert(userRepository);
		user.setRepoId(userRepository.getId());
		this.userDao.updateUser(user);
		return ResultConstant.SUCCESS;
	}

	/**
	 * 创建管理员
	 *
	 * @param token 管理员Token
	 * @param data  请求数据
	 *              name 用户名
	 *              account 账号
	 *              password 密码
	 *              email 邮箱
	 *              phone 手机
	 *              key 授权码
	 * @return 成功或失败
	 */
	@Override
	public ResultResponseEntity createAdminManage(String token, String data) {
		if (!TokenUtil.isAdmin(token)) {
			return ResultConstant.TOKEN_INVALID;
		}
		CreateAdmin createAdmin = RequestUtil.getCreateAdmin(data);
		if (createAdmin == null) {
			return ResultConstant.REQUEST_PARAMETER_ERROR;
		}
		if (createAdmin.getPassword().length() < 6 || createAdmin.getPassword().length() > 15) {
			return ResultConstant.PASSWORD_INVALID;
		}
		if (createAdmin.getName().length() < 2
				|| createAdmin.getName().length() > 16
				|| createAdmin.getName().replaceAll("[\u4e00-\u9fa5]*[a-z]*[A-Z]*\\d*-*_*\\s*", "").length() != 0) {
			return ResultConstant.NAME_INVALID;
		}
		if (!createAdmin.getEmail().matches("^[a-z0-9A-Z]+[- | a-z0-9A-Z . _]+@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-z]{2,}$")) {
			return ResultConstant.EMAIL_INVALID;
		}
		int id = TokenUtil.getId(token);
		Admin admin = this.adminDao.getAdminById(id);
		if (admin == null) {
			return ResultConstant.USER_NOT_FOUND;
		}
		if (admin.getId() != id) {
			return ResultConstant.NO_ACCESS;
		}
		if (admin.getStatus() != 1) {
			return ResultConstant.USER_STATUS_ABNORMAL;
		}
		//权限验证
		if (admin.getLevel() <= 0) {
			return ResultConstant.NO_ACCESS;
		}
		//TODO 授权码验证接口
		if (false) {
			return ResultConstant.INVALID_KEY;
		}
		//TODO 通过授权码获取对应权限等级
		int level = 1;
		if (this.adminDao.getAdminByAccount(createAdmin.getAccount()) != null) {
			return ResultConstant.ACCOUNT_EXISTED;
		}
		if (this.adminDao.getAdminByEmail(createAdmin.getEmail()) != null) {
			return ResultConstant.EMAIL_EXISTED;
		}
		if (this.adminDao.getAdminByPhone(createAdmin.getPhone()) != null) {
			return ResultConstant.PHONE_EXISTED;
		}
		admin = new Admin(createAdmin.getAccount(), createAdmin.getPassword(), createAdmin.getEmail(), createAdmin.getPhone(), createAdmin.getName(), level);
		this.adminDao.insertAdmin(admin);
		return ResultConstant.SUCCESS;
	}

	/**
	 * 删除用户
	 *
	 * @param token 管理员Token
	 * @param data  请求数据
	 *              id 用户ID
	 * @return 成功或失败
	 */
	@Override
	public ResultResponseEntity deleteUserManage(String token, String data) {
		if (!TokenUtil.isAdmin(token)) {
			return ResultConstant.TOKEN_INVALID;
		}
		DeleteUser deleteUser = RequestUtil.getDeleteUser(data);
		if (deleteUser == null) {
			return ResultConstant.REQUEST_PARAMETER_ERROR;
		}
		int id = TokenUtil.getId(token);
		Admin admin = this.adminDao.getAdminById(id);
		if (admin == null) {
			return ResultConstant.USER_NOT_FOUND;
		}
		if (admin.getId() != id) {
			return ResultConstant.NO_ACCESS;
		}
		if (admin.getStatus() != 1) {
			return ResultConstant.USER_STATUS_ABNORMAL;
		}
		//权限验证
		if (admin.getLevel() <= 0) {
			return ResultConstant.NO_ACCESS;
		}
		User user = null;
		if (deleteUser.getId() != 0) {
			user = this.userDao.getUserById(deleteUser.getId());
		}
		if (deleteUser.getAccount() != null) {
			user = this.userDao.getUserByAccount(deleteUser.getAccount());
		}
		if (deleteUser.getEmail() != null) {
			user = this.userDao.getUserByEmail(deleteUser.getEmail());
		}
		if (deleteUser.getPhone() != null) {
			user = this.userDao.getUserByPhone(deleteUser.getPhone());
		}
		if (user == null) {
			return ResultConstant.TARGET_NOT_EXISTED;
		}
		user.setStatus(-1);
		if (this.userDao.updateUser(user) != 1) {
			return ResultConstant.SERVER_ERROR;
		}
		this.userRepositoryDao.updateStatusById(user.getRepoId(), -1);
		return ResultConstant.SUCCESS;
	}

	/**
	 * 删除管理员
	 *
	 * @param token 管理员Token
	 * @param data  请求数据
	 *              id 管理员ID
	 * @return 成功或失败
	 */
	@Override
	public ResultResponseEntity deleteAdminManage(String token, String data) {
		if (!TokenUtil.isAdmin(token)) {
			return ResultConstant.TOKEN_INVALID;
		}
		DeleteAdmin deleteAdmin = RequestUtil.getDeleteAdmin(data);
		if (deleteAdmin == null) {
			return ResultConstant.REQUEST_PARAMETER_ERROR;
		}
		int id = TokenUtil.getId(token);
		Admin admin = this.adminDao.getAdminById(id);
		if (admin == null) {
			return ResultConstant.USER_NOT_FOUND;
		}
		if (admin.getId() != id) {
			return ResultConstant.NO_ACCESS;
		}
		if (admin.getStatus() != 1) {
			return ResultConstant.USER_STATUS_ABNORMAL;
		}
		//权限验证
		if (admin.getLevel() <= 0) {
			return ResultConstant.NO_ACCESS;
		}
		if (deleteAdmin.getId() != 0) {
			admin = this.adminDao.getAdminById(deleteAdmin.getId());
		}
		if (deleteAdmin.getAccount() != null) {
			admin = this.adminDao.getAdminByAccount(deleteAdmin.getAccount());
		}
		if (deleteAdmin.getEmail() != null) {
			admin = this.adminDao.getAdminByEmail(deleteAdmin.getEmail());
		}
		if (deleteAdmin.getPhone() != null) {
			admin = this.adminDao.getAdminByPhone(deleteAdmin.getPhone());
		}
		if (admin == null) {
			return ResultConstant.TARGET_NOT_EXISTED;
		}
		admin.setStatus(-1);
		if (this.adminDao.updateAdmin(admin) != 1) {
			return ResultConstant.SERVER_ERROR;
		}
		return ResultConstant.SUCCESS;
	}

	/**
	 * 获取用户列表
	 *
	 * @param token      管理员Token
	 * @param pageNumber 页数
	 * @return 用户列表
	 */
	@Override
	public ResultResponseEntity getUserListManage(String token, int pageNumber) {
		if (!TokenUtil.isAdmin(token)) {
			return ResultConstant.TOKEN_INVALID;
		}
		int id = TokenUtil.getId(token);
		Admin admin = this.adminDao.getAdminById(id);
		if (admin == null) {
			return ResultConstant.USER_NOT_FOUND;
		}
		if (admin.getId() != id) {
			return ResultConstant.NO_ACCESS;
		}
		if (admin.getStatus() != 1) {
			return ResultConstant.USER_STATUS_ABNORMAL;
		}
		//权限验证
		if (admin.getLevel() <= 0) {
			return ResultConstant.NO_ACCESS;
		}
		//TODO 获取仓库大小
		List<User> userList = this.userDao.getUser((pageNumber - 1) * 20, pageNumber * 20);
		return ResultUtil.createResult("", new UserList(this.userDao.getUserCount(), userList));
	}

	/**
	 * 获取管理员列表
	 *
	 * @param token      管理员Token
	 * @param pageNumber 页数
	 * @return 管理员列表
	 */
	@Override
	public ResultResponseEntity getAdminListManage(String token, int pageNumber) {
		if (!TokenUtil.isAdmin(token)) {
			return ResultConstant.TOKEN_INVALID;
		}
		int id = TokenUtil.getId(token);
		Admin admin = this.adminDao.getAdminById(id);
		if (admin == null) {
			return ResultConstant.USER_NOT_FOUND;
		}
		if (admin.getId() != id) {
			return ResultConstant.NO_ACCESS;
		}
		if (admin.getStatus() != 1) {
			return ResultConstant.USER_STATUS_ABNORMAL;
		}
		//权限验证
		if (admin.getLevel() <= 0) {
			return ResultConstant.NO_ACCESS;
		}
		List<Admin> adminList = this.adminDao.getAdmin((pageNumber - 1) * 20, pageNumber * 20);
		return ResultUtil.createResult("", new AdminList(this.adminDao.getAdminCount(), adminList));
	}

	/**
	 * 获取用户信息
	 *
	 * @param token  管理员Token
	 * @param userId 用户ID
	 * @return 用户信息
	 */
	@Override
	public ResultResponseEntity getUserInfoManage(String token, int userId) {
		if (!TokenUtil.isAdmin(token)) {
			return ResultConstant.TOKEN_INVALID;
		}
		int id = TokenUtil.getId(token);
		Admin admin = this.adminDao.getAdminById(id);
		if (admin == null) {
			return ResultConstant.USER_NOT_FOUND;
		}
		if (admin.getStatus() != 1) {
			return ResultConstant.USER_STATUS_ABNORMAL;
		}
		User user = this.userDao.getUserById(userId);
		if (user == null) {
			return ResultConstant.TARGET_NOT_EXISTED;
		}
		return ResultUtil.createResult(1, "查询成功", new UserInfo(user));
	}

	/**
	 * 获取管理员信息
	 *
	 * @param token   管理员Token
	 * @param adminId 管理员ID
	 * @return 成功或失败
	 */
	@Override
	public ResultResponseEntity getAdminInfoManage(String token, int adminId) {
		return getAdminInfo(token, adminId);
	}

	/**
	 * 修改用户信息
	 *
	 * @param token 管理员Token
	 * @param data  请求数据
	 *              id 用户ID
	 *              account 账号
	 *              email 邮箱
	 *              phone 手机
	 *              name 昵称
	 *              password 密码
	 *              status 状态
	 *              level 等级
	 *              repoSize 仓库大小
	 * @return 用户信息
	 */
	@Override
	public ResultResponseEntity changeUserInfoManage(String token, String data) {
		if (!TokenUtil.isAdmin(token)) {
			return ResultConstant.TOKEN_INVALID;
		}
		ChangeUserInfoManage changeUserInfoManager = RequestUtil.getChangeUserInfoManage(data);
		if (changeUserInfoManager == null) {
			return ResultConstant.REQUEST_PARAMETER_ERROR;
		}
		int id = TokenUtil.getId(token);
		Admin admin = this.adminDao.getAdminById(id);
		if (admin == null) {
			return ResultConstant.USER_NOT_FOUND;
		}
		if (admin.getId() != id) {
			return ResultConstant.NO_ACCESS;
		}
		if (admin.getStatus() != 1) {
			return ResultConstant.USER_STATUS_ABNORMAL;
		}
		// 权限验证
		if (admin.getLevel() <= 0) {
			return ResultConstant.NO_ACCESS;
		}
		User user = this.userDao.getUserById(changeUserInfoManager.getId());
		if (user == null) {
			return ResultConstant.TARGET_NOT_EXISTED;
		}
		if (!user.getPassword().equals(changeUserInfoManager.getPassword()) && changeUserInfoManager.getPassword().length() < 6 || changeUserInfoManager.getPassword().length() > 15) {
			return ResultConstant.PASSWORD_INVALID;
		}
		if (!user.getName().equals(changeUserInfoManager.getName())
				&& (changeUserInfoManager.getName().length() < 2
				|| changeUserInfoManager.getName().length() > 16
				|| changeUserInfoManager.getName().replaceAll("[\u4e00-\u9fa5]*[a-z]*[A-Z]*\\d*-*_*\\s*", "").length() != 0)) {
			return ResultConstant.NAME_INVALID;
		}
		if (!user.getAccount().equals(changeUserInfoManager.getAccount()) && this.adminDao.getAdminByAccount(changeUserInfoManager.getAccount()) != null) {
			return ResultConstant.ACCOUNT_EXISTED;
		}
		if (changeUserInfoManager.getEmail() != null && !changeUserInfoManager.getEmail().equals(user.getEmail()) && !changeUserInfoManager.getEmail().matches("^[a-z0-9A-Z]+[- | a-z0-9A-Z . _]+@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-z]{2,}$")) {
			return ResultConstant.EMAIL_INVALID;
		}
		if (changeUserInfoManager.getEmail() != null && !changeUserInfoManager.getEmail().equals(user.getEmail()) && this.adminDao.getAdminByEmail(changeUserInfoManager.getEmail()) != null) {
			return ResultConstant.EMAIL_EXISTED;
		}
		if (changeUserInfoManager.getPhone() != null && !changeUserInfoManager.getPhone().equals(user.getPhone()) && this.adminDao.getAdminByPhone(changeUserInfoManager.getPhone()) != null) {
			return ResultConstant.PHONE_EXISTED;
		}
		user.setName(changeUserInfoManager.getName());
		user.setAccount(changeUserInfoManager.getAccount());
		user.setEmail(changeUserInfoManager.getEmail());
		user.setPhone(changeUserInfoManager.getPhone());
		user.setPassword(changeUserInfoManager.getPassword());
		user.setStatus(changeUserInfoManager.getStatus());
		user.setLevel(changeUserInfoManager.getLevel());
		user.setChangeTime(System.currentTimeMillis());
		if (this.userDao.updateUser(user) != 1 || this.userRepositoryDao.updateRepoSizeById(user.getRepoId(), changeUserInfoManager.getRepoSize()) != 1) {
			return ResultConstant.SERVER_ERROR;
		}
		user = this.userDao.getUserById(user.getId());
		return ResultUtil.createResult(1, "修改成功", new UserInfo(user));
	}

	/**
	 * 修改管理员信息
	 *
	 * @param token 管理员Token
	 * @param data  请求数据
	 *              id 管理员ID
	 *              account 账号
	 *              email 邮箱
	 *              phone 手机
	 *              password 密码
	 *              name 昵称
	 *              status 状态
	 *              level 等级
	 * @return 用户信息
	 */
	@Override
	public ResultResponseEntity changeAdminInfoManage(String token, String data) {
		if (!TokenUtil.isAdmin(token)) {
			return ResultConstant.TOKEN_INVALID;
		}
		ChangeAdminInfoManage changeAdminInfoManager = RequestUtil.getChangeAdminInfoManage(data);
		if (changeAdminInfoManager == null) {
			return ResultConstant.REQUEST_PARAMETER_ERROR;
		}
		int id = TokenUtil.getId(token);
		Admin admin = this.adminDao.getAdminById(id);
		if (admin == null) {
			return ResultConstant.USER_NOT_FOUND;
		}
		if (admin.getId() != id) {
			return ResultConstant.NO_ACCESS;
		}
		if (admin.getStatus() != 1) {
			return ResultConstant.USER_STATUS_ABNORMAL;
		}
		// 权限验证
		if (admin.getLevel() <= 0) {
			return ResultConstant.NO_ACCESS;
		}
		admin = this.adminDao.getAdminById(changeAdminInfoManager.getId());
		if (admin == null) {
			return ResultConstant.TARGET_NOT_EXISTED;
		}
		if (!admin.getPassword().equals(changeAdminInfoManager.getPassword()) && changeAdminInfoManager.getPassword().length() < 6 || changeAdminInfoManager.getPassword().length() > 15) {
			return ResultConstant.PASSWORD_INVALID;
		}
		if (!admin.getName().equals(changeAdminInfoManager.getName())
				&& (changeAdminInfoManager.getName().length() < 2
				|| changeAdminInfoManager.getName().length() > 16
				|| changeAdminInfoManager.getName().replaceAll("[\u4e00-\u9fa5]*[a-z]*[A-Z]*\\d*-*_*\\s*", "").length() != 0)) {
			return ResultConstant.NAME_INVALID;
		}
		if (!admin.getAccount().equals(changeAdminInfoManager.getAccount()) && this.adminDao.getAdminByAccount(changeAdminInfoManager.getAccount()) != null) {
			return ResultConstant.ACCOUNT_EXISTED;
		}
		if (!changeAdminInfoManager.getEmail().equals(admin.getEmail()) && !changeAdminInfoManager.getEmail().matches("^[a-z0-9A-Z]+[- | a-z0-9A-Z . _]+@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-z]{2,}$")) {
			return ResultConstant.EMAIL_INVALID;
		}
		if (!changeAdminInfoManager.getEmail().equals(admin.getEmail()) && this.adminDao.getAdminByEmail(changeAdminInfoManager.getEmail()) != null) {
			return ResultConstant.EMAIL_EXISTED;
		}
		if (!changeAdminInfoManager.getPhone().equals(admin.getPhone()) && this.adminDao.getAdminByPhone(changeAdminInfoManager.getPhone()) != null) {
			return ResultConstant.PHONE_EXISTED;
		}
		admin.setName(changeAdminInfoManager.getName());
		admin.setAccount(changeAdminInfoManager.getAccount());
		admin.setEmail(changeAdminInfoManager.getEmail());
		admin.setPhone(changeAdminInfoManager.getPhone());
		admin.setPassword(changeAdminInfoManager.getPassword());
		admin.setStatus(changeAdminInfoManager.getStatus());
		admin.setLevel(changeAdminInfoManager.getLevel());
		admin.setChangeTime(System.currentTimeMillis());
		if (this.adminDao.updateAdmin(admin) != 1) {
			return ResultConstant.SERVER_ERROR;
		}
		admin = this.adminDao.getAdminById(admin.getId());
		return ResultUtil.createResult(1, "修改成功", new AdminInfo(admin));
	}
}
