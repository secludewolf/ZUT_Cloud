package com.ztu.cloud.cloud.common.vo.user;

import com.ztu.cloud.cloud.common.bean.mongodb.CommonUserInform;
import lombok.Data;

/**
 * @author Jager
 * @description 通知信息
 * @date 2020/06/28-17:20
 **/
@Data
public class Inform {
	CommonUserInform inform;

	public Inform(CommonUserInform inform) {
		this.inform = inform;
	}
}
