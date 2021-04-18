package com.ztu.cloud.cloud.controller.user;

import com.ztu.cloud.cloud.common.dto.user.download.Download;
import com.ztu.cloud.cloud.common.log.SysLog;
import com.ztu.cloud.cloud.common.validation.Token;
import com.ztu.cloud.cloud.common.vo.ResultResponseEntity;
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


    /**
     * 获取预览文件
     *
     * @param token        用户Token
     * @param repositoryId 仓库ID
     */
    @SysLog(descrption = "预览图片文件", type = "预览文件", modul = "用户模块")
    @GetMapping("/photo/{repositoryId}/{fileId}")
    public void previewPhoto(HttpServletResponse response,
                             @RequestHeader(TokenUtil.TOKEN_HEADER) @Token(role = "user") String token,
                             @PathVariable("repositoryId") @NotBlank(message = "仓库ID不能为空") String repositoryId,
                             @PathVariable("fileId") @NotBlank(message = "文件ID不能为空") String fileId) {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("image/*");
        byte[] buffer = new byte[1024];
        try {
            OutputStream os = response.getOutputStream();
            File testFile = new File("C:/Users/18638/Desktop/logo.png");
            InputStream testInputStream = new FileInputStream(testFile);
            int i = testInputStream.read(buffer);
            while (i != -1) {
                os.write(buffer, 0, i);
                i = testInputStream.read(buffer);
            }
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
