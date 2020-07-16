import {message as Message} from "ant-design-vue";


export function message(content, type = "success", duration = 3) {
  switch (type) {
    case "success":
      Message.success(content, duration);
      break;
    case "error":
      Message.error(content, duration);
      break;
    case "warning":
      Message.warning(content, duration);
      break;
    case "info":
      Message.info(content, duration);
      break;
  }
}
