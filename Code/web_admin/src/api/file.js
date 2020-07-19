import {delete_, get, patch, post, put} from "../util/request";
import {message} from "../util/message";

const fileApi = {
  getFileList: "/admin/file/list/",
  getFileInfo: "/admin/file/",
  deleteFile: "/admin/file/",
};

const catcher = (code, content) => {
  message(content, "warning");
};

export function getFileList(data, handler, catcher = catcher) {
  get(fileApi.getFileList + data, null, handler, catcher);
}

export function getFileInfo(data, handler, catcher = catcher) {
  post(fileApi.getFileInfo + data + "/info", null, handler, catcher);
}

export function deleteFile(data, handler, catcher = catcher) {
  delete_(fileApi.deleteFile + data, null, handler, catcher);
}
