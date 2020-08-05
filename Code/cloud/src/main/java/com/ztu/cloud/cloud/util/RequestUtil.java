package com.ztu.cloud.cloud.util;

import com.ztu.cloud.cloud.common.dto.admin.*;
import com.ztu.cloud.cloud.common.dto.common.*;
import com.ztu.cloud.cloud.common.dto.user.download.DownloadId;
import com.ztu.cloud.cloud.common.dto.user.repository.*;
import com.ztu.cloud.cloud.common.dto.user.share.CreateShare;
import com.ztu.cloud.cloud.common.dto.user.share.GetShare;
import com.ztu.cloud.cloud.common.dto.user.share.SaveShare;
import com.ztu.cloud.cloud.common.dto.user.user.ChangeUserInfo;
import com.ztu.cloud.cloud.common.dto.user.user.RegisterAccount;
import com.ztu.cloud.cloud.common.dto.user.user.RegisterEmail;
import org.springframework.boot.configurationprocessor.json.JSONObject;

/**
 * @author Jager
 * @description 请求解析工具
 * @date 2020/06/23-17:47
 **/
public class RequestUtil {
	/**
	 * 解析邮箱登录请求数据
	 *
	 * @param data 请求数据
	 * @return DTO
	 */
	public static LoginEmail getLoginEmail(String data) {
		JSONObject json = JsonUtil.parseJson(data);
		if (json == null) {
			return null;
		}
		String email = JsonUtil.getString(json, "email");
		String password = JsonUtil.getString(json, "password");
		Boolean rememberMe = JsonUtil.getBoolean(json, "rememberMe");
		rememberMe = rememberMe == null ? false : rememberMe;
		if (email == null || password == null) {
			return null;
		}
		return new LoginEmail(email, password, rememberMe);
	}

	/**
	 * 解析账号登录请求数据
	 *
	 * @param data 请求数据
	 * @return DTO
	 */
	public static LoginAccount getLoginAccount(String data) {
		JSONObject json = JsonUtil.parseJson(data);
		if (json == null) {
			return null;
		}
		String account = JsonUtil.getString(json, "account");
		String password = JsonUtil.getString(json, "password");
		Boolean rememberMe = JsonUtil.getBoolean(json, "rememberMe");
		rememberMe = rememberMe == null ? false : rememberMe;
		if (account == null || password == null) {
			return null;
		}
		return new LoginAccount(account, password, rememberMe);
	}

	/**
	 * 解析忘记密码邮箱请求数据
	 *
	 * @param data 请求数据
	 * @return DTO
	 */
	public static ForgetEmail getForgetEmail(String data) {
		JSONObject json = JsonUtil.parseJson(data);
		if (json == null) {
			return null;
		}
		String email = JsonUtil.getString(json, "email");
		if (email == null) {
			return null;
		}
		return new ForgetEmail(email);
	}

	/**
	 * 解析邮箱注册请求数据
	 *
	 * @param data 请求数据
	 * @return DTO
	 */
	public static RegisterEmail getRegisterEmail(String data) {
		JSONObject json = JsonUtil.parseJson(data);
		if (json == null) {
			return null;
		}
		String name = JsonUtil.getString(json, "name");
		String account = JsonUtil.getString(json, "account");
		String email = JsonUtil.getString(json, "email");
		String password = JsonUtil.getString(json, "password");
		if (name == null || account == null || email == null || password == null) {
			return null;
		}
		return new RegisterEmail(name, account, email, password);
	}

	/**
	 * 解析账号注册请求数据
	 *
	 * @param data 请求数据
	 * @return DTO
	 */
	public static RegisterAccount getRegisterAccount(String data) {
		JSONObject json = JsonUtil.parseJson(data);
		if (json == null) {
			return null;
		}
		String name = JsonUtil.getString(json, "name");
		String account = JsonUtil.getString(json, "account");
		String password = JsonUtil.getString(json, "password");
		if (name == null || account == null || password == null) {
			return null;
		}
		return new RegisterAccount(name, account, password);
	}

