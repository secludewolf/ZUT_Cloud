package com.ztu.cloud.cloud.common.vo.user;

import com.ztu.cloud.cloud.common.bean.mysql.Share;
import lombok.Data;

/**
 * @author Jager
 * @description 分享信息
 * @date 2020/06/28-10:50
 **/
@Data
public class ShareInfo {
	private Share share;

	public ShareInfo(Share share) {
		this.share = share;
	}
}
