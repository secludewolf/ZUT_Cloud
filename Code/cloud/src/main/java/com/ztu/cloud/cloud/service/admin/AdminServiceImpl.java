package com.ztu.cloud.cloud.service.admin;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ztu.cloud.cloud.common.bean.mongodb.UserRepository;
import com.ztu.cloud.cloud.common.bean.mysql.Admin;
import com.ztu.cloud.cloud.common.bean.mysql.User;
import com.ztu.cloud.cloud.common.constant.ResultConstant;
import com.ztu.cloud.cloud.common.dao.mongodb.UserRepositoryDao;
import com.ztu.cloud.cloud.common.dao.mysql.AdminMapper;
import com.ztu.cloud.cloud.common.dao.mysql.UserMapper;
import com.ztu.cloud.cloud.common.dto.admin.*;
import com.ztu.cloud.cloud.common.dto.common.ChangePassword;
import com.ztu.cloud.cloud.common.dto.common.ForgetEmail;
import com.ztu.cloud.cloud.common.dto.common.LoginAccount;
import com.ztu.cloud.cloud.common.vo.ResultResponseEntity;
import com.ztu.cloud.cloud.common.vo.admin.AdminInfo;
import com.ztu.cloud.cloud.common.vo.admin.AdminLogin;
import com.ztu.cloud.cloud.common.vo.admin.UserInfo;
import com.ztu.cloud.cloud.common.vo.common.PageVo;
import com.ztu.cloud.cloud.util.EmailUtil;
import com.ztu.cloud.cloud.util.ResultUtil;
import com.ztu.cloud.cloud.util.TokenUtil;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
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

    public AdminServiceImpl(UserMapper userDao, AdminMapper adminDao, UserRepositoryDao userRepositoryDao,
                            EmailUtil emailUtil) {
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
     * @param parameter 请求数据 {account 账号 password 密码}
     * @return 管理员信息以及更新后的Token
     */
    @Override
    public ResultResponseEntity loginByAccount(LoginAccount parameter) {
        Admin admin = this.adminDao.getAdminByAccount(parameter.getAccount());
        if (admin == null) {
            return ResultConstant.USER_NOT_FOUND;
        }
        if (admin.getStatus() != 1) {
            return ResultConstant.USER_STATUS_ABNORMAL;
        }
        if (!admin.getPassword().equals(parameter.getPassword())) {
            return ResultConstant.WRONG_PASSWORD;
        }
        return ResultUtil.createResultWithToken("登陆成功", new AdminLogin(admin),
                TokenUtil.createAdminToken(admin.getId(), parameter.isRememberMe()));
    }

    /**
     * 通过邮箱重置密码
     *
     * @param parameter 请求数据 {email 邮箱}
     * @return 成功或失败
     */
    @Override
    public ResultResponseEntity forgetEmail(ForgetEmail parameter) {
        Admin admin = this.adminDao.getAdminByEmail(parameter.getEmail());
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
     * @param parameter 请求数据 name 用户名 account 账号 password 密码 email 邮箱 phone 手机 key 授权码
     * @return 成功或失败
     */
    @Override
    public ResultResponseEntity registerByAccount(RegisterAdmin parameter) {
        // 授权码验证接口
        if (parameter.getKey() == null) {
            return ResultConstant.INVALID_KEY;
        }
        // 通过授权码获取对应权限等级
        int level = 1;
        if (this.adminDao.getAdminByAccount(parameter.getAccount()) != null) {
            return ResultConstant.ACCOUNT_EXISTED;
        }
        if (this.adminDao.getAdminByEmail(parameter.getEmail()) != null) {
            return ResultConstant.EMAIL_EXISTED;
        }
        if (this.adminDao.getAdminByPhone(parameter.getPhone()) != null) {
            return ResultConstant.EMAIL_EXISTED;
        }
        Admin admin = new Admin(parameter.getAccount(), parameter.getPassword(), parameter.getEmail(),
                parameter.getPhone(), parameter.getName(), level);
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
     * @param token     管理员Token
     * @param parameter 请求数据 id 管理员ID account 账号 email 邮箱 phone 手机 name 用户名
     * @return 管理员信息
     */
    @Override
    public ResultResponseEntity changeAdminInfo(String token, ChangeAdminInfo parameter) {
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
        if (!admin.getAccount().equals(parameter.getAccount())
                && this.adminDao.getAdminByAccount(parameter.getAccount()) != null) {
            return ResultConstant.ACCOUNT_EXISTED;
        }
        if (parameter.getEmail() != null && !parameter.getEmail().equals(admin.getEmail())
                && this.adminDao.getAdminByEmail(parameter.getEmail()) != null) {
            return ResultConstant.EMAIL_EXISTED;
        }
        if (parameter.getPhone() != null && !parameter.getPhone().equals(admin.getPhone())
                && this.adminDao.getAdminByPhone(parameter.getPhone()) != null) {
            return ResultConstant.PHONE_EXISTED;
        }
        admin.setName(parameter.getName());
        admin.setAccount(parameter.getAccount());
        admin.setEmail(parameter.getEmail());
        admin.setPhone(parameter.getPhone());
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
     * @param token     管理员Token
     * @param parameter 请求数据 id 管理员ID oldPassword 原密码 newPassword 新密码
     * @return 成功或失败
     */
    @Override
    public ResultResponseEntity changeAdminPassword(String token, ChangePassword parameter) {
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
        if (!admin.getPassword().equals(parameter.getOldPassword())) {
            return ResultConstant.WRONG_PASSWORD;
        }
        if (parameter.getNewPassword().length() < 6 || parameter.getNewPassword().length() > 15) {
            return ResultConstant.PASSWORD_INVALID;
        }
        if (this.adminDao.updateAdminPassword(admin.getId(), parameter.getNewPassword()) != 1) {
            return ResultConstant.SERVER_ERROR;
        }
        return ResultUtil.createResult(1, "修改成功");
    }

    /**
     * 创建用户
     *
     * @param token     管理员Token
     * @param parameter 请求数据 name 用户名 account 账号 password 密码 email 邮箱 phone 手机
     * @return 成功或失败
     */
    @Override
    public ResultResponseEntity createUserManage(String token, CreateUser parameter) {
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
        if (this.userDao.getUserByAccount(parameter.getAccount()) != null) {
            return ResultConstant.ACCOUNT_EXISTED;
        }
        if (parameter.getEmail() != null && this.userDao.getUserByEmail(parameter.getEmail()) != null) {
            return ResultConstant.EMAIL_EXISTED;
        }
        if (parameter.getPhone() != null && this.userDao.getUserByPhone(parameter.getPhone()) != null) {
            return ResultConstant.PHONE_EXISTED;
        }
        User user = new User(parameter.getAccount(), parameter.getPassword(), parameter.getEmail(),
                parameter.getPhone(), parameter.getName());
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
     * @param token     管理员Token
     * @param parameter 请求数据 name 用户名 account 账号 password 密码 email 邮箱 phone 手机 key 授权码
     * @return 成功或失败
     */
    @Override
    public ResultResponseEntity createAdminManage(String token, CreateAdmin parameter) {
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
        // 授权码验证接口
        if (false) {
            return ResultConstant.INVALID_KEY;
        }
        // 通过授权码获取对应权限等级
        int level = 1;
        if (this.adminDao.getAdminByAccount(parameter.getAccount()) != null) {
            return ResultConstant.ACCOUNT_EXISTED;
        }
        if (this.adminDao.getAdminByEmail(parameter.getEmail()) != null) {
            return ResultConstant.EMAIL_EXISTED;
        }
        if (this.adminDao.getAdminByPhone(parameter.getPhone()) != null) {
            return ResultConstant.PHONE_EXISTED;
        }
        admin = new Admin(parameter.getAccount(), parameter.getPassword(), parameter.getEmail(), parameter.getPhone(),
                parameter.getName(), level);
        this.adminDao.insertAdmin(admin);
        return ResultConstant.SUCCESS;
    }

    /**
     * 删除用户
     *
     * @param token     管理员Token
     * @param parameter 请求数据 id 用户ID
     * @return 成功或失败
     */
    @Override
    public ResultResponseEntity deleteUserManage(String token, DeleteUser parameter) {
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
        User user = this.userDao.getUserById(parameter.getId());
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
     * @param token     管理员Token
     * @param parameter 请求数据 id 管理员ID
     * @return 成功或失败
     */
    @Override
    public ResultResponseEntity deleteAdminManage(String token, DeleteAdmin parameter) {
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
        admin = this.adminDao.getAdminById(parameter.getId());
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
     * @param pageSize   分页大小
     * @param sortKey    排序Key
     * @param sortType   排序类型
     * @param status     状态
     * @param startTime  起始时间
     * @param endTime    终止时间
     * @param phone      手机
     * @param email      邮箱
     * @param account    账号
     * @return 用户列表
     */
    @Override
    public ResultResponseEntity getUserListManage(String token, Integer pageNumber, Integer pageSize, String sortKey, String sortType, Integer status, Long startTime, Long endTime, String phone, String email, String account) {
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
        String orderBy = sortKey + " " + sortType;
        PageHelper.startPage(pageNumber, pageSize, orderBy);
        List<User> result;
        com.ztu.cloud.cloud.common.dto.condition.User conditionBean = new com.ztu.cloud.cloud.common.dto.condition.User(account, email, phone, status, startTime, endTime);
        try {
            result = this.userDao.getUsers(conditionBean);
        } catch (Exception e) {
            e.printStackTrace();
            result = new LinkedList<>();
        }
        List<com.ztu.cloud.cloud.common.vo.admin.User> userVoList = new LinkedList<>();
        for (User user : result) {
            Long repoSize = this.userRepositoryDao.getRepoSizeById(user.getRepoId());
            if (repoSize == null) {
                repoSize = 0L;
            }
            userVoList.add(new com.ztu.cloud.cloud.common.vo.admin.User(user, repoSize));
        }
        return ResultUtil.createResult("查询成功", new PageVo<com.ztu.cloud.cloud.common.vo.admin.User>(new PageVo<>(new PageInfo<>(result), sortKey, sortType), userVoList));
    }

    /**
     * 获取管理员列表
     *
     * @param token      管理员Token
     * @param pageNumber 页数
     * @param pageSize   分页大小
     * @param sortKey    排序Key
     * @param sortType   排序类型
     * @param status     状态
     * @param startTime  起始时间
     * @param endTime    终止时间
     * @param phone      手机
     * @param email      邮箱
     * @param account    账号
     * @return 管理员列表
     */
    @Override
    public ResultResponseEntity getAdminListManage(String token, Integer pageNumber, Integer pageSize, String sortKey, String sortType, Integer status, Long startTime, Long endTime, String phone, String email, String account) {
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
        String orderBy = sortKey + " " + sortType;
        PageHelper.startPage(pageNumber, pageSize, orderBy);
        List<Admin> result;
        com.ztu.cloud.cloud.common.dto.condition.Admin conditionBean = new com.ztu.cloud.cloud.common.dto.condition.Admin(account, email, phone, status, startTime, endTime);
        try {
            result = this.adminDao.getAdmins(conditionBean);
        } catch (Exception e) {
            e.printStackTrace();
            result = new LinkedList<>();
        }
        PageVo<Admin> pageVo = new PageVo<>(new PageInfo<>(result), sortKey, sortType);
        return ResultUtil.createResult("查询成功", pageVo);
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
     * @param token     管理员Token
     * @param parameter 请求数据 id 用户ID account 账号 email 邮箱 phone 手机 name 昵称 password 密码 status 状态 level 等级 repoSize 仓库大小
     * @return 用户信息
     */
    @Override
    public ResultResponseEntity changeUserInfoManage(String token, ChangeUserInfoManage parameter) {
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
        User user = this.userDao.getUserById(parameter.getId());
        if (user == null) {
            return ResultConstant.TARGET_NOT_EXISTED;
        }
        if (!user.getAccount().equals(parameter.getAccount())
                && this.adminDao.getAdminByAccount(parameter.getAccount()) != null) {
            return ResultConstant.ACCOUNT_EXISTED;
        }
        if (parameter.getEmail() != null && !parameter.getEmail().equals(user.getEmail())
                && this.adminDao.getAdminByEmail(parameter.getEmail()) != null) {
            return ResultConstant.EMAIL_EXISTED;
        }
        if (parameter.getPhone() != null && !parameter.getPhone().equals(user.getPhone())
                && this.adminDao.getAdminByPhone(parameter.getPhone()) != null) {
            return ResultConstant.PHONE_EXISTED;
        }
        user.setName(parameter.getName());
        user.setAccount(parameter.getAccount());
        user.setEmail(parameter.getEmail());
        user.setPhone(parameter.getPhone());
        user.setPassword(parameter.getPassword());
        user.setStatus(parameter.getStatus());
        user.setLevel(parameter.getLevel());
        user.setChangeTime(System.currentTimeMillis());
        if (this.userDao.updateUser(user) != 1
                || this.userRepositoryDao.updateRepoSizeById(user.getRepoId(), parameter.getRepoSize()) != 1) {
            return ResultConstant.SERVER_ERROR;
        }
        user = this.userDao.getUserById(user.getId());
        return ResultUtil.createResult(1, "修改成功", new UserInfo(user));
    }

    /**
     * 修改管理员信息
     *
     * @param token     管理员Token
     * @param parameter 请求数据 id 管理员ID account 账号 email 邮箱 phone 手机 password 密码 name 昵称 status 状态 level 等级
     * @return 用户信息
     */
    @Override
    public ResultResponseEntity changeAdminInfoManage(String token, ChangeAdminInfoManage parameter) {
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
        admin = this.adminDao.getAdminById(parameter.getId());
        if (admin == null) {
            return ResultConstant.TARGET_NOT_EXISTED;
        }
        if (!admin.getAccount().equals(parameter.getAccount())
                && this.adminDao.getAdminByAccount(parameter.getAccount()) != null) {
            return ResultConstant.ACCOUNT_EXISTED;
        }
        if (!parameter.getEmail().equals(admin.getEmail())
                && this.adminDao.getAdminByEmail(parameter.getEmail()) != null) {
            return ResultConstant.EMAIL_EXISTED;
        }
        if (!parameter.getPhone().equals(admin.getPhone())
                && this.adminDao.getAdminByPhone(parameter.getPhone()) != null) {
            return ResultConstant.PHONE_EXISTED;
        }
        admin.setName(parameter.getName());
        admin.setAccount(parameter.getAccount());
        admin.setEmail(parameter.getEmail());
        admin.setPhone(parameter.getPhone());
        admin.setPassword(parameter.getPassword());
        admin.setStatus(parameter.getStatus());
        admin.setLevel(parameter.getLevel());
        admin.setChangeTime(System.currentTimeMillis());
        if (this.adminDao.updateAdmin(admin) != 1) {
            return ResultConstant.SERVER_ERROR;
        }
        admin = this.adminDao.getAdminById(admin.getId());
        return ResultUtil.createResult(1, "修改成功", new AdminInfo(admin));
    }
}