	/**
	 * 解析账号注册请求数据
	 *
	 * @param data 请求数据
	 * @return DTO
	 */
	public static ChangeUserInfo getChangeUserInfo(String data) {
		JSONObject json;
		ChangeUserInfo changeUserInfo;
		json = JsonUtil.parseJson(data);
		if (json == null) {
			return null;
		}
		Integer id = JsonUtil.getInt(json, "id");
		String account = JsonUtil.getString(json, "account");
		String name = JsonUtil.getString(json, "name");
		if (id == null || account == null || name == null) {
			return null;
		}
		changeUserInfo = new ChangeUserInfo(id, account, name);
		changeUserInfo.setEmail(JsonUtil.getString(json, "email"));
		changeUserInfo.setPhone(JsonUtil.getString(json, "phone"));
		return changeUserInfo;
	}

	/**
	 * 解析账号注册请求数据
	 *
	 * @param data 请求数据
	 * @return DTO
	 */
	public static ChangePassword getChangePassword(String data) {
		JSONObject json = JsonUtil.parseJson(data);
		if (json == null) {
			return null;
		}
		Integer id = JsonUtil.getInt(json, "id");
		String oldPassword = JsonUtil.getString(json, "oldPassword");
		String newPassword = JsonUtil.getString(json, "newPassword");
		if (id == null || oldPassword == null || newPassword == null) {
			return null;
		}
		return new ChangePassword(id, oldPassword, newPassword);
	}

	/**
	 * 解析管理员注册请求数据
	 *
	 * @param data 请求数据
	 * @return DTO
	 */
	public static RegisterAdmin getRegisterAdmin(String data) {
		JSONObject json = JsonUtil.parseJson(data);
		if (json == null) {
			return null;
		}
		String name = JsonUtil.getString(json, "name");
		String account = JsonUtil.getString(json, "account");
		String email = JsonUtil.getString(json, "email");
		String phone = JsonUtil.getString(json, "phone");
		String password = JsonUtil.getString(json, "password");
		String key = JsonUtil.getString(json, "key");
		if (name == null || account == null || email == null || phone == null || password == null || key == null) {
			return null;
		}
		return new RegisterAdmin(name, account, email, phone, password, key);
	}

	/**
	 * 创建用户
	 *
	 * @param data 请求数据
	 * @return DTO
	 */
	public static CreateUser getCreateUser(String data) {
		JSONObject json = JsonUtil.parseJson(data);
		if (json == null) {
			return null;
		}
		String account = JsonUtil.getString(json, "account");
		String password = JsonUtil.getString(json, "password");
		String name = JsonUtil.getString(json, "name");
		if (account == null || password == null || name == null) {
			return null;
		}
		CreateUser createUser = new CreateUser(account, password, name);
		createUser.setEmail(JsonUtil.getString(json, "email"));
		createUser.setPhone(JsonUtil.getString(json, "phone"));
		return createUser;
	}

	public static CreateAdmin getCreateAdmin(String data) {
		JSONObject json = JsonUtil.parseJson(data);
		if (json == null) {
			return null;
		}
		String name = JsonUtil.getString(json, "name");
		String account = JsonUtil.getString(json, "account");
		String email = JsonUtil.getString(json, "email");
		String phone = JsonUtil.getString(json, "phone");
		String password = JsonUtil.getString(json, "password");
		String key = JsonUtil.getString(json, "key");
		if (name == null || account == null || email == null || phone == null || password == null || key == null) {
			return null;
		}
		return new CreateAdmin(name, account, email, phone, password, key);
	}

	public static DeleteUser getDeleteUser(String data) {
		JSONObject json = JsonUtil.parseJson(data);
		if (json == null) {
			return null;
		}
		DeleteUser deleteUser = new DeleteUser();
		Integer id = JsonUtil.getInt(json, "id");
		if (id != null) {
			deleteUser.setId(id);
			return deleteUser;
		}
		String account = JsonUtil.getString(json, "account");
		return null;
	}

