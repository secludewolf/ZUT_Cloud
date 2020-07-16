import {delete_, get, patch, post, put} from "../util/request";
import {message} from "../util/message";

const repositoryApi = {
  getRepository: "/repository/",
  createFile: "/repository/file/create",
  createFolder: "/repository/folder/create",
  moveFile: "/repository/file/move",
  moveFolder: "/repository/folder/move",
  copyFile: "/repository/file/copy",
  copyFolder: "/repository/folder/copy",
  renameFile: "/repository/file/rename",
  renameFolder: "/repository/folder/rename",
  deleteFromRepository: "/repository",
  restoreFromRecycleBin: "/repository/recyclebin",
  deleteFromRecycleBin: "/repository/recyclebin",
  cleanRecycleBin: "/repository/recyclebin/clean",
};

const catcher = (code, content) => {
  message(content, "warning");
};

export function getRepository(data, handler, catcher = catcher) {
  get(repositoryApi.getRepository + data, null, handler, catcher);
}

export function createFile(data, handler, catcher = catcher) {
  put(repositoryApi.createFile, data, handler, catcher);
}

export function createFolder(data, handler, catcher = catcher) {
  put(repositoryApi.createFolder, data, handler, catcher);
}

export function moveFile(data, handler, catcher = catcher) {
  patch(repositoryApi.moveFile, data, handler, catcher);
}

export function moveFolder(data, handler, catcher = catcher) {
  patch(repositoryApi.moveFolder, data, handler, catcher);
}

export function copyFile(data, handler, catcher = catcher) {
  patch(repositoryApi.copyFile, data, handler, catcher);
}

export function copyFolder(data, handler, catcher = catcher) {
  patch(repositoryApi.copyFolder, data, handler, catcher);
}

export function renameFile(data, handler, catcher = catcher) {
  patch(repositoryApi.renameFile, data, handler, catcher);
}

export function renameFolder(data, handler, catcher = catcher) {
  patch(repositoryApi.renameFolder, data, handler, catcher);
}

export function deleteFromRepository(data, handler, catcher = catcher) {
  delete_(repositoryApi.deleteFromRepository, data, handler, catcher);
}

export function restoreFromRecycleBin(data, handler, catcher = catcher) {
  patch(repositoryApi.restoreFromRecycleBin, data, handler, catcher);
}

export function deleteFromRecycleBin(data, handler, catcher = catcher) {
  delete_(repositoryApi.deleteFromRecycleBin, data, handler, catcher);
}

export function cleanRecycleBin(data, handler, catcher = catcher) {
  delete_(repositoryApi.cleanRecycleBin, data, handler, catcher);
}
