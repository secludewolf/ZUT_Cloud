import {Message} from 'element-ui';

export function message(content, type = "success", duration = 3000) {
  Message({
    message: content,
    type: type,
    duration: duration
  })
}
