package com.ztu.cloud.cloud.controller.user;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.ztu.cloud.cloud.common.dto.user.download.Download;
import com.ztu.cloud.cloud.common.dto.user.repository.PreviewFile;
import com.ztu.cloud.cloud.common.log.SysLog;
import com.ztu.cloud.cloud.common.validation.Token;
import com.ztu.cloud.cloud.common.vo.ResultResponseEntity;
import com.ztu.cloud.cloud.service.user.PreviewService;
import com.ztu.cloud.cloud.util.JsonUtil;
import com.ztu.cloud.cloud.util.TokenUtil;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import java.io.*;
import java.net.URLEncoder;
import java.util.Arrays;

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

    public PreviewController(PreviewService previewService) {
        this.previewService = previewService;
    }

    /**
     * 获取预览文件
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

}
