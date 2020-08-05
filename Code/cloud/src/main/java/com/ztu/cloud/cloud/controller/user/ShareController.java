package com.ztu.cloud.cloud.controller.user;

import com.ztu.cloud.cloud.common.dto.user.share.CreateShare;
import com.ztu.cloud.cloud.common.dto.user.share.GetShare;
import com.ztu.cloud.cloud.common.dto.user.share.SaveShare;
import com.ztu.cloud.cloud.common.validation.Token;
import com.ztu.cloud.cloud.common.vo.ResultResponseEntity;
import com.ztu.cloud.cloud.service.user.ShareService;
import com.ztu.cloud.cloud.util.TokenUtil;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

/**
 * @author Jager
 * @description 分享管理接口
 * @date 2020/06/28-09:50
 **/
@RestController
public class ShareController {
	ShareService shareService;

	public ShareController(ShareService shareService) {
		this.shareService = shareService;
	}

	/**
	 * 创建分享
	 *
	 * @param token     用户Token
	 * @param parameter 请求参数
	 *                  repositoryId 仓库ID
	 *                  name 分享名称
	 *                  path 分享位置
	 *                  password 密码
	 *                  valid 有效期
	 * @return 分享信息
	 */
	@PutMapping("/share")
	public ResultResponseEntity createShare(@RequestHeader(TokenUtil.TOKEN_HEADER) @Token(role = "user") String token,
	                                        @RequestBody @Valid CreateShare parameter) {
		return this.shareService.createShare(token, parameter);
	}

	/**
	 * 获取分享列表
	 *
	 * @param token 用户Token
	 * @return 分享列表
	 */
	@GetMapping("/share/list")
	public ResultResponseEntity getShareList(@RequestHeader(TokenUtil.TOKEN_HEADER) @Token(role = "user") String token) {
		return this.shareService.getShareList(token);
	}

	/**
	 * 删除信息
	 *
	 * @param token   用户Token
	 * @param shareId 分享ID
	 * @return 删除结果
	 */
	@DeleteMapping("/share/{shareId}")
	public ResultResponseEntity deleteShare(@RequestHeader(TokenUtil.TOKEN_HEADER) @Token(role = "user") String token,
	                                        @PathVariable("shareId") @NotBlank(message = "分享ID不能为空") String shareId) {
		return this.shareService.deleteShare(token, shareId);
	}

	/**
	 * 获取分享信息
	 *
	 * @param token     用户Token
	 * @param parameter 请求参数
	 *                  shareId 分享ID
	 *                  password 分享密码
	 * @return 分享信息
	 */
	@PostMapping("/share")
	public ResultResponseEntity getShare(@RequestHeader(TokenUtil.TOKEN_HEADER) @Token(role = "user") String token,
	                                     @RequestBody @Valid GetShare parameter) {
		return this.shareService.getShare(token, parameter);
	}

	/**
	 * 保存分享
	 *
	 * @param token     用户信Token
	 * @param parameter 请求参数
	 *                  shareId 分享ID
	 *                  password 分享密码
	 *                  path 保存位置
	 * @return 是否保存成功
	 */
	@PostMapping("/share/save")
	public ResultResponseEntity saveShare(@RequestHeader(TokenUtil.TOKEN_HEADER) @Token(role = "user") String token,
	                                      @RequestBody @Valid SaveShare parameter) {
		return this.shareService.saveShare(token, parameter);
	}

}
