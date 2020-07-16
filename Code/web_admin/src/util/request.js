//TODO 类型创建请求工具文件
//TODO request.js创建axios的封装,拦截器,baseUrl,错误处理
import Axios from 'axios';
import {message} from "./message";

export function request(url, data, method, handler, catcher) {
  const axios = Axios.create({
    baseURL: "/api",
    timeout: 6000,
    headers: {
      ['Content-Type']: 'application/json',
      ['authorization']: localStorage.getItem("token") === null ? '' : localStorage.getItem("token"),
    }
  });
  console.log(url);
  console.log(data);
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
    if (code === 1) {
      handler(data);
    } else {
      catcher(code, content);
    }
  }).catch(error => {
    message("网络异常,操作失败!");
    console.log(error);
  })
}

export function get(url, data, handler, catcher) {
  request(url, data, "get", handler, catcher);
}

export function post(url, data, handler, catcher) {
  request(url, data, "post", handler, catcher);

}

export function put(url, data, handler, catcher) {
  request(url, data, "put", handler, catcher);
}

export function patch(url, data, handler, catcher) {
  request(url, data, "patch", handler, catcher);
}

export function delete_(url, data, handler, catcher) {
  request(url, data, "delete", handler, catcher);
}

