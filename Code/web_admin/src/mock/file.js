import Mock from 'mockjs';

const fileApi = {
  getFileList: "/api/admin/file/list/",
  getFileInfo: "/api/admin/file/",
  deleteFile: "/api/admin/file/",
};

const fileList = [{
  "id": "a2f83d96f9f0cfda99af9567f3a5c4e0",
  "name": "测试小文件.txt",
  "path": "C:\\Users\\18638\\OneDrive\\WorkSpace\\Cloud\\Code\\cloud\\SaveFolder\\1\\1\\a2f83d96f9f0cfda99af9567f3a5c4e0",
  "size": 12,
  "status": 1,
  "type": "txt",
  "quoteNumber": 0,
  "createTime": 1594633171459,
  "changeTime": 1594633171459
}, {
  "id": "a455fd6ae07894a9d3f8e9c8ba1924e3",
  "name": "测试大文件.mp4",
  "path": "C:\\Users\\18638\\OneDrive\\WorkSpace\\Cloud\\Code\\cloud\\SaveFolder\\1\\1\\a455fd6ae07894a9d3f8e9c8ba1924e3",
  "size": 22383580,
  "status": 1,
  "type": "mp4",
  "quoteNumber": 0,
  "createTime": 1594633174044,
  "changeTime": 1594633174044
}];

const file = {
  "id": "a2f83d96f9f0cfda99af9567f3a5c4e0",
  "name": "测试小文件.txt",
  "path": "C:\\Users\\18638\\OneDrive\\WorkSpace\\Cloud\\Code\\cloud\\SaveFolder\\1\\1\\a2f83d96f9f0cfda99af9567f3a5c4e0",
  "size": 12,
  "status": 1,
  "type": "txt",
  "quoteNumber": 0,
  "createTime": 1594633171459,
  "changeTime": 1594633171459
};


Mock.mock(fileApi.getFileList + 1, "get", {code: 1, message: "查询成功", data: {fileList: fileList}});
Mock.mock(fileApi.getFileInfo + "a2f83d96f9f0cfda99af9567f3a5c4e0" + "/info", "get", {
  code: 1,
  message: "查询成功",
  data: {file: file}
});
Mock.mock(fileApi.deleteFile + "a2f83d96f9f0cfda99af9567f3a5c4e0", "delete", {code: 1, message: "删除成功", data: null});
