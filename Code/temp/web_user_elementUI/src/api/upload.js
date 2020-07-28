import Axios from "axios";
import {message} from "../util/message";

const axios = Axios.create({
  baseURL: "/api",
  timeout: 10000,
  headers: {
    ['Content-Type']: 'multipart/form-data',
    ['authorization']: localStorage.getItem("token") === null ? '' : localStorage.getItem("token"),
  }
});

export function uploadSmallFile(file, fileName, md5, next) {
  const data = new FormData();
  data.append("block", file);
  data.append("fileName", fileName);
  data.append("blockMd5", md5);
  data.append("fileMd5", md5);
  data.append("index", -1);
  data.append("length", -1);
  axios.request({
    url: "/upload",
    method: "post",
    data: data
  }).then(response => {
    console.log(response);
    if (response.data.code === 1) {
      next();
    }
  }).catch(error => {
    message("网络异常,操作失败!");
    console.log(error);
  })
}

//TODO 待完善
export function uploadBigFile(block, fileName, blockMd5, fileMd5, index, length, next) {
  const data = new FormData();
  data.append("block", block);
  data.append("fileName", fileName);
  data.append("blockMd5", blockMd5);
  data.append("fileMd5", fileMd5);
  data.append("index", index);
  data.append("length", length);
  axios.request({
    url: "/upload",
    method: "post",
    data: data
  }).then(response => {
    console.log(response);
    next(response.data);
  }).catch(error => {
    message("网络异常,操作失败!");
    console.log(error);
  })
}
