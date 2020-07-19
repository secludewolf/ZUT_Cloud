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

export function getTypeName(type) {
  return "未知文件"
}

export function getFormatSize(size) {
  if (null == size || size === '') {
    return "0 Bytes";
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
  //TODO 根据文件类型改变图标
  type = type.toLowerCase();
  if (type === "folder") {
    return require("../assets/explorer/folder.svg");
  } else {
    return require("../assets/explorer/file_blank.svg")
  }
}
