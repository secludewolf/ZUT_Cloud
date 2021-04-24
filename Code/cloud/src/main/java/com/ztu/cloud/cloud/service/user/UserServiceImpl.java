package com.ztu.cloud.cloud.service.user;

import com.ztu.cloud.cloud.common.bean.mongodb.UserRepository;
import com.ztu.cloud.cloud.common.bean.mysql.User;
import com.ztu.cloud.cloud.common.constant.ResultConstant;
import com.ztu.cloud.cloud.common.dao.mongodb.UserRepositoryDao;
import com.ztu.cloud.cloud.common.dao.mysql.UserMapper;
import com.ztu.cloud.cloud.common.dto.common.ChangePassword;
import com.ztu.cloud.cloud.common.dto.common.ForgetEmail;
import com.ztu.cloud.cloud.common.dto.common.LoginAccount;
import com.ztu.cloud.cloud.common.dto.common.LoginEmail;
import com.ztu.cloud.cloud.common.dto.user.user.ChangeUserInfo;
import com.ztu.cloud.cloud.common.dto.user.user.RegisterAccount;
import com.ztu.cloud.cloud.common.dto.user.user.RegisterEmail;
import com.ztu.cloud.cloud.common.vo.ResultResponseEntity;
import com.ztu.cloud.cloud.common.vo.user.UserInfo;
import com.ztu.cloud.cloud.common.vo.user.UserLogin;
import com.ztu.cloud.cloud.util.EmailUtil;
import com.ztu.cloud.cloud.util.ResultUtil;
import com.ztu.cloud.cloud.util.TokenUtil;
import org.springframework.stereotype.Service;

/**
 * @author Jager
 * @description 用户数据业务接口实现
 * @date 2020/06/23-10:35
 **/
@Service
public class UserServiceImpl implements UserService {
    UserMapper userDao;
    UserRepositoryDao userRepositoryDao;
    EmailUtil emailUtil;

    public UserServiceImpl(UserMapper userDao, UserRepositoryDao userRepositoryDao, EmailUtil emailUtil) {
        this.userDao = userDao;
        this.userRepositoryDao = userRepositoryDao;
        this.emailUtil = emailUtil;
    }

    /**
     * 通过Token登陆,并获取用户信息
     *
     * @param token 用户Token
     * @return 用户信息, 仓库信息以及更新后的Token
     */
    @Override
    public ResultResponseEntity loginByToken(String token) {
        User user = getUser(token);
        ResultResponseEntity result = userIsInvalid(user, null);
        // 如果User状态异常
        if (result != null) {
            return result;
        }
        UserRepository repository = this.userRepositoryDao.getById(user.getRepoId());
        if (repository == null) {
            return ResultConstant.REPOSITORY_NOT_FOUND;
        }
        return ResultUtil.createResultWithToken("登陆成功", new UserLogin(user, repository), TokenUtil.refreshToken(token));
    }

    /**
     * 通过邮箱,密码登陆账户
     *
     * @param parameter 请求数据 email 邮箱 password 密码 rememberMe 记住我
     * @return 用户信息, 仓库信息以及更新后的Token
     */
    @Override
    public ResultResponseEntity loginByEmail(LoginEmail parameter) {
        User user = this.userDao.getUserByEmail(parameter.getEmail());
        return login(parameter.getPassword(), user, parameter.isRememberMe());
    }

    /**
     * 通过账号,密码登陆账户
     *
     * @param parameter 请求数据 account 账号 password 密码 rememberMe 记住我
     * @return 用户信息, 仓库信息以及更新后的Token
     */
    @Override
    public ResultResponseEntity loginByAccount(LoginAccount parameter) {
        User user = this.userDao.getUserByAccount(parameter.getAccount());
        return login(parameter.getPassword(), user, parameter.isRememberMe());
    }

    private ResultResponseEntity login(String password, User user, boolean isRememberMe) {
        if (user == null) {
            return ResultConstant.USER_NOT_FOUND;
        }
        if (user.getStatus() != 1) {
            return ResultConstant.USER_STATUS_ABNORMAL;
        }
        if (!user.getPassword().equals(password)) {
            return ResultConstant.WRONG_PASSWORD;
        }
        UserRepository repository = this.userRepositoryDao.getById(user.getRepoId());
        if (repository == null) {
            return ResultConstant.REPOSITORY_NOT_FOUND;
        }
        return ResultUtil.createResultWithToken("登陆成功", new UserLogin(user, repository),
                TokenUtil.createUserToken(user.getId(), isRememberMe));
    }

    /**
     * 通过邮箱重置密码
     *
     * @param parameter 请求数据 email 邮箱
     * @return 成功或失败
     */
    @Override
    public ResultResponseEntity forgetEmail(ForgetEmail parameter) {
        User user = this.userDao.getUserByEmail(parameter.getEmail());
        if (user == null) {
            return ResultConstant.USER_NOT_FOUND;
        }
        if (user.getStatus() != 1) {
            return ResultConstant.USER_STATUS_ABNORMAL;
        }
        // 生成随机密码
        String password = (System.currentTimeMillis() + "").substring(3);
        if (this.userDao.updateUserPassword(user.getId(), password) != 1) {
            return ResultConstant.SERVER_ERROR;
        }
        // 发送重置密码邮件
        this.emailUtil.sendPasswordToEmail(user.getEmail(), password);
        return ResultConstant.SUCCESS;
    }

