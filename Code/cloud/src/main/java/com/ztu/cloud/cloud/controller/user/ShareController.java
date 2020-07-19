package com.ztu.cloud.cloud.controller.user;

import com.ztu.cloud.cloud.common.vo.ResultResponseEntity;
import com.ztu.cloud.cloud.service.user.ShareService;
import com.ztu.cloud.cloud.util.TokenUtil;
import org.springframework.web.bind.annotation.*;

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
	 * @param token 用户Token
	 * @param data  请求参数
	 *              repositoryId 仓库ID
	 *              name 分享名称
	 *              path 分享位置
	 *              password 密码
	 *              valid 有效期
	 * @return 分享信息
	 */
	@PutMapping("/share")
	public ResultResponseEntity createShare(@RequestHeader(TokenUtil.TOKEN_HEADER) String token, @RequestBody String data) {
		return this.shareService.createShare(token, data);
	}

	/**
	 * 获取分享列表
	 *
	 * @param token 用户Token
	 * @return 分享列表
	 */
	@GetMapping("/share/list")
	public ResultResponseEntity getShareList(@RequestHeader(TokenUtil.TOKEN_HEADER) String token) {
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
	public ResultResponseEntity deleteShare(@RequestHeader(TokenUtil.TOKEN_HEADER) String token, @PathVariable String shareId) {
		return this.shareService.deleteShare(token, shareId);
	}

	/**
	 * 获取分享信息
	 *
	 * @param token 用户Token
	 * @param data  请求参数
	 *              shareId 分享ID
	 *              password 分享密码
	 * @return 分享信息
	 */
	@PostMapping("/share")
	public ResultResponseEntity getShare(@RequestHeader(TokenUtil.TOKEN_HEADER) String token, @RequestBody String data) {
		return this.shareService.getShare(token, data);
	}

	/**
	 * 保存分享
	 *
	 * @param token 用户信Token
	 * @param data  请求参数
	 *              shareId 分享ID
	 *              password 分享密码
	 *              path 保存位置
	 * @return 是否保存成功
	 */
	@PostMapping("/share/save")
	public ResultResponseEntity saveShare(@RequestHeader(TokenUtil.TOKEN_HEADER) String token, @RequestBody String data) {
		return this.shareService.saveShare(token, data);
	}

}
