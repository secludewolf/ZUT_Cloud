import Mock from 'mockjs';

const shareApi = {
  getShareList: "/api/admin/share/list/",
  getShareInfo: "/api/admin/share/",
  deleteShare: "/api/admin/share/",
};

const shareList = [{
  "id": "test",
  "userId": 1,
  "repoId": "5f0c3139b9555b210e4d056c",
  "name": "test",
  "password": "123456",
  "status": 1,
  "createTime": 1594634553484,
  "validTime": 1594634553484
}];

const share = {
  "share": {
    "id": "test",
    "userId": 1,
    "repoId": "5f0c3139b9555b210e4d056e",
    "name": "test",
    "password": "123456",
    "status": 1,
    "createTime": 1594634553728,
    "validTime": 1594634553728
  },
  "shareRepository": {
    "id": "5f0c3139b9555b210e4d056e",
    "shareId": "test",
    "userId": 1,
    "userFileIdList": [],
    "fileIdList": [],
    "saveUserIdList": {},
    "downloadUserIdList": {},
    "folder": {
      "name": null,
      "path": null,
      "depth": 0,
      "createTime": 0,
      "changeTime": 0,
      "status": 0,
      "folders": null,
      "files": null
    }
  }
};


Mock.mock(shareApi.getShareList + 1, "get", {code: 1, message: "查询成功", data: {shareList: shareList}});
Mock.mock(shareApi.getShareInfo + "test" + "/info", "get", {
  code: 1,
  message: "查询成功",
  data: share
});
Mock.mock(shareApi.deleteShare + "test", "delete", {code: 1, message: "删除成功", data: null});
