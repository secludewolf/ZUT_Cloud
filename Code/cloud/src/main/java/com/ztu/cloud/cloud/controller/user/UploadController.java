package com.ztu.cloud.cloud.controller.user;

import com.ztu.cloud.cloud.common.validation.Token;
import com.ztu.cloud.cloud.common.vo.ResultResponseEntity;
import com.ztu.cloud.cloud.service.user.UploadService;
import com.ztu.cloud.cloud.util.TokenUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author Jager
 * @description 上传接口
 * @date 2020/06/27-09:31
 **/
@RestController
public class UploadController {
	UploadService uploadService;

	public UploadController(UploadService uploadService) {
		this.uploadService = uploadService;
	}

	/**
	 * 上传文件
	 *
	 * @param token    用户Token
	 * @param block    文件块
	 * @param fileName 文件名
	 * @param blockMd5 文件块MD5
	 * @param fileMd5  文件MD5
	 * @param index    文件块号
	 * @param length   文件总块数
	 * @return 已接收的文件块号
	 */
	@PostMapping("/upload")
	public ResultResponseEntity upload(@RequestHeader(TokenUtil.TOKEN_HEADER) @Token(role = "user") String token,
	                                   @RequestParam("block") @NotNull(message = "文件块不能为空") MultipartFile block,
	                                   @RequestParam("fileName") @NotBlank(message = "文件名不能为空") String fileName,
	                                   @RequestParam("blockMd5") @NotBlank(message = "文件块特征码不能为空") String blockMd5,
	                                   @RequestParam("fileMd5") @NotBlank(message = "文件特征码不能为空") String fileMd5,
	                                   @RequestParam("index") @NotNull(message = "文件块序号不能为空") Integer index,
	                                   @RequestParam("length") @NotNull(message = "文件块长度不能为空") Integer length) {
		return uploadService.upload(token, fileName, block, blockMd5, fileMd5, index, length);
	}
}
