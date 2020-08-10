import Axios from 'axios';
import {message} from 'ant-design-vue';

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
      //判断是否是Token错误
      if (code === 0) {
        if (data.errors != null) {
          for (let index in data.errors) {
            message.warn(data.errors[index]);
          }
        } else {
          message.warn(content);
        }
      } else if (code === -2) {
        localStorage.setItem("token", "");
        self.location.href = "/login";
        message.warn("状态异常!");
      } else {
        catcher(code, content);
      }
    }
  }).catch(error => {
    message.warn("网络异常!");
    if (url === "/login/token") {
      localStorage.setItem("token", "");
      self.location.href = "/login";
    }
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

