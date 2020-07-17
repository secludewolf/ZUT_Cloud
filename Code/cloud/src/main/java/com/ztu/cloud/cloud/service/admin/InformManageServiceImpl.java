package com.ztu.cloud.cloud.service.admin;

import com.ztu.cloud.cloud.common.bean.mongodb.AdminInform;
import com.ztu.cloud.cloud.common.bean.mongodb.CommonAdminInform;
import com.ztu.cloud.cloud.common.bean.mongodb.CommonUserInform;
import com.ztu.cloud.cloud.common.bean.mysql.Admin;
import com.ztu.cloud.cloud.common.constant.ResultConstant;
import com.ztu.cloud.cloud.common.dao.mongodb.AdminInformDao;
import com.ztu.cloud.cloud.common.dao.mongodb.CommonAdminInformDao;
import com.ztu.cloud.cloud.common.dao.mongodb.CommonUserInformDao;
import com.ztu.cloud.cloud.common.dao.mysql.AdminMapper;
import com.ztu.cloud.cloud.common.dto.admin.CreateInform;
import com.ztu.cloud.cloud.common.vo.ResultResponseEntity;
import com.ztu.cloud.cloud.common.vo.admin.Inform;
import com.ztu.cloud.cloud.common.vo.admin.InformList;
import com.ztu.cloud.cloud.util.RequestUtil;
import com.ztu.cloud.cloud.util.ResultUtil;
import com.ztu.cloud.cloud.util.TokenUtil;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;

/**
 * @author Jager
 * @description 通知管理接口实现
 * @date 2020/06/28-17:47
 **/
@Component
public class InformManageServiceImpl implements InformManageService {
	AdminMapper adminDao;
	CommonUserInformDao commonUserInformDao;
	CommonAdminInformDao commonAdminInformDao;
	AdminInformDao adminInformDao;

	public InformManageServiceImpl(AdminMapper adminDao, CommonUserInformDao commonUserInformDao, CommonAdminInformDao commonAdminInformDao, AdminInformDao adminInformDao) {
		this.adminDao = adminDao;
		this.commonUserInformDao = commonUserInformDao;
		this.commonAdminInformDao = commonAdminInformDao;
		this.adminInformDao = adminInformDao;
	}

	/**
	 * 创建用户通知
	 *
	 * @param token 管理员Token
	 * @param data  请求参数
	 *              header 标题
	 *              content 内容
	 *              validTime 有消息
	 * @return 通知信息
	 */
	@Override
	public ResultResponseEntity createUserInform(String token, String data) {
		if (!TokenUtil.isAdmin(token)) {
			return ResultConstant.TOKEN_INVALID;
		}
		CreateInform createInform = RequestUtil.getCreateInform(data);
		if (createInform == null) {
			return ResultConstant.REQUEST_PARAMETER_ERROR;
		}
		int id = TokenUtil.getId(token);
		Admin admin = this.adminDao.getAdminById(id);
		if (admin == null) {
			return ResultConstant.USER_NOT_FOUND;
		}
		if (admin.getStatus() != 1) {
			return ResultConstant.USER_STATUS_ABNORMAL;
		}
		//TODO 权限验证
		if (admin.getLevel() <= 0) {
			return ResultConstant.NO_ACCESS;
		}
		CommonUserInform inform = new CommonUserInform(createInform.getHeader(), createInform.getContent(), createInform.getValidTime(), id);
		this.commonUserInformDao.insert(inform);
		return ResultUtil.createResult("成功", new com.ztu.cloud.cloud.common.vo.user.Inform(inform));
	}

	/**
	 * 创建管理员通知
	 *
	 * @param token 管理员Token
	 * @param data  请求参数
	 *              header 标题
	 *              content 内容
	 *              validTime 有消息
	 * @return 通知信息
	 */
	@Override
	public ResultResponseEntity createAdminInform(String token, String data) {
		if (!TokenUtil.isAdmin(token)) {
			return ResultConstant.TOKEN_INVALID;
		}
		CreateInform createInform = RequestUtil.getCreateInform(data);
		if (createInform == null) {
			return ResultConstant.REQUEST_PARAMETER_ERROR;
		}
		int id = TokenUtil.getId(token);
		Admin admin = this.adminDao.getAdminById(id);
		if (admin == null) {
			return ResultConstant.USER_NOT_FOUND;
		}
		if (admin.getStatus() != 1) {
			return ResultConstant.USER_STATUS_ABNORMAL;
		}
		//TODO 权限验证
		if (admin.getLevel() <= 0) {
			return ResultConstant.NO_ACCESS;
		}
		CommonAdminInform inform = new CommonAdminInform(createInform.getHeader(), createInform.getContent(), createInform.getValidTime(), id);
		this.commonAdminInformDao.insert(inform);
		return ResultUtil.createResult("成功", new Inform(inform));
	}