    /**
     * 通过邮箱,密码注册账号
     *
     * @param parameter 请求数据 name 昵称 account 账号 email 邮箱 password 密码
     * @return 成功或失败
     */
    @Override
    public ResultResponseEntity registerByEmail(RegisterEmail parameter) {
        // 判断账号是否被使用
        if (this.userDao.getUserByAccount(parameter.getAccount()) != null) {
            return ResultConstant.ACCOUNT_EXISTED;
        }
        // 判断邮箱是否被使用
        if (this.userDao.getUserByEmail(parameter.getEmail()) != null) {
            return ResultConstant.EMAIL_EXISTED;
        }
        // 创建用户信息
        User user =
                new User(parameter.getAccount(), parameter.getPassword(), parameter.getEmail(), parameter.getName());
        this.userDao.insertUser(user);
        // 创建仓库信息
        UserRepository userRepository = new UserRepository(user.getId());
        this.userRepositoryDao.insert(userRepository);
        user.setRepoId(userRepository.getId());
        // 更新仓库信息
        this.userDao.updateUser(user);
        // 发送注册邮件
        this.emailUtil.sendRegisterSuccess(user.getEmail(), user.getAccount());
        return ResultConstant.SUCCESS;
    }

    /**
     * 通过账号,密码注册账号
     *
     * @param parameter 请求数据 name 昵称 account 账号 password 密码
     * @return 成功或失败
     */
    @Override
    public ResultResponseEntity registerByAccount(RegisterAccount parameter) {
        // 判断账号是否被使用
        if (this.userDao.getUserByAccount(parameter.getAccount()) != null) {
            return ResultConstant.ACCOUNT_EXISTED;
        }
        // 创建用户信息
        User user = new User(parameter.getAccount(), parameter.getPassword(), parameter.getName());
        this.userDao.insertUser(user);
        // 创建仓库信息
        UserRepository userRepository = new UserRepository(user.getId());
        this.userRepositoryDao.insert(userRepository);
        user.setRepoId(userRepository.getId());
        // 更新仓库信息
        this.userDao.updateUser(user);
        return ResultConstant.SUCCESS;
    }

    /**
     * 获取用户信息
     *
     * @param token  用户Token
     * @param userId 用户ID
     * @return 用户信息
     */
    @Override
    public ResultResponseEntity getUserInfo(String token, int userId) {
        User user = getUser(token);
        ResultResponseEntity result = userIsInvalid(user, null);
        // 如果User状态异常
        if (result != null) {
            return result;
        }
        user = this.userDao.getUserById(userId);
        if (user == null) {
            return ResultConstant.TARGET_NOT_EXISTED;
        }
        return ResultUtil.createResult(1, "查询成功", new UserInfo(user));
    }

    /**
     * 修改用户信息
     *
     * @param token     用户Token
     * @param parameter 请求数据 id 用户ID account 账号 email 邮箱 phone 手机 name 昵称
     * @return 用户信息
     */
    @Override
    public ResultResponseEntity changeUserInfo(String token, ChangeUserInfo parameter) {
        User user = getUser(token);
        ResultResponseEntity result = userIsInvalid(user, parameter.getId());
        // 如果User状态异常
        if (result != null) {
            return result;
        }
        // 判断账号是否被使用
        if (!user.getAccount().equals(parameter.getAccount())
                && this.userDao.getUserByAccount(parameter.getAccount()) != null) {
            return ResultConstant.ACCOUNT_EXISTED;
        }
        // 判断邮箱是否被使用
        if (parameter.getEmail() != null && !parameter.getEmail().equals(user.getEmail())
                && this.userDao.getUserByEmail(parameter.getEmail()) != null) {
            return ResultConstant.EMAIL_EXISTED;
        }
        // 判断手机是否被使用
        if (parameter.getPhone() != null && !parameter.getPhone().equals(user.getPhone())
                && this.userDao.getUserByPhone(parameter.getPhone()) != null) {
            return ResultConstant.PHONE_EXISTED;
        }
        user.setName(parameter.getName());
        user.setAccount(parameter.getAccount());
        user.setEmail(parameter.getEmail());
        user.setPhone(parameter.getPhone());
        user.setChangeTime(System.currentTimeMillis());
        // 更新用户信息并判断是否成功
        if (this.userDao.updateUser(user) != 1) {
            return ResultConstant.SERVER_ERROR;
        }
        return ResultUtil.createResult(1, "修改成功", new UserInfo(user));
    }

    /**
     * 修改用户密码
     *
     * @param token     用户Token
     * @param parameter 请求数据 id 用户ID oldPassword 原密码 newPassword 新密码
     * @return 成功或失败
     */
    @Override
    public ResultResponseEntity changeUserPassword(String token, ChangePassword parameter) {
        User user = getUser(token);
        ResultResponseEntity result = userIsInvalid(user, parameter.getId());
        // 如果User状态异常
        if (result != null) {
            return result;
        }
        // 判断原始密码是否正确
        if (!user.getPassword().equals(parameter.getOldPassword())) {
            return ResultConstant.WRONG_PASSWORD;
        }
        // 判断是否更新成功
        if (this.userDao.updateUserPassword(user.getId(), parameter.getNewPassword()) != 1) {
            return ResultConstant.SERVER_ERROR;
        }
        return ResultConstant.SUCCESS;
    }

    private User getUser(String token) {
        int id = TokenUtil.getId(token);
        return this.userDao.getUserById(id);
    }

    private ResultResponseEntity userIsInvalid(User user, Integer id) {
        if (user == null) {
            return ResultConstant.USER_NOT_FOUND;
        }
        if (id != null && user.getId() != id) {
            return ResultConstant.NO_ACCESS;
        }
        if (user.getStatus() != 1) {
            return ResultConstant.USER_STATUS_ABNORMAL;
        }
        return null;
    }
}
