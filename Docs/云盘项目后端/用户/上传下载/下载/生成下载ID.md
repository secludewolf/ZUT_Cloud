# 生成下载ID

- 请求地址：/download

- 请求方式：POST

- 请求体编码：text/json

- 响应体编码：text/json

- 请求头：

  ```
  Authorization:登录状态Token(string)
  ```

- 请求参数：

  ```json
  {
      shareId = 分享ID(String){当请求为分享下载时使用}
  	repositoryId = 仓库ID(String){仓库多文件下载}
      fileId = 文件ID(string){当请求为单文件下载时使用}
  	fileName = 文件名(string){当请求为单文件下载时使用}
  	folder = {{当请求为多文件下载时使用}
          files = [{
          	id = 文件ID
          	name = 文件名
      	}]
  		folder = [{
              files = []
  			folder = []
  			name = 文件夹名
          }]
  		name = 文件夹名
      }
  }
  ```

- 返回参数：

  ```json
  {
      code:状态码(int) {1 成功，0 失败}
      message:状态说明(string) {成功，失败}
  	data:{{当分块传输是使用}
          id = 下载ID(string)
      }
  }
  ```

- 流程图：

  ```mermaid
  graph TD
  start((开始))-->Token解析-->TokenID-->j1{查询用户信息}--失败-->返回无下载权限-->finish((结束))
  j1--成功-->j2{判断下载来源}--分享下载-->j3{判断分享状态}--无效-->返回无下载权限
  j3--有效-->j4{判断文件是否属于该仓库}--不属于-->返回无下载权限
  j4--属于-->生成下载ID-->创建下载信息-->存储下载信息-->JSON封装-->返回成功-->finish
  j2--用户下载-->j5{判断用户状态}--无效-->返回无下载权限
  j5--有效-->j4
  start-->请求参数-->分享ID-->j2
  请求参数-->文件ID-->j4
  请求参数-->文件名-->创建下载信息
  请求参数-->文件夹-->j4
  文件夹-->创建下载信息
  ```

  



