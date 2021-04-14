import {delete_, get, patch, post, put} from "../util/request";
import {message} from "../util/message";

const controllerApi = {
  getPvData: "/admin/chart/pv",
  getUvData: "/admin/chart/uv",
  getDfData: "/admin/chart/df",
  getDsData: "/admin/chart/ds",
  getMonthData: "/admin/chart/mouth",
  getFileData: "/admin/chart/file",
  getDownloadData: "/admin/chart/download",
};

const catcher = (code, content) => {
  message(content, "warning");
};

export function getPvData(handler, catcher = catcher) {
  get(controllerApi.getPvData, null, handler, catcher);
}

export function getUvData(handler, catcher = catcher) {
  get(controllerApi.getUvData, null, handler, catcher);
}

export function getDfData(handler, catcher = catcher) {
  get(controllerApi.getDfData, null, handler, catcher);
}

export function getDsData(handler, catcher = catcher) {
  get(controllerApi.getDsData, null, handler, catcher);
}

export function getMonthData(handler, catcher = catcher) {
  get(controllerApi.getMonthData, null, handler, catcher);
}

export function getFileData(handler, catcher = catcher) {
  get(controllerApi.getFileData, null, handler, catcher);
}

export function getDownloadData(handler, catcher = catcher) {
  get(controllerApi.getDownloadData, null, handler, catcher);
}
