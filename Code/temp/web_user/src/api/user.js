import {get, patch, post, put} from "../util/request";
import {message} from 'ant-design-vue';

const userApi = {
  loginByToken: "/login/token",
  loginByAccount: "/login/account",
  loginByEmail: "/login/email",
  registerByAccount: "/register/account",
  registerByEmail: "/register/email",
  forgetEmail: "/forget/email",
  getUserInfo: "/user/info/",
  changeUserInfo: "/user/info",
  changeUserPassword: "/user/password"
};

const catcher = (code, content) => {
  message.warn(content);
};

export function loginByToken(handler, catcher = catcher) {
  get(userApi.loginByToken, null, handler, catcher);
}

export function loginByAccount(data, handler, catcher = catcher) {
  post(userApi.loginByAccount, data, handler, catcher);
}

export function loginByEmail(data, handler, catcher = catcher) {
  post(userApi.loginByEmail, data, handler, catcher);
}

export function registerByAccount(data, handler, catcher = catcher) {
  put(userApi.registerByAccount, data, handler, catcher);
}

export function registerByEmail(data, handler, catcher = catcher) {
  put(userApi.registerByEmail, data, handler, catcher);
}

export function forgetEmail(data, handler, catcher = catcher) {
  post(userApi.forgetEmail, data, handler, catcher);
}

export function getUserInfo(data, handler, catcher = catcher) {
  get(userApi.getUserInfo + data, null, handler, catcher);
}

export function changeUserInfo(data, handler, catcher = catcher) {
  patch(userApi.changeUserInfo, data, handler, catcher);
}

export function changeUserPassword(data, handler, catcher = catcher) {
  patch(userApi.changeUserPassword, data, handler, catcher);
}


