package com.ztu.cloud.cloud.service.user;

import com.ztu.cloud.cloud.common.bean.mysql.File;
import com.ztu.cloud.cloud.common.dao.mysql.FileMapper;
import com.ztu.cloud.cloud.common.dao.mysql.UserMapper;
import com.ztu.cloud.cloud.common.dto.user.preview.PreviewDocument;
import com.ztu.cloud.cloud.common.dto.user.preview.PreviewPhoto;
import com.ztu.cloud.cloud.common.dto.user.preview.PreviewVideo;
import com.ztu.cloud.cloud.util.StoreUtil;
import org.jodconverter.DocumentConverter;
import org.jodconverter.document.DefaultDocumentFormatRegistry;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * @author Jager
 * @description 文件预览业务实现
 * @date 2021/04/19-16:14
 **/
@Component
public class PreviewServiceImpl implements PreviewService {
    UserMapper userMapper;
    FileMapper fileMapper;
    StoreUtil storeUtil;
    List<String> photoTypeList;
    List<String> videoTypeList;
    List<String> documentTypeList;

    public PreviewServiceImpl(UserMapper userMapper, FileMapper fileMapper, StoreUtil storeUtil, DocumentConverter documentConverter) {
        this.userMapper = userMapper;
        this.fileMapper = fileMapper;
        this.storeUtil = storeUtil;
        //TODO 配置文件获取
        this.photoTypeList = Arrays.asList("jpg", "jpeg", "gif", "png");
        this.videoTypeList = Arrays.asList("mp4");
        this.documentTypeList = Arrays.asList("doc", "docx", "xlsx", "xls", "csv", "ppt", "pptx", "pdf", "txt");
    }

    /**
     * 获取预览文件
     *
     * @param token        用户Token
     * @param repositoryId 仓库ID
     * @param fileId       文件ID
     * @return 预览信息
     */
    @Override
    public PreviewPhoto previewUserPhoto(String token, String repositoryId, String fileId) {
        //TODO 用户身份验证
        //TODO 用户文件权限验证
        //TODO 文件状态验证
        PreviewPhoto previewPhoto = new PreviewPhoto();
        File file = this.fileMapper.getFileById(fileId);
        if (file == null) {
            previewPhoto.setCode(0);
            previewPhoto.setMessage("文件不存在");
            return previewPhoto;
        }
        if (!this.photoTypeList.contains(file.getType().toLowerCase())) {
            previewPhoto.setCode(0);
            previewPhoto.setMessage("暂不支持" + file.getType() + "格式文件预览");
            return previewPhoto;
        }
        InputStream inputStream = this.storeUtil.getFileInputStream(file.getPath());
        if (inputStream == null) {
            previewPhoto.setCode(0);
            previewPhoto.setMessage("预览异常");
            return previewPhoto;
        }
        previewPhoto.setInputStream(inputStream);
        return previewPhoto;
    }

    /**
     * 获取预览视频
     *
     * @param repositoryId 仓库ID
     * @param fileId       文件ID
     * @return 预览信息
     */
    @Override
    public PreviewVideo previewUserVideo(String repositoryId, String fileId) {
        //TODO 鉴权
        //TODO 用户身份验证
        //TODO 用户文件权限验证
        //TODO 文件状态验证
        PreviewVideo previewVideo = new PreviewVideo();
        File file = this.fileMapper.getFileById(fileId);
        if (file == null) {
            previewVideo.setCode(0);
            previewVideo.setMessage("文件不存在");
            return previewVideo;
        }
        if (!this.videoTypeList.contains(file.getType().toLowerCase())) {
            previewVideo.setCode(0);
            previewVideo.setMessage("暂不支持" + file.getType() + "格式文件预览");
            return previewVideo;
        }
        //查找临时文件是否存在
        java.io.File tempFile = this.storeUtil.getTempFile(file.getId());
        //更新文件最后访问时间
        this.storeUtil.flashTempFileLastModifiedTime(file.getId());
        //创建临时文件
        if (tempFile == null) {
            InputStream fileInputStream = this.storeUtil.getFileInputStream(file.getPath());
            this.storeUtil.storeTempFile(file.getId(), fileInputStream);
            tempFile = this.storeUtil.getTempFile(file.getId());
        }
        if (tempFile == null) {
            previewVideo.setCode(0);
            previewVideo.setMessage("预览异常");
            return previewVideo;
        }
        previewVideo.setFile(tempFile);
        return previewVideo;
    }

    /**
     * 获取预览文档
     *
     * @param token        用户Token
     * @param repositoryId 仓库ID
     * @param fileId       文件ID
     */
    @Override
    public PreviewDocument previewUserDocument(String token, String repositoryId, String fileId) {
        //TODO 用户身份验证
        //TODO 用户文件权限验证
        //TODO 文件状态验证
        PreviewDocument previewDocument = new PreviewDocument();
        File file = this.fileMapper.getFileById(fileId);
        if (file == null) {
            previewDocument.setCode(0);
            previewDocument.setMessage("文件不存在");
            return previewDocument;
        }
        if (!this.documentTypeList.contains(file.getType().toLowerCase())) {
            previewDocument.setCode(0);
            previewDocument.setMessage("暂不支持" + file.getType() + "格式文件预览");
            return previewDocument;
        }
        // 限制文件大小
        if (file.getSize() > 1024 * 1024 * 3 || ("txt".equals(file.getType().toLowerCase()) && file.getSize() > 1024 * 1024)) {
            previewDocument.setCode(0);
            previewDocument.setMessage("文件过大,暂不支持预览");
            return previewDocument;
        }
        switch (file.getType().toLowerCase()) {
            case "doc":
                previewDocument.setType(DefaultDocumentFormatRegistry.DOC);
                break;
            case "docx":
                previewDocument.setType(DefaultDocumentFormatRegistry.DOCX);
                break;
            case "xlsx":
                previewDocument.setType(DefaultDocumentFormatRegistry.XLSX);
                break;
            case "xls":
                previewDocument.setType(DefaultDocumentFormatRegistry.XLS);
                break;
            case "csv":
                previewDocument.setType(DefaultDocumentFormatRegistry.CSV);
                break;
            case "ppt":
                previewDocument.setType(DefaultDocumentFormatRegistry.PPT);
                break;
            case "pptx":
                previewDocument.setType(DefaultDocumentFormatRegistry.PPTX);
                break;
            case "pdf":
                previewDocument.setType(DefaultDocumentFormatRegistry.PDF);
                break;
            case "txt":
                previewDocument.setType(DefaultDocumentFormatRegistry.TXT);
                break;
            default:
                break;
        }
        InputStream inputStream = this.storeUtil.getFileInputStream(file.getPath());
        if (inputStream == null) {
            previewDocument.setCode(0);
            previewDocument.setMessage("预览异常");
            return previewDocument;
        }
        previewDocument.setInputStream(inputStream);
        return previewDocument;
    }
}
