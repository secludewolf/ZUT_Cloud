package com.ztu.cloud.cloud.controller.admin;

import com.ztu.cloud.cloud.common.validation.Token;
import com.ztu.cloud.cloud.common.vo.ResultResponseEntity;
import com.ztu.cloud.cloud.service.admin.ShareManageService;
import com.ztu.cloud.cloud.util.TokenUtil;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author Jager
 * @description 分享接口
 * @date 2020/06/28-13:22
 **/
@RestController
public class ShareAdminController {
    ShareManageService shareService;

    public ShareAdminController(ShareManageService shareService) {
        this.shareService = shareService;
    }

    /**
     * 获取分享列表
     *
     * @param token
     *            管理员Token
     * @param pageNumber
     *            页数
     * @return 分享列表
     */
    @GetMapping("/admin/share/list/{pageNumber}")
    public ResultResponseEntity getShareList(
        @RequestHeader(TokenUtil.TOKEN_HEADER) @Token(role = "admin") String token,
        @PathVariable @NotNull(message = "分页数不能为空") Integer pageNumber) {
        return this.shareService.getShareList(token, pageNumber);
    }

    /**
     * 获取分享信息
     *
     * @param token
     *            管理员Token
     * @param shareId
     *            分享ID
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
     * @param token
     *            管理员Token
     * @param shareId
     *            分享ID
     * @return 删除结果
     */
    @DeleteMapping("/admin/share/{shareId}")
    public ResultResponseEntity deleteShare(
        @RequestHeader(TokenUtil.TOKEN_HEADER) @Token(role = "admin") String token,
        @PathVariable @NotBlank(message = "分享ID不能为空") String shareId) {
        return this.shareService.deleteShare(token, shareId);
    }
}
