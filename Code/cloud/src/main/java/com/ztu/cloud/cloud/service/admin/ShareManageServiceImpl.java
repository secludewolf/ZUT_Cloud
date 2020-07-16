package com.ztu.cloud.cloud.service.admin;

import com.ztu.cloud.cloud.common.bean.mongodb.ShareRepository;
import com.ztu.cloud.cloud.common.bean.mysql.Admin;
import com.ztu.cloud.cloud.common.bean.mysql.Share;
import com.ztu.cloud.cloud.common.constant.ResultConstant;
import com.ztu.cloud.cloud.common.dao.mongodb.ShareRepositoryDao;
import com.ztu.cloud.cloud.common.dao.mysql.AdminMapper;
import com.ztu.cloud.cloud.common.dao.mysql.ShareMapper;
import com.ztu.cloud.cloud.common.vo.ResultResponseEntity;
import com.ztu.cloud.cloud.common.vo.admin.ShareList;
import com.ztu.cloud.cloud.util.ResultUtil;
import com.ztu.cloud.cloud.util.TokenUtil;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Jager
 * @description 分享业务接口实现
 * @date 2020/06/28-13:23
 **/
@Component
public class ShareManageServiceImpl implements ShareManageService {
    AdminMapper adminDao;
    ShareMapper shareDao;
    ShareRepositoryDao shareRepositoryDao;

    public ShareManageServiceImpl(AdminMapper adminDao, ShareMapper shareDao, ShareRepositoryDao shareRepositoryDao) {
        this.adminDao = adminDao;
        this.shareDao = shareDao;
        this.shareRepositoryDao = shareRepositoryDao;
    }

    /**
     * 获取分享列表
     *
     * @param token      管理员Token
     * @param pageNumber 页数
     * @return 分享列表
     */
    @Override
    public ResultResponseEntity getShareList(String token, int pageNumber) {
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
        //TODO 权限验证
        if (admin.getLevel() <= 0) {
            return ResultConstant.NO_ACCESS;
        }
        List<Share> shareList = this.shareDao.getShare((pageNumber - 1) * 20, pageNumber * 20);
        return ResultUtil.createResult("成功", new ShareList(shareList));
    }

    /**
     * 获取分享信息
     *
     * @param token   管理员Token
     * @param shareId 分享ID
     * @return 分享信息
     */
    @Override
    public ResultResponseEntity getShare(String token, String shareId) {
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
        //TODO 权限验证
        if (admin.getLevel() <= 0) {
            return ResultConstant.NO_ACCESS;
        }
        Share shareInfo = this.shareDao.getShareById(shareId);
        if (shareInfo == null) {
            return ResultConstant.TARGET_NOT_EXISTED;
        }
        ShareRepository shareRepository = this.shareRepositoryDao.getById(shareInfo.getRepoId());
        return ResultUtil.createResult("成功", new com.ztu.cloud.cloud.common.vo.admin.ShareRepository(shareInfo, shareRepository));
    }

    /**
     * 删除分享
     *
     * @param token   管理员Token
     * @param shareId 分享ID
     * @return 删除结果
     */
    @Override
    public ResultResponseEntity deleteShare(String token, String shareId) {
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
        //TODO 权限验证
        if (admin.getLevel() <= 0) {
            return ResultConstant.NO_ACCESS;
        }
        Share share = this.shareDao.getShareById(shareId);
        if (share == null) {
            return ResultConstant.TARGET_NOT_EXISTED;
        }
        this.shareDao.updateShareStatus(shareId, -1);
        return ResultUtil.createResult(1, "删除成功");
    }
}
