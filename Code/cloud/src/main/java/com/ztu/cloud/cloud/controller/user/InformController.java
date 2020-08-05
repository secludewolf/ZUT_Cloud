package com.ztu.cloud.cloud.controller.user;

import com.ztu.cloud.cloud.common.vo.ResultResponseEntity;
import com.ztu.cloud.cloud.service.user.InformService;
import com.ztu.cloud.cloud.util.TokenUtil;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author Jager
 * @description 通知接口
 * @date 2020/06/28-16:48
 **/
@RestController
public class InformController {
	InformService informService;

	public InformController(InformService informService) {
		this.informService = informService;
	}

	/**
	 * 获取通知列表
	 *
	 * @param token 用户Token
	 * @return 通知列表
	 */
	@GetMapping("/inform")
	public ResultResponseEntity getInformList(@RequestHeader(TokenUtil.TOKEN_HEADER) @NotBlank(message = "Token不能为空") String token) {
		return this.informService.getInformList(token);
	}

	/**
	 * 获取通知信息
	 *
	 * @param token    用户Token
	 * @param informId 通知ID
	 * @return 通知信息
	 */
	@GetMapping("/inform/{informId}")
	public ResultResponseEntity getInform(@RequestHeader(TokenUtil.TOKEN_HEADER) @NotBlank(message = "Token不能为空") String token,
	                                      @PathVariable @NotBlank(message = "通知ID不能为空") String informId) {
		return this.informService.getInform(token, informId);
	}

	/**
	 * 修改通知状态
	 *
	 * @param token    用户Token
	 * @param informId 通知ID
	 * @param status   通知状态
	 * @return 是否成功
	 */
	@PatchMapping("/inform/{informId}/{status}")
	public ResultResponseEntity changeInformStatus(@RequestHeader(TokenUtil.TOKEN_HEADER) @NotBlank(message = "Token不能为空") String token,
	                                               @PathVariable @NotBlank(message = "通知ID不能为空") String informId, @PathVariable @NotNull(message = "状态不能为空") Integer status) {
		return this.informService.changeInformStatus(token, informId, status);
	}
}
