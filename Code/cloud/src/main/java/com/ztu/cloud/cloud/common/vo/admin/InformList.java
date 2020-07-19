package com.ztu.cloud.cloud.common.vo.admin;

import com.ztu.cloud.cloud.common.bean.mongodb.CommonAdminInform;
import lombok.Data;

import java.util.List;

/**
 * @author Jager
 * @description 通知列表
 * @date 2020/06/28-17:14
 **/
@Data
public class InformList {
	List<CommonAdminInform> informList;

	public InformList(List<CommonAdminInform> informList) {
		this.informList = informList;
	}
}