	public static DeleteAdmin getDeleteAdmin(String data) {
		JSONObject json = JsonUtil.parseJson(data);
		if (json == null) {
			return null;
		}
		DeleteAdmin deleteAdmin = new DeleteAdmin();
		Integer id = JsonUtil.getInt(json, "id");
		if (id != null) {
			deleteAdmin.setId(id);
			return deleteAdmin;
		}
		String account = JsonUtil.getString(json, "account");
		return null;
	}

	public static ChangeUserInfoManage getChangeUserInfoManage(String data) {
		JSONObject json = JsonUtil.parseJson(data);
		if (json == null) {
			return null;
		}
		Integer id = JsonUtil.getInt(json, "id");
		String password = JsonUtil.getString(json, "account");
		String account = JsonUtil.getString(json, "password");
		String name = JsonUtil.getString(json, "name");
		Integer status = JsonUtil.getInt(json, "status");
		Integer level = JsonUtil.getInt(json, "level");
		Long repoSize = JsonUtil.getLong(json, "repoSize");
		if (id == null || password == null || account == null || name == null || status == null || level == null || repoSize == null) {
			return null;
		}
		ChangeUserInfoManage changeUserInfoManage = new ChangeUserInfoManage(id, account, password, name, status, level, repoSize);
		changeUserInfoManage.setEmail(JsonUtil.getString(json, "email"));
		changeUserInfoManage.setPhone(JsonUtil.getString(json, "phone"));
		return changeUserInfoManage;
	}

	public static ChangeAdminInfoManage getChangeAdminInfoManage(String data) {
		JSONObject json = JsonUtil.parseJson(data);
		if (json == null) {
			return null;
		}
		Integer id = JsonUtil.getInt(json, "id");
		String password = JsonUtil.getString(json, "password");
		String account = JsonUtil.getString(json, "account");
		String email = JsonUtil.getString(json, "email");
		String phone = JsonUtil.getString(json, "phone");
		String name = JsonUtil.getString(json, "name");
		Integer status = JsonUtil.getInt(json, "status");
		Integer level = JsonUtil.getInt(json, "level");
		Long repoSize = JsonUtil.getLong(json, "repoSize");
		if (id == null || password == null || account == null || email == null || phone == null || name == null || status == null || level == null || repoSize == null) {
			return null;
		}
		ChangeAdminInfoManage changeAdminInfoManage = new ChangeAdminInfoManage(id, account, password, email, phone, name, status, level);
		return changeAdminInfoManage;
	}

	public static CreateFile getCreateFile(String data) {
		JSONObject json = JsonUtil.parseJson(data);
		if (json == null) {
			return null;
		}
		String repositoryId = JsonUtil.getString(json, "repositoryId");
		String fileId = JsonUtil.getString(json, "fileId");
		String name = JsonUtil.getString(json, "name");
		String path = JsonUtil.getString(json, "path");
		if (repositoryId == null || fileId == null || name == null || path == null) {
			return null;
		}
		return new CreateFile(repositoryId, fileId, name, path);
	}

	public static CreateFolder getCreateFolder(String data) {
		JSONObject json = JsonUtil.parseJson(data);
		if (json == null) {
			return null;
		}
		String repositoryId = JsonUtil.getString(json, "repositoryId");
		String name = JsonUtil.getString(json, "name");
		String path = JsonUtil.getString(json, "path");
		if (repositoryId == null || name == null || path == null) {
			return null;
		}
		return new CreateFolder(repositoryId, name, path);
	}

