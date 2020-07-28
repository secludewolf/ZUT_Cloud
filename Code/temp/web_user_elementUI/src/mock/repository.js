import Mock from 'mockjs';

const repositoryApi = {
  getRepository: "/api/repository/",
  createFile: "/api/repository/file/create",
  createFolder: "/api/repository/folder/create",
  moveFile: "/api/repository/file/move",
  moveFolder: "/api/repository/folder/move",
  copyFile: "/api/repository/file/copy",
  copyFolder: "/api/repository/folder/copy",
  renameFile: "/api/repository/file/rename",
  renameFolder: "/api/repository/folder/rename",
  deleteFromRepository: "/api/repository",
  restoreFromRecycleBin: "/api/repository/recyclebin",
  deleteFromRecycleBin: "/api/repository/recyclebin",
  cleanRecycleBin: "/api/repository/recyclebin/clean",
};

const data = {
  repository: {
    "id": "5f06b20d17e32b20664aa4c6",
    "userId": 1,
    "status": 1,
    "repoSize": 1099511627776,
    "useSize": 0,
    "folder": {
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
    },
    "recycleBin": {"folders": null, "files": null}
  }
};

Mock.mock(repositoryApi.getRepository, "get", {code: 1, message: '查询成功', data: data});
Mock.mock(repositoryApi.createFile, "put", {code: 1, message: '创建成功', data: data});
Mock.mock(repositoryApi.createFolder, "put", {code: 1, message: '创建成功', data: data});
Mock.mock(repositoryApi.moveFile, "patch", {code: 1, message: '移动成功', data: data});
Mock.mock(repositoryApi.moveFolder, "patch", {code: 1, message: '移动成功', data: data});
Mock.mock(repositoryApi.copyFile, "patch", {code: 1, message: '复制成功', data: data});
Mock.mock(repositoryApi.copyFolder, "patch", {code: 1, message: '复制成功', data: data});
Mock.mock(repositoryApi.renameFile, "patch", {code: 1, message: '修改成功', data: data});
Mock.mock(repositoryApi.renameFolder, "patch", {code: 1, message: '修改成功', data: data});
Mock.mock(repositoryApi.deleteFromRepository, "delete", {code: 1, message: '删除成功', data: data});
Mock.mock(repositoryApi.restoreFromRecycleBin, "patch", {code: 1, message: '恢复成功', data: data});
Mock.mock(repositoryApi.deleteFromRecycleBin, "delete", {code: 1, message: '删除成功', data: data});
Mock.mock(repositoryApi.cleanRecycleBin, "delete", {code: 1, message: '删除成功', data: data});
