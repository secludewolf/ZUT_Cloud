import {delete_, get, patch, post, put} from "../util/request";
import {message} from "../util/message";

const infoApi = {
  getInformList: "/admin/inform",
  getInform: "/admin/inform/",
  changeInformStatus: "/admin/inform/",
  getUserInformList: "/admin/inform/user/list/",
  getAdminInformList: "/admin/inform/admin/list/",
  changeUserInformStatus: "/admin/inform/user/",
  changeAdminInformStatus: "/admin/inform/admin/",
  createUserInform: "/admin/inform/user",
  createAdminInform: "/admin/inform/admin",
};

const catcher = (code, content) => {
  message(content, "warning");
};

export function getInformList(handler, catcher = catcher) {
  get(infoApi.getInformList, null, handler, catcher);
}

export function getInform(data, handler, catcher = catcher) {
  get(infoApi.getInform + data, null, handler, catcher);
}

export function changeInformStatus(data, handler, catcher = catcher) {
  patch(infoApi.changeInformStatus + data, null, handler, catcher);
}

export function getUserInformList(data, handler, catcher = catcher) {
  get(infoApi.getUserInformList + data, null, handler, catcher);
}

export function getAdminInformList(data, handler, catcher = catcher) {
  get(infoApi.getAdminInformList + data, null, handler, catcher);
}

export function changeUserInformStatus(data, handler, catcher = catcher) {
  patch(infoApi.changeUserInformStatus + data, null, handler, catcher);
}

export function changeAdminInformStatus(data, handler, catcher = catcher) {
  patch(infoApi.changeUserInformStatus + data, null, handler, catcher);
}

export function createUserInform(data, handler, catcher = catcher) {
  put(infoApi.createUserInform, data, handler, catcher);
}

export function createAdminInform(data, handler, catcher = catcher) {
  put(infoApi.createAdminInform, data, handler, catcher);
}
