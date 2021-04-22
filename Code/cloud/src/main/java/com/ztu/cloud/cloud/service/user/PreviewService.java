package com.ztu.cloud.cloud.service.user;

import com.ztu.cloud.cloud.common.dto.user.preview.PreviewFile;
import com.ztu.cloud.cloud.common.dto.user.preview.PreviewVideo;

/**
 * @author Jager
 * @description 预览文件业务接口
 * @date 2021/04/19-15:59
 **/
public interface PreviewService {
    /**
     * 获取预览图片
     *
     * @param token        用户Token
     * @param repositoryId 仓库ID
     * @param fileId       文件ID
     * @return 预览信息
     */
    PreviewFile previewUserPhoto(String token, String repositoryId, String fileId);

    /**
     * 获取预览视频
     *
     * @param repositoryId 仓库ID
     * @param fileId       文件ID
     * @return 预览信息
     */
    PreviewVideo previewUserVideo(String repositoryId, String fileId);
}
