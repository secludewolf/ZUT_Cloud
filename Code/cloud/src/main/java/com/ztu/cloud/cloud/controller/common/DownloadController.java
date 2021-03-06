package com.ztu.cloud.cloud.controller.common;

import com.ztu.cloud.cloud.common.dto.user.download.Download;
import com.ztu.cloud.cloud.common.dto.user.download.DownloadId;
import com.ztu.cloud.cloud.common.log.SysLog;
import com.ztu.cloud.cloud.common.validation.Token;
import com.ztu.cloud.cloud.common.vo.ResultResponseEntity;
import com.ztu.cloud.cloud.service.common.DownloadService;
import com.ztu.cloud.cloud.util.CommonUtil;
import com.ztu.cloud.cloud.util.TokenUtil;
import org.apache.commons.io.IOUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.io.OutputStream;
import java.net.URLEncoder;

/**
 * @author Jager
 * @description 下载接口
 * @date 2020/06/27-09:31
 **/
@RestController
@Validated
public class DownloadController {
    DownloadService downloadService;

    public DownloadController(DownloadService downloadService) {
        this.downloadService = downloadService;
    }

    /**
     * 获取下载ID
     *
     * @param token     用户Token
     * @param parameter 请求参数 shareId 分享ID repositoryId 仓库ID fileId 文件Id fileName 文件名 folder 文件夹
     * @return 下载ID
     */
    @SysLog(descrption = "用户获取下载ID", type = "文件下载", modul = "公共模块")
    @PostMapping("/download")
    public ResultResponseEntity getDownloadId(
            @RequestHeader(TokenUtil.TOKEN_HEADER) @Token(role = "user") String token,
            @RequestBody @Valid DownloadId parameter) {
        return this.downloadService.getDownloadId(token, parameter);
    }

    /**
     * 获取下载ID
     *
     * @param token  管理员Token
     * @param fileId 文件ID
     * @return 下载ID
     */
    @SysLog(descrption = "管理员获取下载ID", type = "文件下载", modul = "公共模块")
    @PostMapping("/admin/download/{fileId}")
    public ResultResponseEntity getAdminDownloadId(
            @RequestHeader(TokenUtil.TOKEN_HEADER) @Token(role = "admin") String token,
            @PathVariable @NotBlank(message = "文件ID不能为空") String fileId) {
        return this.downloadService.getAdminDownloadId(token, fileId);
    }

    /**
     * 下载文件
     *
     * @param response   response
     * @param downloadId 下载ID
     */
    @SysLog(descrption = "通过下载ID下载文件", type = "文件下载", modul = "公共模块")
    @GetMapping("/download/{downloadId}")
    public void download(HttpServletResponse response,
                         @PathVariable @NotBlank(message = "下载ID不能为空") String downloadId) {
        Download download = this.downloadService.download(downloadId);
        if (download == null) {
            CommonUtil.returnError(0, "下载错误", response);
            return;
        }
        try {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/force-download");
            response.setHeader("Content-Disposition",
                    "attachment; fileName=" + URLEncoder.encode(download.getFileName(), "UTF-8") + ";filename*=utf-8''"
                            + URLEncoder.encode(download.getFileName(), "UTF-8"));
            OutputStream outputStream = response.getOutputStream();
            IOUtils.copy(download.getInputStream(), outputStream);
            download.getInputStream().close();
            outputStream.close();
        } catch (Exception e) {
            CommonUtil.returnError(0, "下载错误", response);
        }
    }
}
