import {delete_, get, patch, post, put} from "../util/request";
import {message} from "../util/message";
import Mock from "mockjs";

const informApi = {
  getInformList: "/inform",
  getInform: "/inform/",
  changeInformStatus: "/inform/",
};

const catcher = (code, content) => {
  message(content, "warning");
};

export function getInformList(handler, catcher = catcher) {
  get(informApi.getInformList, null, handler, catcher)
}

export function getInform(data, handler, catcher = catcher) {
  get(informApi.getInform + data, null, handler, catcher)
}

export function changeInformStatus(data, handler, catcher = catcher) {
  patch(informApi.changeInformStatus + data + '/1', null, handler, catcher)
}


