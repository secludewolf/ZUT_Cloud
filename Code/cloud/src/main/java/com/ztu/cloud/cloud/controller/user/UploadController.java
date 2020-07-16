package com.ztu.cloud.cloud.controller.user;

import com.ztu.cloud.cloud.common.vo.ResultResponseEntity;
import com.ztu.cloud.cloud.service.user.UploadService;
import com.ztu.cloud.cloud.util.TokenUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
    public ResultResponseEntity upload(@RequestHeader(TokenUtil.TOKEN_HEADER) String token, @RequestParam("block") MultipartFile block, @RequestParam("fileName") String fileName, @RequestParam("blockMd5") String blockMd5, @RequestParam("fileMd5") String fileMd5, @RequestParam("index") Integer index, @RequestParam("length") Integer length) {
        return uploadService.upload(token, fileName, block, blockMd5, fileMd5, index, length);
    }
}
