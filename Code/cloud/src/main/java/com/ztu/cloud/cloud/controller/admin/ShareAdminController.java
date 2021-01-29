package com.ztu.cloud.cloud.controller.admin;

import com.ztu.cloud.cloud.common.validation.Token;
import com.ztu.cloud.cloud.common.vo.ResultResponseEntity;
import com.ztu.cloud.cloud.service.admin.ShareManageService;
import com.ztu.cloud.cloud.util.TokenUtil;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

/**
 * @author Jager
 * @description 分享接口
 * @date 2020/06/28-13:22
 **/
@RestController
@Validated
public class ShareAdminController {
    ShareManageService shareService;

    public ShareAdminController(ShareManageService shareService) {
        this.shareService = shareService;
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
     */
    @GetMapping("/admin/share/list/{pageNumber}")
    public ResultResponseEntity getShareList(
            @RequestHeader(TokenUtil.TOKEN_HEADER) @Token(role = "admin") String token,
            @PathVariable @Min(1) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = "20", required = false) @Min(1) Integer pageSize,
            @RequestParam(name = "sortKey", defaultValue = "id", required = false) String sortKey,
            @RequestParam(value = "sortType", defaultValue = "asc", required = false) String sortType,
            @RequestParam(value = "status", required = false) Integer status,
            @RequestParam(value = "startTime", required = false) Long startTime,
            @RequestParam(value = "endTime", required = false) Long endTime,
            @RequestParam(value = "userId", required = false) Integer userId,
            @RequestParam(value = "name", required = false) String name) {
        return this.shareService.getShareList(token, pageNumber, pageSize, sortKey, sortType, status, startTime, endTime, userId, name);
    }

    /**
     * 获取分享信息
     *
     * @param token   管理员Token
     * @param shareId 分享ID
     * @return 分享信息
     */
    @GetMapping("/admin/share/{shareId}")
    public ResultResponseEntity getShare(
            @RequestHeader(TokenUtil.TOKEN_HEADER) @Token(role = "admin") String token,
            @PathVariable @NotBlank(message = "分享ID不能为空") String shareId) {
        return this.shareService.getShare(token, shareId);
    }

    /**
     * 删除分享
     *
     * @param token   管理员Token
     * @param shareId 分享ID
     * @return 删除结果
     */
    @DeleteMapping("/admin/share/{shareId}")
    public ResultResponseEntity deleteShare(
            @RequestHeader(TokenUtil.TOKEN_HEADER) @Token(role = "admin") String token,
            @PathVariable @NotBlank(message = "分享ID不能为空") String shareId) {
        return this.shareService.deleteShare(token, shareId);
    }
}
