package com.ztu.cloud.cloud.controller.user;

import com.google.gson.JsonObject;
import com.ztu.cloud.cloud.common.dto.user.preview.PreviewDocument;
import com.ztu.cloud.cloud.common.dto.user.preview.PreviewPhoto;
import com.ztu.cloud.cloud.common.dto.user.preview.PreviewVideo;
import com.ztu.cloud.cloud.common.log.SysLog;
import com.ztu.cloud.cloud.common.validation.Token;
import com.ztu.cloud.cloud.service.common.NonStaticResourceHttpRequestHandler;
import com.ztu.cloud.cloud.service.user.PreviewService;
import com.ztu.cloud.cloud.util.TokenUtil;
import org.apache.commons.io.IOUtils;
import org.jodconverter.DocumentConverter;
import org.jodconverter.document.DefaultDocumentFormatRegistry;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import java.io.*;
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
    DocumentConverter documentConverter;

    public PreviewController(PreviewService previewService, NonStaticResourceHttpRequestHandler nonStaticResourceHttpRequestHandler, DocumentConverter documentConverter) {
        this.previewService = previewService;
        this.nonStaticResourceHttpRequestHandler = nonStaticResourceHttpRequestHandler;
        this.documentConverter = documentConverter;
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
        PreviewPhoto previewPhoto = this.previewService.previewUserPhoto(token, repositoryId, fileId);
        if (previewPhoto.getInputStream() == null) {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");
            try {
                ServletOutputStream outputStream = response.getOutputStream();
                JsonObject json = new JsonObject();
                json.addProperty("code", previewPhoto.getCode());
                json.addProperty("message", previewPhoto.getMessage());
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
            OutputStream outputStream = response.getOutputStream();
            int i = previewPhoto.getInputStream().read(buffer);
            while (i != -1) {
                outputStream.write(buffer, 0, i);
                i = previewPhoto.getInputStream().read(buffer);
            }
            previewPhoto.getInputStream().close();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取预览视频
     */
    @SysLog(descrption = "预览视频文件", type = "预览文件", modul = "用户模块")
    @GetMapping("/user/video/{repositoryId}/{fileId}")
    public void previewUserVideo(HttpServletRequest request, HttpServletResponse response,
                                 @PathVariable("repositoryId") @NotBlank(message = "仓库ID不能为空") String repositoryId,
                                 @PathVariable("fileId") @NotBlank(message = "文件ID不能为空") String fileId) {
        //TODO 鉴权
        PreviewVideo previewVideo = this.previewService.previewUserVideo(repositoryId, fileId);
        if (previewVideo.getFile() == null) {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");
            try {
                ServletOutputStream outputStream = response.getOutputStream();
                JsonObject json = new JsonObject();
                json.addProperty("code", previewVideo.getCode());
                json.addProperty("message", previewVideo.getMessage());
                String content = json.toString();
                outputStream.write(content.getBytes());
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }
        Path filePath = Paths.get(previewVideo.getFile().getAbsolutePath());
        try {
            String mimeType = Files.probeContentType(filePath);
            if (!StringUtils.isEmpty(mimeType)) {
                response.setContentType(mimeType);
            }
            request.setAttribute(NonStaticResourceHttpRequestHandler.ATTR_FILE, filePath);
            nonStaticResourceHttpRequestHandler.handleRequest(request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取预览文档
     *
     * @param token        用户Token
     * @param repositoryId 仓库ID
     * @param fileId       文件ID
     */
    @SysLog(descrption = "预览文档文件", type = "预览文件", modul = "用户模块")
    @GetMapping("/user/document/{repositoryId}/{fileId}")
    public void previewUserDocument(HttpServletResponse response,
                                    @RequestHeader(TokenUtil.TOKEN_HEADER) @Token(role = "user") String token,
                                    @PathVariable("repositoryId") @NotBlank(message = "仓库ID不能为空") String repositoryId,
                                    @PathVariable("fileId") @NotBlank(message = "文件ID不能为空") String fileId) {
        PreviewDocument previewDocument = this.previewService.previewUserDocument(token, repositoryId, fileId);
        if (previewDocument.getInputStream() == null) {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");
            try {
                ServletOutputStream outputStream = response.getOutputStream();
                JsonObject json = new JsonObject();
                json.addProperty("code", previewDocument.getCode());
                json.addProperty("message", previewDocument.getMessage());
                String content = json.toString();
                outputStream.write(content.getBytes());
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }
        try {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/pdf");
            ServletOutputStream outputStream = response.getOutputStream();
            if (previewDocument.getType().equals(DefaultDocumentFormatRegistry.PDF)) {
                IOUtils.copy(previewDocument.getInputStream(), outputStream);
                previewDocument.getInputStream().close();
                outputStream.close();
                return;
            }
            this.documentConverter
                    .convert(previewDocument.getInputStream(), true)
                    .as(previewDocument.getType())
                    .to(outputStream)
                    .as(DefaultDocumentFormatRegistry.PDF).execute();
        } catch (Exception e) {
            //TODO 返回错误信息
            e.printStackTrace();
        }
    }
}
