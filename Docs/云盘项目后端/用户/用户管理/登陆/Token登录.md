# Token登录

- 请求地址：/login/token

- 请求方式：GET

- 请求体编码：无

- 响应体编码：text/json

- 请求头：

  ```json
  Authorization:登录状态Token(string)
  ```

- 返回参数：

  ```json
  {
      code:状态码(int) {1 成功，0 状态异常，-1 失败}
      message:状态说明(string) {成功，账号状态异常，失败}
  	data:{用户信息
          user:{
              id = 用户ID(int)
              account = 用户账号(string)
  			repoId = 仓库ID(int)
              email = 用户邮箱(string)
              phone = 用户手机(string)
              status = 账号状态(int)
  			level = 用户等级(int)
              name = 用户昵称(string)
              createTime = 账号创建时间(long)时间戳
  			changeTime = 账号修改时间(long)时间戳
          }
  		repository:{
              id = 仓库ID(int)
  			userId = 用户ID(int)
              status = 仓库状态(int)
              useSize = 已用空间大小(long)byte
              repoSize = 仓库总空间大小;(long)byte
              folder = 文件根目录(object)
              recycleBin = 回收站目录(object)
          }
      }
  }
  ```

- 流程图：

  ```mermaid
  graph TD
  j4--失败-->用户状态异常-->finish
  start((开始))-->请求参数-->Token解析-->用户ID-->j1{查询用户信息}--成功-->j4{判断用户状态}--成功-->j3{查询用户仓库数据}--成功-->j5{判断Token时效是否过半}--未过半-->JSON封装-->返回数据-->finish{结束}
  j1--失败-->返回Token无效-->finish
  j3--失败-->返回服务器异常-->finish
  ```
```
  
  




```