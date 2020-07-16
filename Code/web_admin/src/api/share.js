import {delete_, get, patch, post, put} from "../util/request";
import {message} from "../util/message";

const shareApi = {
  getShareList: "/admin/share/list/",
  getShareInfo: "/admin/share/",
  deleteShare: "/admin/share/",
};

const catcher = (code, content) => {
  message(content, "warning");
};

export function getShareList(data, handler, catcher = catcher) {
  get(shareApi.getShareList + data, null, handler, catcher);
}

export function getShareInfo(data, handler, catcher = catcher) {
  post(shareApi.getShareInfo + data + "/info", null, handler, catcher);
}

export function deleteShare(data, handler, catcher = catcher) {
  delete_(shareApi.deleteShare + data, null, handler, catcher);
}
