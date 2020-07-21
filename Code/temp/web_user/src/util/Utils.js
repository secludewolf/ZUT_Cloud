import {FileType, FileTypeName} from "./Const";

/**
 * 获取格式化时间
 * @param date 时间戳
 * @returns {string} 格式化时间
 */
export function getFormatDate(date) {
  date = new Date(date);
  let YY = date.getFullYear() + '-';
  let MM = (date.getMonth() + 1 < 10 ? '0' + (date.getMonth() + 1) : date.getMonth() + 1) + '-';
  let DD = (date.getDate() < 10 ? '0' + (date.getDate()) : date.getDate());
  let hh = (date.getHours() < 10 ? '0' + date.getHours() : date.getHours()) + ':';
  let mm = (date.getMinutes() < 10 ? '0' + date.getMinutes() : date.getMinutes()) + ':';
  let ss = (date.getSeconds() < 10 ? '0' + date.getSeconds() : date.getSeconds());
  return YY + MM + DD + " " + hh + mm + ss;
}

/**
 * 获取文件类型
 * @param type 文件后缀名
 * @returns {string} 文件类型名
 */
export function getTypeName(type) {
  if (type === "folder") {
    return "文件夹";
  } else {
    return FileTypeName[type].name == null ? "未知文件类型" : FileTypeName[type].name;
  }
}

/**
 * 获取格式化文件大小
 * @param size 文件大小
 * @returns {string} 格式化文件大小
 */
export function getFormatSize(size) {
  if (null == size || size === '') {
    return "-";
  }
  let unitArr = ["Bytes", "KB", "MB", "GB", "TB", "PB", "EB", "ZB", "YB"];
  let index = 0;
  size = parseFloat(size);
  index = Math.floor(Math.log(size) / Math.log(1024));
  let formatSize = size / Math.pow(1024, index);
  formatSize = formatSize.toFixed(2);//保留的小数位数
  return formatSize + unitArr[index];
}

/**
 * 获取文件类型对应图片
 * @param type 文件类型
 * @returns 图片路径
 */
export function getImg(type) {
  type = type.toLowerCase();
  if (type === "folder") {
    return require("../assets/explorer/folder.svg");
  } else {
    if (FileType["ai"].indexOf(type) !== -1) {
      return require("../assets/explorer/file_ai.svg");
    } else if (FileType["torrent"].indexOf(type) !== -1) {
      return require("../assets/explorer/file_bt.svg");
    } else if (FileType["cad"].indexOf(type) !== -1) {
      return require("../assets/explorer/file_cad.svg");
    } else if (FileType["cloud"].indexOf(type) !== -1) {
      return require("../assets/explorer/file_cloud.svg");
    } else if (FileType["code"].indexOf(type) !== -1) {
      return require("../assets/explorer/file_code.svg");
    } else if (FileType["excel"].indexOf(type) !== -1) {
      return require("../assets/explorer/file_excel.svg");
    } else if (FileType["exe"].indexOf(type) !== -1) {
      return require("../assets/explorer/file_exe.svg");
    } else if (FileType["flash"].indexOf(type) !== -1) {
      return require("../assets/explorer/file_flash.svg");
    } else if (FileType["html"].indexOf(type) !== -1) {
      return require("../assets/explorer/file_html.svg");
    } else if (FileType["iso"].indexOf(type) !== -1) {
      return require("../assets/explorer/file_iso.svg");
    } else if (FileType["audio"].indexOf(type) !== -1) {
      return require("../assets/explorer/file_music.svg");
    } else if (FileType["pdf"].indexOf(type) !== -1) {
      return require("../assets/explorer/file_pdf.svg");
    } else if (FileType["ppt"].indexOf(type) !== -1) {
      return require("../assets/explorer/file_ppt.svg");
    } else if (FileType["psd"].indexOf(type) !== -1) {
      return require("../assets/explorer/file_psd.svg");
    } else if (FileType["txt"].indexOf(type) !== -1) {
      return require("../assets/explorer/file_txt.svg");
    } else if (FileType["video"].indexOf(type) !== -1) {
      return require("../assets/explorer/file_video.svg");
    } else if (FileType["word"].indexOf(type) !== -1) {
      return require("../assets/explorer/file_word.svg");
    } else if (FileType["zip"].indexOf(type) !== -1) {
      return require("../assets/explorer/file_zip.svg");
    } else {
      return require("../assets/explorer/file_blank.svg");
    }
  }
}

/**
 * 获取全部文件
 * @param folder 主文件夹
 * @param files 文件列表
 */
export function getAllFile(folder, files) {
  for (let key in folder.files) {
    if (!folder.files.hasOwnProperty(key)) continue;
    files[Math.random() + ""] = folder.files[key];
  }
  for (let key in folder.folders) {
    if (!folder.folders.hasOwnProperty(key)) continue;
    getAllFile(folder.folders[key], files)
  }
}

/**
 * 获取全部文件夹
 * @param folder 主文件夹
 * @param folders 文件夹列表
 */
export function getAllFolder(folder, folders) {
  for (let key in folder.folders) {
    if (!folder.folders.hasOwnProperty(key)) continue;
    folders[Math.random() + ""] = folder.folders[key];
    getAllFolder(folder.folders[key], folders)
  }
}

export function findKey(data, value, compare = (a, b) => a === b) {
  return Object.keys(data).find(k => compare(data[k], value))
}
