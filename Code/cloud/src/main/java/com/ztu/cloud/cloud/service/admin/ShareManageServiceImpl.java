package com.ztu.cloud.cloud.service.admin;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ztu.cloud.cloud.common.bean.mongodb.ShareRepository;
import com.ztu.cloud.cloud.common.bean.mysql.Admin;
import com.ztu.cloud.cloud.common.bean.mysql.Share;
import com.ztu.cloud.cloud.common.constant.ResultConstant;
import com.ztu.cloud.cloud.common.dao.mongodb.ShareRepositoryDao;
import com.ztu.cloud.cloud.common.dao.mysql.AdminMapper;
import com.ztu.cloud.cloud.common.dao.mysql.ShareMapper;
import com.ztu.cloud.cloud.common.dao.mysql.ShareReportMapper;
import com.ztu.cloud.cloud.common.vo.ResultResponseEntity;
import com.ztu.cloud.cloud.common.vo.common.PageVo;
import com.ztu.cloud.cloud.util.ResultUtil;
import com.ztu.cloud.cloud.util.TokenUtil;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
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
    ShareReportMapper shareReportMapper;

    public ShareManageServiceImpl(AdminMapper adminDao, ShareMapper shareDao, ShareRepositoryDao shareRepositoryDao, ShareReportMapper shareReportMapper) {
        this.adminDao = adminDao;
        this.shareDao = shareDao;
        this.shareRepositoryDao = shareRepositoryDao;
        this.shareReportMapper = shareReportMapper;
    }

    /**
     * 获取分享列表
     *
     * @param token      管理员Token
     * @param pageNumber 页数
     * @param pageSize   分页大小
     * @param sortKey    排序主键
     * @param sortType   排序类型
     * @param status     状态
     * @param startTime  起始时间
     * @param endTime    终止时间
     * @param userId     文件类型
     * @param name       文件名称
     * @return 分享列表
     */
    @Override
    public ResultResponseEntity getShareList(String token, Integer pageNumber, Integer pageSize, String sortKey, String sortType, Integer status, Long startTime, Long endTime, Integer userId, String name) {
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
        List<Share> result;
        com.ztu.cloud.cloud.common.dto.condition.Share conditionBean = new com.ztu.cloud.cloud.common.dto.condition.Share(userId, name, status, startTime, endTime);
        try {
            result = this.shareDao.getShares(conditionBean);
        } catch (Exception e) {
            e.printStackTrace();
            result = new LinkedList<>();
        }
        for (Share share : result) {
            if (share.getReportNumber() > 0) {
                share.setReportList(this.shareReportMapper.getShareReportListByFileId(share.getId()));
            } else {
                share.setReportList(new LinkedList<>());
            }
        }
        PageVo<Share> pageVo = new PageVo<>(new PageInfo<>(result), sortKey, sortType);
        return ResultUtil.createResult("成功", pageVo);
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
        Share shareInfo = this.shareDao.getShareById(shareId);
        if (shareInfo == null) {
            return ResultConstant.TARGET_NOT_EXISTED;
        }
        ShareRepository shareRepository = this.shareRepositoryDao.getById(shareInfo.getRepoId());
        return ResultUtil.createResult("成功",
                new com.ztu.cloud.cloud.common.vo.admin.ShareRepository(shareInfo, shareRepository));
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
        Share share = this.shareDao.getShareById(shareId);
        if (share == null) {
            return ResultConstant.TARGET_NOT_EXISTED;
        }
        this.shareDao.updateShareStatus(shareId, -1);
        return ResultUtil.createResult(1, "删除成功");
    }
}
