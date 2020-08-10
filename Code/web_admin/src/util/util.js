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
 * 获取格式化文件大小
 * @param size 文件大小
 * @param fractionDigits 保留小数位数
 * @returns {string} 格式化文件大小
 */
export function getFormatSize(size, fractionDigits = 2) {
  if (size === 0){
    return "0";
  }
  if (null == size || size === '') {
    return "-";
  }
  let unitArr = ["Bytes", "KB", "MB", "GB", "TB", "PB", "EB", "ZB", "YB"];
  let index;
  size = parseFloat(size);
  index = Math.floor(Math.log(size) / Math.log(1024));
  let formatSize = size / Math.pow(1024, index);
  formatSize = formatSize.toFixed(fractionDigits);//保留的小数位数
  return formatSize + unitArr[index];
}