	public static MoveFile getMoveFile(String data) {
		JSONObject json = JsonUtil.parseJson(data);
		if (json == null) {
			return null;
		}
		String repositoryId = JsonUtil.getString(json, "repositoryId");
		String name = JsonUtil.getString(json, "name");
		String oldPath = JsonUtil.getString(json, "oldPath");
		String newPath = JsonUtil.getString(json, "newPath");
		if (repositoryId == null || name == null || oldPath == null || newPath == null) {
			return null;
		}
		return new MoveFile(repositoryId, name, oldPath, newPath);
	}


	public static MoveFolder getMoveFolder(String data) {
		JSONObject json = JsonUtil.parseJson(data);
		if (json == null) {
			return null;
		}
		String repositoryId = JsonUtil.getString(json, "repositoryId");
		String name = JsonUtil.getString(json, "name");
		String oldPath = JsonUtil.getString(json, "oldPath");
		String newPath = JsonUtil.getString(json, "newPath");
		if (repositoryId == null || name == null || oldPath == null || newPath == null) {
			return null;
		}
		return new MoveFolder(repositoryId, name, oldPath, newPath);
	}

	public static CopyFile getCopyFile(String data) {
		JSONObject json = JsonUtil.parseJson(data);
		if (json == null) {
			return null;
		}
		String repositoryId = JsonUtil.getString(json, "repositoryId");
		String name = JsonUtil.getString(json, "name");
		String oldPath = JsonUtil.getString(json, "oldPath");
		String newPath = JsonUtil.getString(json, "newPath");
		if (repositoryId == null || name == null || oldPath == null || newPath == null) {
			return null;
		}
		return new CopyFile(repositoryId, name, oldPath, newPath);
	}


	public static CopyFolder getCopyFolder(String data) {
		JSONObject json = JsonUtil.parseJson(data);
		if (json == null) {
			return null;
		}
		String repositoryId = JsonUtil.getString(json, "repositoryId");
		String name = JsonUtil.getString(json, "name");
		String oldPath = JsonUtil.getString(json, "oldPath");
		String newPath = JsonUtil.getString(json, "newPath");
		if (repositoryId == null || name == null || oldPath == null || newPath == null) {
			return null;
		}
		return new CopyFolder(repositoryId, name, oldPath, newPath);
	}

	public static RenameFile getRenameFile(String data) {
		JSONObject json = JsonUtil.parseJson(data);
		if (json == null) {
			return null;
		}
		String repositoryId = JsonUtil.getString(json, "repositoryId");
		String oldName = JsonUtil.getString(json, "oldName");
		String newName = JsonUtil.getString(json, "newName");
		String path = JsonUtil.getString(json, "path");
		if (repositoryId == null || oldName == null || newName == null || path == null) {
			return null;
		}
		return new RenameFile(repositoryId, oldName, newName, path);
	}


	public static RenameFolder getRenameFolder(String data) {
		JSONObject json = JsonUtil.parseJson(data);
		if (json == null) {
			return null;
		}
		String repositoryId = JsonUtil.getString(json, "repositoryId");
		String oldName = JsonUtil.getString(json, "oldName");
		String newName = JsonUtil.getString(json, "newName");
		String path = JsonUtil.getString(json, "path");
		if (repositoryId == null || oldName == null || newName == null || path == null) {
			return null;
		}
		return new RenameFolder(repositoryId, oldName, newName, path);
	}

	public static DeleteFileToRecyclebin getDeleteFileToRecyclebin(String data) {
		JSONObject json = JsonUtil.parseJson(data);
		if (json == null) {
			return null;
		}
		String repositoryId = JsonUtil.getString(json, "repositoryId");
		Boolean isFile = JsonUtil.getBoolean(json, "isFile");
		String name = JsonUtil.getString(json, "name");
		String path = JsonUtil.getString(json, "path");
		if (repositoryId == null || isFile == null || name == null || path == null) {
			return null;
		}
		return new DeleteFileToRecyclebin(repositoryId, isFile, name, path);
	}

