package com.ztu.cloud.cloud.controller.admin;

import com.ztu.cloud.cloud.common.dto.user.download.Download;
import com.ztu.cloud.cloud.common.validation.Token;
import com.ztu.cloud.cloud.common.vo.ResultResponseEntity;
import com.ztu.cloud.cloud.service.admin.FileService;
import com.ztu.cloud.cloud.util.TokenUtil;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * @author Jager
 * @description 管理员文件控制接口
 * @date 2020/06/28-08:43
 **/
@RestController
@RequestMapping("/admin")
@Validated
public class FileController {
	FileService fileService;

	public FileController(FileService fileService) {
		this.fileService = fileService;
	}

	/**
	 * 下载文件
	 *
	 * @param request  request
	 * @param response response
	 * @param token    管理员Token
	 * @param fileId   文件ID
	 */
	@GetMapping("/download/{fileId}")
	public void download(HttpServletRequest request, HttpServletResponse response,
	                     @RequestHeader(TokenUtil.TOKEN_HEADER) @Token(role = "admin") String token,
	                     @PathVariable @NotBlank(message = "文件ID不能为空") String fileId) {
		Download download = this.fileService.download(token, fileId);
		if (download == null) {
			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/json");
			try {
				ServletOutputStream outputStream = response.getOutputStream();
				// TODO改为JSON字符串
				String content = "下载失效！";
				outputStream.write(content.getBytes());
				outputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return;
		}
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/force-download");
		try {
			response.setHeader("Content-Disposition",
					"attachment; fileName=" + URLEncoder.encode(download.getFileName(), "UTF-8") + ";filename*=utf-8''"
							+ URLEncoder.encode(download.getFileName(), "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		byte[] buffer = new byte[1024];
		try {
			OutputStream os = response.getOutputStream();
			int i = download.getInputStream().read(buffer);
			while (i != -1) {
				os.write(buffer, 0, i);
				i = download.getInputStream().read(buffer);
			}
			os.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取文件列表
	 *
	 * @param token      管理员Token
	 * @param pageNumber 页数
	 * @param pageSize   分页大小
	 * @param sortKey    排序主键
	 * @param sortType   排序类型
	 * @return 文件列表
	 */
	@GetMapping("/file/list/{pageNumber}")
	public ResultResponseEntity getFileList(
			@RequestHeader(TokenUtil.TOKEN_HEADER) @Token(role = "admin") String token,
			@PathVariable @Min(1) Integer pageNumber,
			@RequestParam(name = "pageSize", defaultValue = "20", required = false) @Min(1) Integer pageSize,
			@RequestParam(name = "sortKey", defaultValue = "id", required = false) String sortKey,
			@RequestParam(value = "sortType", defaultValue = "asc", required = false) String sortType) {
		return this.fileService.getFileList(token, pageNumber, pageSize, sortKey, sortType);
	}

	/**
	 * 获取文件信息
	 *
	 * @param token  管理员Token
	 * @param fileId 文件ID
	 * @return 文件信息
	 */
	@GetMapping("/file/{fileId}/info")
	public ResultResponseEntity getFileInfo(
			@RequestHeader(TokenUtil.TOKEN_HEADER) @Token(role = "admin") String token,
			@PathVariable @NotBlank(message = "文件ID不能为空") String fileId) {
		return this.fileService.getFileInfo(token, fileId);
	}

	/**
	 * 删除文件
	 *
	 * @param token  管理员Token
	 * @param fileId 文件ID
	 * @return 是否成功
	 */
	@DeleteMapping("/file/{fileId}")
	public ResultResponseEntity deleteFile(
			@RequestHeader(TokenUtil.TOKEN_HEADER) @Token(role = "admin") String token,
			@PathVariable @NotBlank(message = "文件ID不能为空") String fileId) {
		return this.fileService.deleteFile(token, fileId);
	}
}
