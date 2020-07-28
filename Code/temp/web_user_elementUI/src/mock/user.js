import Mock from 'mockjs';

const userApi = {
  loginByToken: "/api/login/token",
  loginByAccount: "/api/login/account",
  loginByEmail: "/api/login/email",
  registerByAccount: "/api/register/account",
  registerByEmail: "/api/register/email",
  forgetEmail: "/api/forget/email",
  getUserInfo: "/api/user/info/",
  changeUserInfo: "/api/user/info",
  changeUserPassword: "/api/user/password"
};

const folder = {
  "name": "root",
  "path": "",
  "depth": 0,
  "createTime": 1593423709223,
  "changeTime": 1593423709223,
  "status": 1,
  "folders": {
    "folder2": {
      "name": "folder2",
      "path": "/root",
      "depth": 1,
      "createTime": 1593423709223,
      "changeTime": 1593423709223,
      "status": 1,
      "folders": null,
      "files": {
        "file4.txt": {
          "id": "file4",
          "userFileId": 4,
          "shareIdList": null,
          "name": "file4.txt",
          "path": "/root/folder2",
          "type": "txt",
          "size": 1,
          "status": 1,
          "createTime": 1593423709223,
          "changeTime": 1593423709223
        }
      }
    },
    "folder1": {
      "name": "folder1",
      "path": "/root",
      "depth": 1,
      "createTime": 1593423709223,
      "changeTime": 1593423709223,
      "status": 1,
      "folders": {
        "folder3": {
          "name": "folder3",
          "path": "/root/folder1",
          "depth": 1,
          "createTime": 1593423709223,
          "changeTime": 1593423709223,
          "status": 1,
          "folders": null,
          "files": {
            "file5.txt": {
              "id": "file5",
              "userFileId": 5,
              "shareIdList": null,
              "name": "file5.txt",
              "path": "/root/folder1/folder3",
              "type": "txt",
              "size": 1,
              "status": 1,
              "createTime": 1593423709223,
              "changeTime": 1593423709223
            }
          }
        }
      },
      "files": {
        "file3.txt": {
          "id": "file3",
          "userFileId": 3,
          "shareIdList": null,
          "name": "file3.txt",
          "path": "/root/folder1",
          "type": "txt",
          "size": 1,
          "status": 1,
          "createTime": 1593423709223,
          "changeTime": 1593423709223
        }
      }
    }
  },
  "files": {
    "file1.txt": {
      "id": "file1",
      "userFileId": 1,
      "shareIdList": null,
      "name": "file1.txt",
      "path": "/root",
      "type": "txt",
      "size": 1,
      "status": 1,
      "createTime": 1593423709223,
      "changeTime": 1593423709223
    },
    "file2.txt": {
      "id": "file2",
      "userFileId": 2,
      "shareIdList": null,
      "name": "file2.txt",
      "path": "/root",
      "type": "txt",
      "size": 1,
      "status": 1,
      "createTime": 1593423709223,
      "changeTime": 1593423709223
    }
  }
};
const repository = {
  "id": "5f06b20d17e32b20664aa4c6",
  "userId": 1,
  "status": 1,
  "repoSize": 1099511627776,
  "useSize": 0,
  "folder": folder,
  "recycleBin": {"folders": null, "files": null}
};
const user = {
  "id": 1,
  "repoId": "5f06b20d17e32b20664aa4c6",
  "account": "admin",
  "email": "814878826@qq.com",
  "phone": "18600000000",
  "name": "Mock测试",
  "status": "正常",
  "level": "1级",
  "createTime": 1594274316583,
  "changeTime": 1594274316583
};

Mock.mock(userApi.loginByToken, "get", {code: 1, message: '登陆成功', data: {user: user, repository: repository}});
Mock.mock(userApi.loginByAccount, "post", {code: 1, message: '登陆成功', data: {user: user, repository: repository}});
Mock.mock(userApi.loginByEmail, "post", {code: 1, message: '登陆成功', data: {user: user, repository: repository}});
Mock.mock(userApi.registerByAccount, "put", {code: 1, message: '成功', data: null});
Mock.mock(userApi.registerByEmail, "put", {code: 1, message: '成功', data: null});
Mock.mock(userApi.forgetEmail, "post", {code: 1, message: '成功', data: null});
Mock.mock(userApi.changeUserInfo, "patch", {code: 1, message: '修改成功', data: {user: user}});
Mock.mock(userApi.changeUserPassword, "patch", {code: 1, message: '修改成功', data: null});
Mock.mock(userApi.getUserInfo, "get", {code: 1, message: '查询成功', data: {user: user}});
