package com.ztu.cloud.cloud.controller.user;

import com.google.gson.JsonObject;
import com.ztu.cloud.cloud.common.dto.user.preview.PreviewFile;
import com.ztu.cloud.cloud.common.log.SysLog;
import com.ztu.cloud.cloud.common.validation.Token;
import com.ztu.cloud.cloud.service.common.NonStaticResourceHttpRequestHandler;
import com.ztu.cloud.cloud.service.user.PreviewService;
import com.ztu.cloud.cloud.util.TokenUtil;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author Jager
 * @description 文件预览接口管理
 * @date 2021/04/18-15:35
 **/
@RestController
@RequestMapping("/preview")
@Validated
public class PreviewController {
    PreviewService previewService;
    NonStaticResourceHttpRequestHandler nonStaticResourceHttpRequestHandler;

    public PreviewController(PreviewService previewService, NonStaticResourceHttpRequestHandler nonStaticResourceHttpRequestHandler) {
        this.previewService = previewService;
        this.nonStaticResourceHttpRequestHandler = nonStaticResourceHttpRequestHandler;
    }

    /**
     * 获取预览图片
     *
     * @param token        用户Token
     * @param repositoryId 仓库ID
     * @param fileId       文件ID
     */
    @SysLog(descrption = "预览图片文件", type = "预览文件", modul = "用户模块")
    @GetMapping("/user/photo/{repositoryId}/{fileId}")
    public void previewUserPhoto(HttpServletResponse response,
                                 @RequestHeader(TokenUtil.TOKEN_HEADER) @Token(role = "user") String token,
                                 @PathVariable("repositoryId") @NotBlank(message = "仓库ID不能为空") String repositoryId,
                                 @PathVariable("fileId") @NotBlank(message = "文件ID不能为空") String fileId) {
        PreviewFile previewFile = this.previewService.previewUserPhoto(token, repositoryId, fileId);
        if (previewFile.getInputStream() == null) {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");
            try {
                ServletOutputStream outputStream = response.getOutputStream();
                JsonObject json = new JsonObject();
                json.addProperty("code", previewFile.getCode());
                json.addProperty("message", previewFile.getMessage());
                String content = json.toString();
                outputStream.write(content.getBytes());
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }
        response.setCharacterEncoding("UTF-8");
        response.setContentType("image/*");
        byte[] buffer = new byte[1024];
        try {
            OutputStream os = response.getOutputStream();
            int i = previewFile.getInputStream().read(buffer);
            while (i != -1) {
                os.write(buffer, 0, i);
                i = previewFile.getInputStream().read(buffer);
            }
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //TODO 鉴权
    /**
     * 获取预览视频
     *
     */
    @SysLog(descrption = "预览视频文件", type = "预览文件", modul = "用户模块")
    @GetMapping("/user/video/{repositoryId}/{fileId}")
    public void previewUserVideo(HttpServletRequest request, HttpServletResponse response) {
        String realPath = "C:/Users/18638/Desktop/sin/长江夜线20210126.mp4";
        Path filePath = Paths.get(realPath);
        try {
            String mimeType = Files.probeContentType(filePath);
            if (!StringUtils.isEmpty(mimeType)) {
                response.setContentType(mimeType);
            }
            request.setAttribute(NonStaticResourceHttpRequestHandler.ATTR_FILE, filePath);
            nonStaticResourceHttpRequestHandler.handleRequest(request, response);
        } catch (Exception e) {
//            e.printStackTrace();
        }
    }
}
