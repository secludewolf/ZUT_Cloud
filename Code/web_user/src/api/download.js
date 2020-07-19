import {post} from "../util/request";

const downloadApi = {
  getDownloadId: "/download"
};

export function getDownloadId(data, handler, catcher) {
  post(downloadApi.getDownloadId, data, handler, catcher);
}

