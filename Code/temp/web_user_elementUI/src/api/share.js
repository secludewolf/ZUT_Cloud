import {delete_, get, patch, post, put} from "../util/request";
import {message} from "../util/message";

const shareApi = {
  createShare: "/share",
  getShareList: "/share/list",
  deleteShare: "/share/",
  getShare: "/share",
  saveShare: "/share/save",
};

const catcher = (code, content) => {
  message(content, "warning");
};

export function createShare(data, handler, catcher = catcher) {
  put(shareApi.createShare, data, handler, catcher);
}

export function getShareList(handler, catcher = catcher) {
  get(shareApi.getShareList, null, handler, catcher);
}

export function deleteShare(data, handler, catcher = catcher) {
  delete_(shareApi.deleteShare + data, null, handler, catcher);
}

export function getShare(data, handler, catcher = catcher) {
  post(shareApi.getShare, data, handler, catcher);
}

export function saveShare(data, handler, catcher = catcher) {
  post(shareApi.saveShare, data, handler, catcher);
}
