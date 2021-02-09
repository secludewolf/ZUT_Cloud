import {post} from "../util/request";

const downloadApi = {
  getDownloadId: "/admin/download/"
};

export function getDownloadId(data, handler, catcher) {
  post(downloadApi.getDownloadId + data, null, handler, catcher);
}

