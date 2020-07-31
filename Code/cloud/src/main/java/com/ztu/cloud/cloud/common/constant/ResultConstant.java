package com.ztu.cloud.cloud.common.constant;

import com.ztu.cloud.cloud.common.vo.ResultResponseEntity;
import com.ztu.cloud.cloud.util.ResultUtil;
import org.springframework.http.HttpStatus;

/**
 * @author Jager
 * @description 重用返回体
 * @date 2020/06/23-12:10
 **/
public class ResultConstant {
	public final static ResultResponseEntity SERVER_ERROR = ResultUtil.createResult(HttpStatus.INTERNAL_SERVER_ERROR, 0, "服务器错误", null);
	public final static ResultResponseEntity SUCCESS = ResultUtil.createResult(1, "成功");
	public final static ResultResponseEntity REQUEST_PARAMETER_ERROR = ResultUtil.createResult(0, "请求参数错误");
	public final static ResultResponseEntity NO_ACCESS = ResultUtil.createResult(-1, "无访问权限");
	public final static ResultResponseEntity TOKEN_INVALID = ResultUtil.createResult(-2, "Token无效");
	public final static ResultResponseEntity USER_NOT_FOUND = ResultUtil.createResult(-3, "用户不存在");
	public final static ResultResponseEntity USER_STATUS_ABNORMAL = ResultUtil.createResult(-4, "用户状态异常");
	public final static ResultResponseEntity REPOSITORY_NOT_FOUND = ResultUtil.createResult(-5, "仓库不存在");
	public final static ResultResponseEntity WRONG_PASSWORD = ResultUtil.createResult(-6, "密码错误");
	public final static ResultResponseEntity PASSWORD_INVALID = ResultUtil.createResult(-7, "密码不合法");
	public final static ResultResponseEntity EMAIL_EXISTED = ResultUtil.createResult(-8, "邮箱已存在");
	public final static ResultResponseEntity PHONE_EXISTED = ResultUtil.createResult(-9, "手机已存在");
	public final static ResultResponseEntity ACCOUNT_EXISTED = ResultUtil.createResult(-10, "账号已存在");
	public final static ResultResponseEntity NAME_INVALID = ResultUtil.createResult(-11, "名称不合法");
	public final static ResultResponseEntity EMAIL_INVALID = ResultUtil.createResult(-12, "邮箱不合法");
	public final static ResultResponseEntity PHONE_INVALID = ResultUtil.createResult(-13, "手机不合法");
	public final static ResultResponseEntity TARGET_NOT_EXISTED = ResultUtil.createResult(-14, "目标不存在");
	public final static ResultResponseEntity INVALID_KEY = ResultUtil.createResult(-15, "无效授权码");
	public final static ResultResponseEntity REPOSITORY_STATUS_ABNORMAL = ResultUtil.createResult(-16, "仓库状态异常");
	public final static ResultResponseEntity FILE_NOT_EXISTED = ResultUtil.createResult(-17, "文件不存在");
	public final static ResultResponseEntity FOLDER_NOT_EXISTED = ResultUtil.createResult(-18, "文件夹不存在");
	public final static ResultResponseEntity FILE_EXISTED = ResultUtil.createResult(-19, "文件已存在");
	public final static ResultResponseEntity FOLDER_EXISTED = ResultUtil.createResult(-20, "文件夹已存在");
	public final static ResultResponseEntity REPOSITORY_FULL = ResultUtil.createResult(-21, "仓库空间不足");
	public final static ResultResponseEntity PATH_INVALID = ResultUtil.createResult(-22, "文件嵌套");
	public final static ResultResponseEntity FILE_DAMAGE = ResultUtil.createResult(-23, "文件损坏");
	public final static ResultResponseEntity SHARE_INVALID = ResultUtil.createResult(-24, "分享失效");
	public final static ResultResponseEntity FOLDER_DEPTH_TOO_BIG = ResultUtil.createResult(-25, "文件深度过大");
}
