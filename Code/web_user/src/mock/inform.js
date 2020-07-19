import Mock from 'mockjs';

const informApi = {
  getInformList: "/api/inform",
  getInform: "/api/inform/",
  changeInformStatus: "/api/inform/",
};


const inform1 = {
  id: "5f0809ab9b5aed16566006aa",
  header: "测试通知标题一",
  content: "测试通知内容一",
  createTime: 1594362283563,
  validTime: 1595362283563,
  status: 0,
  adminId: 1
};

const inform2 = {
  id: "5f0809ab9b5aed16566006bb",
  header: "测试通知标题二",
  content: "测试通知内容二",
  createTime: 1594362283563,
  validTime: 1595362283563,
  status: 1,
  adminId: 1
};

const informList = [inform1, inform2];

Mock.mock(informApi.getInformList, "get", {code: 1, message: '成功', data: {informList: informList}});
Mock.mock(informApi.getInform, "get", {code: 1, message: '成功', data: {inform: inform1}});
Mock.mock(informApi.changeInformStatus + "5f0809ab9b5aed16566006aa/1", "patch", {code: 1, message: '成功', data: null});
