package com.ztu.cloud.cloud.common.dto.user.download;

import com.ztu.cloud.cloud.common.bean.mongodb.inside.Folder;
import lombok.Data;

/**
 * @author Jager
 * @description 获取下载ID
 * @date 2020/06/27-10:14
 **/
@Data
public class DownloadId {
	private String repositoryId;
	private String shareId;
	private Long userFileId;
	private String fileName;
	private Folder folder;
}