	/**
	 * 获取通知列表
	 *
	 * @param token 管理员Token
	 * @return 通知列表
	 */
	@Override
	public ResultResponseEntity getInformList(String token) {
		if (!TokenUtil.isAdmin(token)) {
			return ResultConstant.TOKEN_INVALID;
		}
		int id = TokenUtil.getId(token);
		Admin admin = this.adminDao.getAdminById(id);
		if (admin == null) {
			return ResultConstant.USER_NOT_FOUND;
		}
		if (admin.getStatus() != 1) {
			return ResultConstant.USER_STATUS_ABNORMAL;
		}
		List<CommonAdminInform> validList = this.commonAdminInformDao.getValidList();
		AdminInform adminInform = this.adminInformDao.getById(id);
		if (adminInform == null) {
			adminInform = new AdminInform(id, new HashMap<>(10));
			this.adminInformDao.insert(adminInform);
			for (CommonAdminInform item : validList) {
				item.setStatus(0);
			}
		} else {
			for (CommonAdminInform item : validList) {
				if (adminInform.getStatus().get(item.getId()) == null) {
					item.setStatus(0);
				} else {
					item.setStatus(adminInform.getStatus().get(item.getId()));
				}
			}
		}
		return ResultUtil.createResult("成功", new InformList(validList));
	}

	/**
	 * 获取通知信息
	 *
	 * @param token    管理员Token
	 * @param informId 通知ID
	 * @return 通知信息
	 */
	@Override
	public ResultResponseEntity getInform(String token, String informId) {
		if (!TokenUtil.isAdmin(token)) {
			return ResultConstant.TOKEN_INVALID;
		}
		int id = TokenUtil.getId(token);
		Admin admin = this.adminDao.getAdminById(id);
		if (admin == null) {
			return ResultConstant.USER_NOT_FOUND;
		}
		if (admin.getStatus() != 1) {
			return ResultConstant.USER_STATUS_ABNORMAL;
		}
		CommonAdminInform inform = this.commonAdminInformDao.getById(informId);
		if (inform == null) {
			return ResultConstant.TARGET_NOT_EXISTED;
		}
		AdminInform adminInform = this.adminInformDao.getById(id);
		if (adminInform == null) {
			inform.setStatus(0);
		} else {
			if (adminInform.getStatus().get(informId) == null) {
				inform.setStatus(0);
			} else {
				inform.setStatus(adminInform.getStatus().get(informId));
			}
		}
		return ResultUtil.createResult("成功", new Inform(inform));
	}

	/**
	 * 修改通知状态
	 *
	 * @param token    管理员Token
	 * @param informId 通知ID
	 * @param status   通知状态
	 * @return 是否成功
	 */
	@Override
	public ResultResponseEntity changeInformStatus(String token, String informId, int status) {
		if (!TokenUtil.isAdmin(token)) {
			return ResultConstant.TOKEN_INVALID;
		}
		int id = TokenUtil.getId(token);
		Admin admin = this.adminDao.getAdminById(id);
		if (admin == null) {
			return ResultConstant.USER_NOT_FOUND;
		}
		if (admin.getStatus() != 1) {
			return ResultConstant.USER_STATUS_ABNORMAL;
		}
		CommonAdminInform inform = this.commonAdminInformDao.getById(informId);
		if (inform == null) {
			return ResultConstant.TARGET_NOT_EXISTED;
		}
		AdminInform adminInform = this.adminInformDao.getById(id);
		if (adminInform == null) {
			adminInform = new AdminInform(id, new HashMap<>(1));
			this.adminInformDao.insert(adminInform);
		}
		adminInform.getStatus().put(informId, status);
		this.adminInformDao.updateStatusById(id, adminInform.getStatus());
		return ResultUtil.createResult(1, "成功");
	}
}
