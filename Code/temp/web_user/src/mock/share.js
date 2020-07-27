import Mock from 'mockjs';

const shareApi = {
  createShare: "/api/share",
  getShareList: "/api/share/list",
  deleteShare: "/api/share/",
  getShare: "/api/share",
  saveShare: "/api/share/save",
};

const shareList = [{
  id: "share1",
  userId: 17,
  repoId: "test",
  name: "test",
  password: null,
  status: 1,
  createTime: 1594365511023,
  validTime: 1594365511023
}, {
  id: "share2",
  userId: 17,
  repoId: "test",
  name: "test",
  password: "123456",
  status: 0,
  createTime: 1594365511023,
  validTime: 1594365511023
}];


const shareInfo = {
  "share": {
    "id": "test",
    "userId": 21,
    "repoId": "5f0816478aaa9411110eb129",
    "name": "test",
    "password": "123456",
    "status": 1,
    "createTime": 1594365511605,
    "validTime": 1594365511605
  }, "shareRepository": {
    "id": "5f0816478aaa9411110eb129",
    "shareId": "test",
    "userId": 1,
    "userFileIdList": [],
    "fileIdList": [],
    "saveUserIdList": {},
    "downloadUserIdList": {},
    "folder": {
      "name": "root",
      "path": "",
      "depth": 0,
      "createTime": 1594365511598,
      "changeTime": 1594365511598,
      "status": 1,
      "folders": {
        "folder2": {
          "name": "folder2",
          "path": "/root",
          "depth": 1,
          "createTime": 1594365511598,
          "changeTime": 1594365511598,
          "status": 1,
          "folders": null,
          "files": {
            "file4.txt": {
              "id": "file4",
              "userFileId": 98,
              "shareIdList": null,
              "name": "file4.txt",
              "path": "/root/folder2",
              "type": "txt",
              "size": 1,
              "status": 1,
              "createTime": 1594365511598,
              "changeTime": 1594365511598
            }
          }
        },
        "folder1": {
          "name": "folder1",
          "path": "/root",
          "depth": 1,
          "createTime": 1594365511598,
          "changeTime": 1594365511598,
          "status": 1,
          "folders": {
            "folder3": {
              "name": "folder3",
              "path": "/root/folder1",
              "depth": 1,
              "createTime": 1594365511598,
              "changeTime": 1594365511598,
              "status": 1,
              "folders": null,
              "files": {
                "file5.txt": {
                  "id": "file5",
                  "userFileId": 99,
                  "shareIdList": null,
                  "name": "file5.txt",
                  "path": "/root/folder1/folder3",
                  "type": "txt",
                  "size": 1,
                  "status": 1,
                  "createTime": 1594365511598,
                  "changeTime": 1594365511598
                }
              }
            }
          },
          "files": {
            "file3.txt": {
              "id": "file3",
              "userFileId": 97,
              "shareIdList": null,
              "name": "file3.txt",
              "path": "/root/folder1",
              "type": "txt",
              "size": 1,
              "status": 1,
              "createTime": 1594365511598,
              "changeTime": 1594365511598
            }
          }
        }
      },
      "files": {
        "file1.txt": {
          "id": "file1",
          "userFileId": 95,
          "shareIdList": null,
          "name": "file1.txt",
          "path": "/root",
          "type": "txt",
          "size": 1,
          "status": 1,
          "createTime": 1594365511598,
          "changeTime": 1594365511598
        },
        "file2.txt": {
          "id": "file2",
          "userFileId": 96,
          "shareIdList": null,
          "name": "file2.txt",
          "path": "/root",
          "type": "txt",
          "size": 1,
          "status": 1,
          "createTime": 1594365511598,
          "changeTime": 1594365511598
        }
      }
    }
  }
};

Mock.mock(shareApi.getShareList, "get", {code: 1, message: '成功', data: {shareList: shareList}});
Mock.mock(shareApi.getShare, "post", {code: 1, message: '成功', data: shareInfo});
Mock.mock(shareApi.createShare, "put", {
  code: 1, message: '成功', data: {
    share: {
      id: "share_id",
      userId: 17,
      repoId: "test",
      name: "测试分享",
      password: null,
      status: 1,
      createTime: 1594365511023,
      validTime: 1594365511023
    },
  }
});
Mock.mock(shareApi.deleteShare + "share1", "delete", {code: 1, message: '成功', data: null});
Mock.mock(shareApi.saveShare, "post", {code: 1, message: '成功', data: null});
