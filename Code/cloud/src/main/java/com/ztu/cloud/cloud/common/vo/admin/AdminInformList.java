package com.ztu.cloud.cloud.common.vo.admin;

import com.ztu.cloud.cloud.common.bean.mongodb.CommonAdminInform;
import lombok.Data;

import java.util.List;

/**
 * @author Jager
 * @description 管理员通知列表
 * @date 2020/8/8-16:09
 **/
@Data
public class AdminInformList {
	List<CommonAdminInform> informList;

	public AdminInformList(List<CommonAdminInform> informList) {
		this.informList = informList;
	}
}
