import {get} from "../util/request";

const previewApi = {
  previewPhoto: "/preview/photo/"
};

export function previewPhoto(data, handler, catcher) {
  request(previewApi.previewPhoto + data, null, "get", handler, catcher, "blob");
}

import Axios from 'axios';
import {message} from 'ant-design-vue';

export function request(url, data, method, handler, catcher, responseType) {
  const axios = Axios.create({
    baseURL: "/api",
    timeout: 6000,
    headers: {
      ['Content-Type']: 'application/json',
      ['authorization']: localStorage.getItem("token") === null ? '' : localStorage.getItem("token"),
    },
    responseType: responseType
  });
  axios.request({
    url: url,
    method: method,
    data: data
  }).then(response => {
    console.log(response);
    if (response.headers['authorization'] != null) {
      localStorage.setItem("token", response.headers['authorization']);
    }
    const data = response.data.data;
    const code = response.data.code;
    const content = response.data.message;
    handler(response);
  }).catch(error => {
    console.log(error);
    const code = error.response.data.code;
    const content = error.response.data.message;
    if (content != null) {
      message.warn(content);
    } else {
      message.warn("未知错误");
    }
  })
}

