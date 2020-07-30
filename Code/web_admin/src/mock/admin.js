import Mock from 'mockjs';

const adminApi = {
  loginByToken: "/api/admin/login/token",
  loginByAccount: "/api/admin/login/account",
  registerByAccount: "/api/admin/register/account",
  forgetEmail: "/api/admin/forget/email",
  getAdminInfo: "/api/admin/info/",
  changeAdminInfo: "/api/admin/info",
  changeAdminPassword: "/api/admin/password",
  createUserManage: "/api/admin/user",
  createAdminManage: "/api/admin/admin",
  deleteUserManage: "/api/admin/user",
  deleteAdminManage: "/api/admin/admin",
  getUserListManage: "/api/admin/user/list/",
  getAdminListManage: "/api/admin/admin/list/",
  getUserInfoManage: "/api/admin/user/",
  getAdminInfoManage: "/api/admin/admin/",
  changeUserInfoManage: "/api/admin/user",
  changeAdminInfoManege: "/api/admin/admin"
};

const admin = {
  id: 57,
  repoId: null,
  account: "lichen",
  email: "814878826@qq.com",
  phone: "18600000000",
  name: "测试账号",
  status: "正常",
  level: "1级",
  createTime: 1594609737393,
  changeTime: 1594609737393
};

const user = {
  id: 14,
  repoId: "5f0bd0499bb65f0b39b08b1a",
  account: "lichen",
  email: "814878826@qq.com",
  phone: null,
  name: "测试账号",
  status: "正常",
  level: "1级",
  createTime: 1594609737477,
  changeTime: 1594609737477
};

Mock.mock(adminApi.loginByToken, "get", {code: 1, message: "登陆成功", data: {admin: admin}});
Mock.mock(adminApi.loginByAccount, "post", {code: 1, message: "登陆成功", data: {admin: admin}});
Mock.mock(adminApi.registerByAccount, "put", {code: 1, message: "注册成功", data: null});
Mock.mock(adminApi.forgetEmail, "post", {code: 1, message: "重置成功", data: null});
Mock.mock(adminApi.getAdminInfo, "get", {code: 1, message: "查询成功", data: {admin: admin}});
Mock.mock(adminApi.changeAdminInfo, "patch", {code: 1, message: "修改成功", data: {admin: admin}});
Mock.mock(adminApi.changeAdminPassword, "patch", {code: 1, message: "修改成功", data: null});
Mock.mock(adminApi.createUserManage, "put", {code: 1, message: "创建成功", data: null});
Mock.mock(adminApi.createAdminManage, "put", {code: 1, message: "创建成功", data: null});
Mock.mock(adminApi.deleteUserManage, "delete", {code: 1, message: "删除成功", data: null});
Mock.mock(adminApi.deleteAdminManage, "delete", {code: 1, message: "删除成功", data: null});
Mock.mock(adminApi.getUserListManage + "1", "get", {code: 1, message: "查询成功", data: {userList: [{"id":19,"repoId":"5f0c2462b296812c15d4bfc9","account":"lichen","email":"814878826@qq.com","phone":null,"name":"测试账号","password":"000000","status":1,"level":1,"createTime":1594631266270,"changeTime":1594631266270}]}});
Mock.mock(adminApi.getAdminListManage + "1", "get", {code: 1, message: "查询成功", data: {adminList: [{"id":91,"repoId":null,"account":"lichen","email":"814878826@qq.com","phone":"18600000000","name":"测试账号","password":"000000","status":1,"level":1,"createTime":1594631267634,"changeTime":1594631267634},{"id":92,"repoId":null,"account":"lichen1","email":"8148788261@qq.com","phone":"186000000001","name":"测试账号","password":"000000","status":1,"level":1,"createTime":1594631267637,"changeTime":1594631267637}]}});
Mock.mock(adminApi.getUserInfoManage, "get", {code: 1, message: "查询成功", data: {user: user}});
Mock.mock(adminApi.getAdminInfoManage, "get", {code: 1, message: "查询成功", data: {admin: admin}});
Mock.mock(adminApi.changeUserInfoManage, "patch", {code: 1, message: "修改成功", data: {user: user}});
Mock.mock(adminApi.changeAdminInfoManege, "patch", {code: 1, message: "修改成功", data: {admin: admin}});
