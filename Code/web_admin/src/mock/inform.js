import Mock from 'mockjs';

const infoApi = {
  getInformList: "/api/admin/inform",
  getInform: "/api/admin/inform/",
  changeInformStatus: "/api/admin/inform/",
  getUserInformList: "/api/admin/inform/user/list/",
  getAdminInformList: "/api/admin/inform/admin/list/",
  changeUserInformStatus: "/api/admin/inform/user/",
  changeAdminInformStatus: "/api/admin/inform/admin/",
  createUserInform: "/api/admin/inform/user",
  createAdminInform: "/api/admin/inform/admin",
};

const informList = [{
  "id": "5f0d06b566e4da663ac70113",
  "header": "测试通知标题",
  "content": "测试通知内容",
  "createTime": 1594689205849,
  "validTime": 1595689205849,
  "status": 0,
  "adminId": 1
}, {
  "id": "5f0d06b566e4da663ac70114",
  "header": "测试通知标题",
  "content": "测试通知内容",
  "createTime": 1594689205849,
  "validTime": 1595689205849,
  "status": 1,
  "adminId": 1
}];

const inform = {
  "id": "5f0d06b566e4da663ac70113",
  "header": "测试通知标题",
  "content": "测试通知内容",
  "createTime": 1594689205849,
  "validTime": 1595689205849,
  "status": 0,
  "adminId": 1
};

Mock.mock(infoApi.getInformList, "get", {code: 1, message: "查询成功", data: {informList: informList}});
Mock.mock(infoApi.getInform + "", "get", {code: 1, message: "查询成功", data: {inform: inform}});
Mock.mock(infoApi.changeInformStatus + "5f0d06b566e4da663ac70113/1", "patch", {code: 1, message: "修改成功", data: null});
Mock.mock(infoApi.getUserInformList + "1", "get", {code: 1, message: "查询成功", data: {informList: informList}});
Mock.mock(infoApi.getAdminInformList + "1", "get", {code: 1, message: "查询成功", data: {informList: informList}});
Mock.mock(infoApi.changeUserInformStatus + "5f0d06b566e4da663ac70113/1", "patch", {code: 1, message: "修改成功", data: null});
Mock.mock(infoApi.changeAdminInformStatus + "5f0d06b566e4da663ac70113/1", "patch", {code: 1, message: "修改成功", data: null});
Mock.mock(infoApi.createUserInform, "put", {code: 1, message: "创建成功", data: {inform: inform}});
Mock.mock(infoApi.createAdminInform, "put", {code: 1, message: "创建成功", data: {inform: inform}});

