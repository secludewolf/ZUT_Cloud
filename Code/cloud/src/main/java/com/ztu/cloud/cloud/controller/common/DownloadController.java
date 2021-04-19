package com.ztu.cloud.cloud.controller.common;

import com.ztu.cloud.cloud.common.dto.user.download.Download;
import com.ztu.cloud.cloud.common.dto.user.download.DownloadId;
import com.ztu.cloud.cloud.common.log.SysLog;
import com.ztu.cloud.cloud.common.validation.Token;
import com.ztu.cloud.cloud.common.vo.ResultResponseEntity;
import com.ztu.cloud.cloud.service.common.DownloadService;
import com.ztu.cloud.cloud.util.TokenUtil;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
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
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");
            try {
                ServletOutputStream outputStream = response.getOutputStream();
                // TODO 改为JSON字符串
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
}
