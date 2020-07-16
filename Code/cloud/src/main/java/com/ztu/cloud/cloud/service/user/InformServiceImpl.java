package com.ztu.cloud.cloud.service.user;

import com.ztu.cloud.cloud.common.bean.mongodb.CommonUserInform;
import com.ztu.cloud.cloud.common.bean.mongodb.UserInform;
import com.ztu.cloud.cloud.common.bean.mysql.User;
import com.ztu.cloud.cloud.common.constant.ResultConstant;
import com.ztu.cloud.cloud.common.dao.mongodb.CommonUserInformDao;
import com.ztu.cloud.cloud.common.dao.mongodb.UserInformDao;
import com.ztu.cloud.cloud.common.dao.mysql.UserMapper;
import com.ztu.cloud.cloud.common.vo.ResultResponseEntity;
import com.ztu.cloud.cloud.common.vo.user.Inform;
import com.ztu.cloud.cloud.common.vo.user.InformList;
import com.ztu.cloud.cloud.util.ResultUtil;
import com.ztu.cloud.cloud.util.TokenUtil;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;

/**
 * @author Jager
 * @description 通知业务实现
 * @date 2020/06/28-16:49
 **/
@Component
public class InformServiceImpl implements InformService {
    UserMapper userDao;
    CommonUserInformDao commonUserInformDao;
    UserInformDao userInformDao;

    public InformServiceImpl(UserMapper userDao, CommonUserInformDao commonUserInformDao, UserInformDao userInformDao) {
        this.userDao = userDao;
        this.commonUserInformDao = commonUserInformDao;
        this.userInformDao = userInformDao;
    }

    /**
     * 获取通知列表
     *
     * @param token 用户Token
     * @return 通知列表
     */
    @Override
    public ResultResponseEntity getInformList(String token) {
        if (!TokenUtil.isUser(token)) {
            return ResultConstant.TOKEN_INVALID;
        }
        int id = TokenUtil.getId(token);
        User user = this.userDao.getUserById(id);
        if (user == null) {
            return ResultConstant.USER_NOT_FOUND;
        }
        if (user.getStatus() != 1) {
            return ResultConstant.USER_STATUS_ABNORMAL;
        }
        List<CommonUserInform> validList = this.commonUserInformDao.getValidList();
        UserInform userInform = this.userInformDao.getById(id);
        if (userInform == null) {
            userInform = new UserInform(id, new HashMap<>(10));
            this.userInformDao.insert(userInform);
            for (CommonUserInform item : validList) {
                item.setStatus(0);
            }
        } else {
            for (CommonUserInform item : validList) {
                if (userInform.getStatus().get(item.getId()) == null) {
                    item.setStatus(0);
                } else {
                    item.setStatus(userInform.getStatus().get(item.getId()));
                }
            }
        }
        return ResultUtil.createResult("成功", new InformList(validList));
    }

    /**
     * 获取通知信息
     *
     * @param token    用户Token
     * @param informId 通知ID
     * @return 通知信息
     */
    @Override
    public ResultResponseEntity getInform(String token, String informId) {
        if (!TokenUtil.isUser(token)) {
            return ResultConstant.TOKEN_INVALID;
        }
        int id = TokenUtil.getId(token);
        User user = this.userDao.getUserById(id);
        if (user == null) {
            return ResultConstant.USER_NOT_FOUND;
        }
        if (user.getStatus() != 1) {
            return ResultConstant.USER_STATUS_ABNORMAL;
        }
        CommonUserInform inform = this.commonUserInformDao.getById(informId);
        if (inform == null) {
            return ResultConstant.TARGET_NOT_EXISTED;
        }
        UserInform userInform = this.userInformDao.getById(id);
        if (userInform == null) {
            inform.setStatus(0);
        } else {
            if (userInform.getStatus().get(informId) == null) {
                inform.setStatus(0);
            } else {
                inform.setStatus(userInform.getStatus().get(informId));
            }
        }
        return ResultUtil.createResult("成功", new Inform(inform));
    }

    /**
     * 修改通知状态
     *
     * @param token    用户Token
     * @param informId 通知ID
     * @param status   通知状态
     * @return 是否成功
     */
    @Override
    public ResultResponseEntity changeInformStatus(String token, String informId, int status) {
        if (!TokenUtil.isUser(token)) {
            return ResultConstant.TOKEN_INVALID;
        }
        int id = TokenUtil.getId(token);
        User user = this.userDao.getUserById(id);
        if (user == null) {
            return ResultConstant.USER_NOT_FOUND;
        }
        if (user.getStatus() != 1) {
            return ResultConstant.USER_STATUS_ABNORMAL;
        }
        CommonUserInform inform = this.commonUserInformDao.getById(informId);
        if (inform == null) {
            return ResultConstant.TARGET_NOT_EXISTED;
        }
        UserInform userInform = this.userInformDao.getById(id);
        if (userInform == null) {
            userInform = new UserInform(id, new HashMap<>(1));
            this.userInformDao.insert(userInform);
        }
        userInform.getStatus().put(informId, status);
        this.userInformDao.updateStatusById(id, userInform.getStatus());
        return ResultUtil.createResult(1, "成功");
    }
}
