import {delete_, get, patch, post, put} from "../util/request";
import {message} from "../util/message";

const adminApi = {
  loginByToken: "/admin/login/token",
  loginByAccount: "/admin/login/account",
  registerByAccount: "/admin/register/account",
  forgetEmail: "/admin/forget/email",
  getAdminInfo: "/admin/user/info/",
  changeAdminInfo: "/admin/user/info",
  changeAdminPassword: "/admin/user/password",
  createUserManage: "/admin/user",
  createAdminManage: "/admin/admin",
  deleteUserManage: "/admin/user",
  deleteAdminManage: "/admin/admin",
  getUserListManage: "/admin/user/list/",
  getAdminListManage: "/admin/admin/list/",
  getUserInfoManage: "/admin/user/",
  getAdminInfoManage: "/admin/admin/",
  changeUserInfoManage: "/admin/user",
  changeAdminInfoManege: "/admin/admin"
};

const catcher = (code, content) => {
  message(content, "warning");
};

export function loginByToken(handler, catcher = catcher) {
  get(adminApi.loginByToken, null, handler, catcher);
}

export function loginByAccount(data, handler, catcher = catcher) {
  post(adminApi.loginByAccount, data, handler, catcher);
}

export function registerByAccount(data, handler, catcher = catcher) {
  put(adminApi.registerByAccount, data, handler, catcher);
}

export function forgetEmail(data, handler, catcher = catcher) {
  post(adminApi.forgetEmail, data, handler, catcher);
}

export function getAdminInfo(handler, catcher = catcher) {
  get(adminApi.getAdminInfo, null, handler, catcher);
}

export function changeAdminInfo(data, handler, catcher = catcher) {
  patch(adminApi.changeAdminInfo, data, handler, catcher);
}

export function changeAdminPassword(data, handler, catcher = catcher) {
  patch(adminApi.changeAdminPassword, data, handler, catcher);
}

export function createUserManage(data, handler, catcher = catcher) {
  put(adminApi.createUserManage, data, handler, catcher);
}

export function createAdminManage(data, handler, catcher = catcher) {
  put(adminApi.createAdminManage, data, handler, catcher);
}

export function deleteUserManage(data, handler, catcher = catcher) {
  delete_(adminApi.deleteUserManage, data, handler, catcher);
}

export function deleteAdminManage(data, handler, catcher = catcher) {
  delete_(adminApi.deleteAdminManage, data, handler, catcher);
}

export function getUserListManage(data, handler, catcher = catcher) {
  get(adminApi.getUserListManage + data, null, handler, catcher);
}

export function getAdminListManage(data, handler, catcher = catcher) {
  get(adminApi.getAdminListManage + data, null, handler, catcher);
}

export function getUserInfoManage(data, handler, catcher = catcher) {
  get(adminApi.getUserInfoManage + data + "/info", null, handler, catcher);
}

export function getAdminInfoManage(data, handler, catcher = catcher) {
  get(adminApi.getAdminInfoManage + data + "/info", null, handler, catcher);
}

export function changeUserInfoManage(data, handler, catcher = catcher) {
  patch(adminApi.changeUserInfoManage, data, handler, catcher);
}

export function changeAdminInfoManage(data, handler, catcher = catcher) {
  patch(adminApi.changeAdminInfoManege, data, handler, catcher);
}
