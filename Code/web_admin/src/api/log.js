import {delete_, get, patch, post, put} from "../util/request";
import {message} from "../util/message";

const logApi = {
  getLogListManage: "/admin/log/list/"
};

const catcher = (code, content) => {
  message(content, "warning");
};

export function getLogListManage(data, handler, catcher = catcher) {
  get(logApi.getLogListManage + data, null, handler, catcher);
}
