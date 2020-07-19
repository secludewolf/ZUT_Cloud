package com.ztu.cloud.cloud.util;

import com.ztu.cloud.cloud.common.dto.admin.*;
import com.ztu.cloud.cloud.common.dto.common.*;
import com.ztu.cloud.cloud.common.dto.user.download.DownloadId;
import com.ztu.cloud.cloud.common.dto.user.repository.*;
import com.ztu.cloud.cloud.common.dto.user.share.CreateShare;
import com.ztu.cloud.cloud.common.dto.user.share.GetShare;
import com.ztu.cloud.cloud.common.dto.user.share.SaveShare;
import com.ztu.cloud.cloud.common.dto.user.user.RegisterAccount;
import com.ztu.cloud.cloud.common.dto.user.user.RegisterEmail;
import org.springframework.boot.configurationprocessor.json.JSONException;
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
		try {
			JSONObject json = JsonUtil.parseJson(data);
			return new LoginEmail(
					JsonUtil.getString(json, "email"),
					JsonUtil.getString(json, "password"));
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 解析账号登录请求数据
	 *
	 * @param data 请求数据
	 * @return DTO
	 */
	public static LoginAccount getLoginAccount(String data) {
		try {
			JSONObject json = JsonUtil.parseJson(data);
			return new LoginAccount(
					JsonUtil.getString(json, "account"),
					JsonUtil.getString(json, "password"));
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 解析忘记密码邮箱请求数据
	 *
	 * @param data 请求数据
	 * @return DTO
	 */
	public static ForgetEmail getForgetEmail(String data) {
		try {
			JSONObject json = JsonUtil.parseJson(data);
			return new ForgetEmail(
					JsonUtil.getString(json, "email"));
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 解析邮箱注册请求数据
	 *
	 * @param data 请求数据
	 * @return DTO
	 */
	public static RegisterEmail getRegisterEmail(String data) {
		try {
			JSONObject json = JsonUtil.parseJson(data);
			return new RegisterEmail(
					JsonUtil.getString(json, "name"),
					JsonUtil.getString(json, "account"),
					JsonUtil.getString(json, "email"),
					JsonUtil.getString(json, "password"));
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 解析账号注册请求数据
	 *
	 * @param data 请求数据
	 * @return DTO
	 */
	public static RegisterAccount getRegisterAccount(String data) {
		try {
			JSONObject json = JsonUtil.parseJson(data);
			return new RegisterAccount(
					JsonUtil.getString(json, "name"),
					JsonUtil.getString(json, "account"),
					JsonUtil.getString(json, "password"));
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
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
		try {
			json = JsonUtil.parseJson(data);
			changeUserInfo = new ChangeUserInfo(
					JsonUtil.getInt(json, "id"),
					JsonUtil.getString(json, "account"),
					JsonUtil.getString(json, "name"));
			System.out.println(1);
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
		try {
			changeUserInfo.setEmail(JsonUtil.getString(json, "email"));
		} catch (JSONException e) {
			changeUserInfo.setEmail(null);
		}
		try {
			changeUserInfo.setPhone(JsonUtil.getString(json, "phone"));
		} catch (JSONException e) {
			changeUserInfo.setPhone(null);
		}
		return changeUserInfo;
	}

	/**
	 * 解析账号注册请求数据
	 *
	 * @param data 请求数据
	 * @return DTO
	 */
	public static ChangePassword getChangePassword(String data) {
		try {
			JSONObject json = JsonUtil.parseJson(data);
			return new ChangePassword(
					JsonUtil.getInt(json, "id"),
					JsonUtil.getString(json, "oldPassword"),
					JsonUtil.getString(json, "newPassword"));
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 解析管理员注册请求数据
	 *
	 * @param data 请求数据
	 * @return DTO
	 */
	public static RegisterAdmin getRegisterAdmin(String data) {
		try {
			JSONObject json = JsonUtil.parseJson(data);
			return new RegisterAdmin(
					JsonUtil.getString(json, "name"),
					JsonUtil.getString(json, "account"),
					JsonUtil.getString(json, "email"),
					JsonUtil.getString(json, "phone"),
					JsonUtil.getString(json, "password"),
					JsonUtil.getString(json, "key"));
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 创建用户
	 *
	 * @param data 请求数据
	 * @return DTO
	 */
	public static CreateUser getCreateUser(String data) {
		JSONObject json;
		CreateUser createUser;
		try {
			json = JsonUtil.parseJson(data);
			createUser = new CreateUser(
					JsonUtil.getString(json, "account"),
					JsonUtil.getString(json, "password"),
					JsonUtil.getString(json, "name"));
			System.out.println(1);
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
		try {
			createUser.setEmail(JsonUtil.getString(json, "email"));
		} catch (JSONException e) {
			createUser.setEmail(null);
		}
		try {
			createUser.setPhone(JsonUtil.getString(json, "phone"));
		} catch (JSONException e) {
			createUser.setPhone(null);
		}
		return createUser;
	}

	public static CreateAdmin getCreateAdmin(String data) {
		try {
			JSONObject json = JsonUtil.parseJson(data);
			return new CreateAdmin(
					JsonUtil.getString(json, "name"),
					JsonUtil.getString(json, "account"),
					JsonUtil.getString(json, "email"),
					JsonUtil.getString(json, "phone"),
					JsonUtil.getString(json, "password"),
					JsonUtil.getString(json, "key"));
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static DeleteUser getDeleteUser(String data) {
		JSONObject json;
		DeleteUser deleteUser = new DeleteUser();
		try {
			json = JsonUtil.parseJson(data);
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
		try {
			deleteUser.setId(JsonUtil.getInt(json, "id"));
			return deleteUser;
		} catch (JSONException e) {
			deleteUser.setId(0);
		}
		try {
			deleteUser.setAccount(JsonUtil.getString(json, "account"));
			return deleteUser;
		} catch (JSONException e) {
			deleteUser.setAccount(null);
		}
		try {
			deleteUser.setEmail(JsonUtil.getString(json, "email"));
			return deleteUser;
		} catch (JSONException e) {
			deleteUser.setEmail(null);
		}
		try {
			deleteUser.setPhone(JsonUtil.getString(json, "phone"));
			return deleteUser;
		} catch (JSONException e) {
			deleteUser.setPhone(null);
		}
		return null;
	}

	public static DeleteAdmin getDeleteAdmin(String data) {
		JSONObject json;
		DeleteAdmin deleteAdmin = new DeleteAdmin();
		try {
			json = JsonUtil.parseJson(data);
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
		try {
			deleteAdmin.setId(JsonUtil.getInt(json, "id"));
			return deleteAdmin;
		} catch (JSONException e) {
			deleteAdmin.setId(0);
		}
		try {
			deleteAdmin.setAccount(JsonUtil.getString(json, "account"));
			return deleteAdmin;
		} catch (JSONException e) {
			deleteAdmin.setAccount(null);
		}
		try {
			deleteAdmin.setEmail(JsonUtil.getString(json, "email"));
			return deleteAdmin;
		} catch (JSONException e) {
			deleteAdmin.setEmail(null);
		}
		try {
			deleteAdmin.setPhone(JsonUtil.getString(json, "phone"));
			return deleteAdmin;
		} catch (JSONException e) {
			deleteAdmin.setPhone(null);
		}
		return null;
	}

	public static ChangeUserInfoManage getChangeUserInfoManage(String data) {
		JSONObject json;
		ChangeUserInfoManage changeUserInfoManage;
		try {
			json = JsonUtil.parseJson(data);
			changeUserInfoManage = new ChangeUserInfoManage(
					JsonUtil.getInt(json, "id"),
					JsonUtil.getString(json, "password"),
					JsonUtil.getString(json, "account"),
					JsonUtil.getString(json, "name"),
					JsonUtil.getInt(json, "status"),
					JsonUtil.getInt(json, "level"),
					JsonUtil.getLong(json, "repoSize"));
			System.out.println(1);
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
		try {
			changeUserInfoManage.setEmail(JsonUtil.getString(json, "email"));
		} catch (JSONException e) {
			changeUserInfoManage.setEmail(null);
		}
		try {
			changeUserInfoManage.setPhone(JsonUtil.getString(json, "phone"));
		} catch (JSONException e) {
			changeUserInfoManage.setPhone(null);
		}
		return changeUserInfoManage;
	}

	public static ChangeAdminInfoManage getChangeAdminInfoManage(String data) {
		JSONObject json;
		ChangeAdminInfoManage changeAdminInfoManage;
		try {
			json = JsonUtil.parseJson(data);
			changeAdminInfoManage = new ChangeAdminInfoManage(
					JsonUtil.getInt(json, "id"),
					JsonUtil.getString(json, "password"),
					JsonUtil.getString(json, "account"),
					JsonUtil.getString(json, "email"),
					JsonUtil.getString(json, "phone"),
					JsonUtil.getString(json, "name"),
					JsonUtil.getInt(json, "status"),
					JsonUtil.getInt(json, "level"));
			System.out.println(1);
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
		return changeAdminInfoManage;
	}

	public static CreateFile getCreateFile(String data) {
		try {
			JSONObject json = JsonUtil.parseJson(data);
			return new CreateFile(
					JsonUtil.getString(json, "repositoryId"),
					JsonUtil.getString(json, "fileId"),
					JsonUtil.getString(json, "name"),
					JsonUtil.getString(json, "path"));
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static CreateFolder getCreateFolder(String data) {
		try {
			JSONObject json = JsonUtil.parseJson(data);
			return new CreateFolder(
					JsonUtil.getString(json, "repositoryId"),
					JsonUtil.getString(json, "name"),
					JsonUtil.getString(json, "path"));
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static MoveFile getMoveFile(String data) {
		try {
			JSONObject json = JsonUtil.parseJson(data);
			return new MoveFile(
					JsonUtil.getString(json, "repositoryId"),
					JsonUtil.getString(json, "name"),
					JsonUtil.getString(json, "oldPath"),
					JsonUtil.getString(json, "newPath"));
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}


	public static MoveFolder getMoveFolder(String data) {
		try {
			JSONObject json = JsonUtil.parseJson(data);
			return new MoveFolder(
					JsonUtil.getString(json, "repositoryId"),
					JsonUtil.getString(json, "name"),
					JsonUtil.getString(json, "oldPath"),
					JsonUtil.getString(json, "newPath"));
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static CopyFile getCopyFile(String data) {
		try {
			JSONObject json = JsonUtil.parseJson(data);
			return new CopyFile(
					JsonUtil.getString(json, "repositoryId"),
					JsonUtil.getString(json, "name"),
					JsonUtil.getString(json, "oldPath"),
					JsonUtil.getString(json, "newPath"));
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}


	public static CopyFolder getCopyFolder(String data) {
		try {
			JSONObject json = JsonUtil.parseJson(data);
			return new CopyFolder(
					JsonUtil.getString(json, "repositoryId"),
					JsonUtil.getString(json, "name"),
					JsonUtil.getString(json, "oldPath"),
					JsonUtil.getString(json, "newPath"));
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static RenameFile getRenameFile(String data) {
		try {
			JSONObject json = JsonUtil.parseJson(data);
			return new RenameFile(
					JsonUtil.getString(json, "repositoryId"),
					JsonUtil.getString(json, "oldName"),
					JsonUtil.getString(json, "newName"),
					JsonUtil.getString(json, "path"));
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}


	public static RenameFolder getRenameFolder(String data) {
		try {
			JSONObject json = JsonUtil.parseJson(data);
			return new RenameFolder(
					JsonUtil.getString(json, "repositoryId"),
					JsonUtil.getString(json, "oldName"),
					JsonUtil.getString(json, "newName"),
					JsonUtil.getString(json, "path"));
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static DeleteFileToRecyclebin getDeleteFileToRecyclebin(String data) {
		try {
			JSONObject json = JsonUtil.parseJson(data);
			return new DeleteFileToRecyclebin(
					JsonUtil.getString(json, "repositoryId"),
					JsonUtil.getBoolean(json, "isFile"),
					JsonUtil.getString(json, "name"),
					JsonUtil.getString(json, "path"));
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static RestoreFile getDeleteRestoreFile(String data) {
		try {
			JSONObject json = JsonUtil.parseJson(data);
			return new RestoreFile(
					JsonUtil.getString(json, "repositoryId"),
					JsonUtil.getBoolean(json, "isFile"),
					JsonUtil.getString(json, "recycleId"));
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static DeleteFileFromRecyclebin getDeleteFileFromRecyclebin(String data) {
		try {
			JSONObject json = JsonUtil.parseJson(data);
			return new DeleteFileFromRecyclebin(
					JsonUtil.getString(json, "repositoryId"),
					JsonUtil.getBoolean(json, "isFile"),
					JsonUtil.getString(json, "recycleId"));
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static CleanRecyclebin getCleanRecyclebin(String data) {
		try {
			JSONObject json = JsonUtil.parseJson(data);
			return new CleanRecyclebin(JsonUtil.getString(json, "repositoryId"));
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static DownloadId getDownloadId(String data) {
		DownloadId downloadId = new DownloadId();
		JSONObject json;
		try {
			json = JsonUtil.parseJson(data);
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
		try {
			downloadId.setRepositoryId(JsonUtil.getString(json, "repositoryId"));
		} catch (JSONException e) {
			downloadId.setRepositoryId(null);
		}
		try {
			downloadId.setShareId(JsonUtil.getString(json, "shareId"));
		} catch (JSONException e) {
			downloadId.setShareId(null);
		}
		try {
			downloadId.setUserFileId(JsonUtil.getLong(json, "userFileId"));
		} catch (JSONException e) {
			downloadId.setUserFileId(null);
		}
		try {
			downloadId.setFileName(JsonUtil.getString(json, "fileName"));
		} catch (JSONException e) {
			downloadId.setFileName(null);
		}
		//TODO 多文件分享
//        downloadId.setFolder(new Folder(JsonUtil.getJSONObject(json, "folder")));
		downloadId.setFolder(null);
		return downloadId;
	}

	public static CreateShare getCreateShare(String data) {
		JSONObject json;
		CreateShare createShare;
		try {
			json = JsonUtil.parseJson(data);
			createShare = new CreateShare(
					JsonUtil.getString(json, "repositoryId"),
					JsonUtil.getString(json, "name"),
					JsonUtil.getString(json, "path"),
					JsonUtil.getLong(json, "validTime"));
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
		try {
			createShare.setPassword(JsonUtil.getString(json, "password"));
		} catch (JSONException e) {
			createShare.setPassword(null);
		}
		return createShare;
	}

	public static GetShare getShare(String data) {
		JSONObject json;
		GetShare getShare;
		try {
			json = JsonUtil.parseJson(data);
			getShare = new GetShare(JsonUtil.getString(json, "shareId"));
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
		try {
			getShare.setPassword(JsonUtil.getString(json, "password"));
		} catch (JSONException e) {
			getShare.setPassword(null);
		}
		return getShare;
	}

	public static SaveShare getSaveShare(String data) {
		JSONObject json;
		SaveShare saveShare;
		try {
			json = JsonUtil.parseJson(data);
			saveShare = new SaveShare(JsonUtil.getString(json, "shareId"),
					JsonUtil.getString(json, "path"));
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
		try {
			saveShare.setPassword(JsonUtil.getString(json, "password"));
		} catch (JSONException e) {
			saveShare.setPassword(null);
		}
		return saveShare;
	}

	public static CreateInform getCreateInform(String data) {
		try {
			JSONObject json = JsonUtil.parseJson(data);
			return new CreateInform(
					JsonUtil.getString(json, "header"),
					JsonUtil.getString(json, "content"),
					JsonUtil.getLong(json, "validTime"));
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}
}