	public static RestoreFile getDeleteRestoreFile(String data) {
		JSONObject json = JsonUtil.parseJson(data);
		if (json == null) {
			return null;
		}
		String repositoryId = JsonUtil.getString(json, "repositoryId");
		Boolean isFile = JsonUtil.getBoolean(json, "isFile");
		String recycleId = JsonUtil.getString(json, "recycleId");
		if (repositoryId == null || isFile == null || recycleId == null) {
			return null;
		}
		return new RestoreFile(repositoryId, isFile, recycleId);
	}

	public static DeleteFileFromRecyclebin getDeleteFileFromRecyclebin(String data) {
		JSONObject json = JsonUtil.parseJson(data);
		if (json == null) {
			return null;
		}
		String repositoryId = JsonUtil.getString(json, "repositoryId");
		Boolean isFile = JsonUtil.getBoolean(json, "isFile");
		String recycleId = JsonUtil.getString(json, "recycleId");
		if (repositoryId == null || isFile == null || recycleId == null) {
			return null;
		}
		return new DeleteFileFromRecyclebin(repositoryId, isFile, recycleId);
	}

	public static CleanRecyclebin getCleanRecyclebin(String data) {
		JSONObject json = JsonUtil.parseJson(data);
		if (json == null) {
			return null;
		}
		String repositoryId = JsonUtil.getString(json, "repositoryId");
		if (repositoryId == null) {
			return null;
		}
		return new CleanRecyclebin(repositoryId);
	}

	public static DownloadId getDownloadId(String data) {
		JSONObject json = JsonUtil.parseJson(data);
		if (json == null) {
			return null;
		}
		DownloadId downloadId = new DownloadId();
		downloadId.setRepositoryId(JsonUtil.getString(json, "repositoryId"));
		downloadId.setShareId(JsonUtil.getString(json, "shareId"));
		downloadId.setUserFileId(JsonUtil.getLong(json, "userFileId"));
		downloadId.setFileName(JsonUtil.getString(json, "fileName"));
		//TODO 多文件分享
		downloadId.setFolder(null);
		return downloadId;
	}

	public static CreateShare getCreateShare(String data) {
		JSONObject json = JsonUtil.parseJson(data);
		if (json == null) {
			return null;
		}
		String repositoryId = JsonUtil.getString(json, "repositoryId");
		String name = JsonUtil.getString(json, "name");
		String path = JsonUtil.getString(json, "path");
		Long validTime = JsonUtil.getLong(json, "validTime");
		if (repositoryId == null || name == null || path == null || validTime == null) {
			return null;
		}
		CreateShare createShare = new CreateShare(repositoryId, name, path, validTime);
		createShare.setPassword(JsonUtil.getString(json, "password"));
		return createShare;
	}

	public static GetShare getShare(String data) {
		JSONObject json = JsonUtil.parseJson(data);
		if (json == null) {
			return null;
		}
		String shareId = JsonUtil.getString(json, "shareId");
		if (shareId == null) {
			return null;
		}
		GetShare getShare = new GetShare(shareId);
		getShare.setPassword(JsonUtil.getString(json, "password"));
		return getShare;
	}

	public static SaveShare getSaveShare(String data) {
		JSONObject json = JsonUtil.parseJson(data);
		if (json == null) {
			return null;
		}
		String shareId = JsonUtil.getString(json, "shareId");
		String path = JsonUtil.getString(json, "path");
		if (shareId == null || path == null) {
			return null;
		}
		SaveShare saveShare = new SaveShare(shareId, path);
		saveShare.setPassword(JsonUtil.getString(json, "password"));
		return saveShare;
	}

	public static CreateInform getCreateInform(String data) {
		JSONObject json = JsonUtil.parseJson(data);
		if (json == null) {
			return null;
		}
		String header = JsonUtil.getString(json, "header");
		String content = JsonUtil.getString(json, "content");
		Long validTime = JsonUtil.getLong(json, "validTime");
		if (header == null || content == null || validTime == null) {
			return null;
		}
		return new CreateInform(header, content, validTime);
	}
}
