<template>
  <el-container class="share">
    <el-header>
      <Header v-bind:index="2+''" :name=user.name></Header>
    </el-header>
    <el-main>
      <BaseExplorer v-if="loadShareStatus"
                    :isRepository=isRepository
                    :isShare=isShare
                    :isRecycleBin=isRecycleBin
                    :isSearch=isSearch
                    :password=password
                    :repository=repository
                    :root=root
                    :folders=folders
                    :files=files
                    @changeIsRepository="changeIsRepository"
                    @changeIsShare="changeIsShare"
                    @changeIsRecycleBin="changeIsRecycleBin"
                    @changeIsSearch="changeIsSearch"
                    @changeFolders="changeFolders"
                    @changeFiles="changeFiles"></BaseExplorer>
    </el-main>
    <el-dialog title="分享密码" :visible.sync="dialogVisible" width="500px">
      <el-form>
        <el-form-item label="分享密码" label-position="right" label-width="70px">
          <el-input v-model="password"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="cancel">取 消</el-button>
        <el-button type="primary" @click="loadShare">确 定</el-button>
      </div>
    </el-dialog>
  </el-container>
</template>

<script>
  import Header from "@/components/common/Header";
  import Menu from "@/components/index/Menu";
  import BaseExplorer from "@/components/explorer/BaseExplorer";
  import {loginByToken} from "../api/user";
  import {message} from "../util/message";
  import {getShare} from "../api/share";

  export default {
    name: "Share",
    created() {
      //TODO 将Repository和User变为共享数据,防止请求次数过多
      if (this.$route.params.user != null && this.$route.params.repository != null) {
        this.user = this.$route.params.user;
      } else {
        const handler = (data) => {
          this.user = data.user;
        };
        const catcher = () => {
        };
        loginByToken(handler);
      }
    },
    mounted() {
      console.log(this.$route.query.id);
      if (this.$route.query.id == null) {
        this.$router.push("/ShareError");
      } else {
        this.shareId = this.$route.query.id;
        this.dialogVisible = true;
      }
    },
    components: {
      Header,
      Menu,
      BaseExplorer
    },
    data() {
      return {
        activeIndex: '1',
        dialogVisible: false,
        password: null,
        shareId: "",
        loadShareStatus: false,
        user: {},
        repository: {
          id: "",
          folder: {
            "name": "root",
            "path": "",
            "depth": 0,
            "createTime": 1593423709223,
            "changeTime": 1593423709223,
            "status": 1,
            "folders": {
              "folder2": {
                "name": "folder2",
                "path": "/root",
                "depth": 1,
                "createTime": 1593423709223,
                "changeTime": 1593423709223,
                "status": 1,
                "folders": null,
                "files": {
                  "file4.txt": {
                    "id": "file4",
                    "userFileId": 4,
                    "shareIdList": null,
                    "name": "file4.txt",
                    "path": "/root/folder2",
                    "type": "txt",
                    "size": 1,
                    "status": 1,
                    "createTime": 1593423709223,
                    "changeTime": 1593423709223
                  }
                }
              },
              "folder1": {
                "name": "folder1",
                "path": "/root",
                "depth": 1,
                "createTime": 1593423709223,
                "changeTime": 1593423709223,
                "status": 1,
                "folders": {
                  "folder3": {
                    "name": "folder3",
                    "path": "/root/folder1",
                    "depth": 1,
                    "createTime": 1593423709223,
                    "changeTime": 1593423709223,
                    "status": 1,
                    "folders": null,
                    "files": {
                      "file5.txt": {
                        "id": "file5",
                        "userFileId": 5,
                        "shareIdList": null,
                        "name": "file5.txt",
                        "path": "/root/folder1/folder3",
                        "type": "txt",
                        "size": 1,
                        "status": 1,
                        "createTime": 1593423709223,
                        "changeTime": 1593423709223
                      }
                    }
                  }
                },
                "files": {
                  "file3.txt": {
                    "id": "file3",
                    "userFileId": 3,
                    "shareIdList": null,
                    "name": "file3.txt",
                    "path": "/root/folder1",
                    "type": "txt",
                    "size": 1,
                    "status": 1,
                    "createTime": 1593423709223,
                    "changeTime": 1593423709223
                  }
                }
              }
            },
            "files": {
              "file1.txt": {
                "id": "file1",
                "userFileId": 1,
                "shareIdList": null,
                "name": "file1.txt",
                "path": "/root",
                "type": "txt",
                "size": 1,
                "status": 1,
                "createTime": 1593423709223,
                "changeTime": 1593423709223
              },
              "file2.txt": {
                "id": "file2",
                "userFileId": 2,
                "shareIdList": null,
                "name": "file2.txt",
                "path": "/root",
                "type": "txt",
                "size": 1,
                "status": 1,
                "createTime": 1593423709223,
                "changeTime": 1593423709223
              }
            }
          }
        },
        isRepository: false,
        isShare: true,
        isSearch: false,
        isRecycleBin: false,
        root: {},
        folders: {},
        files: {}
      }
    },
    methods: {
      loadShare() {
        const data = {
          shareId: this.shareId,
          password: this.password
        };
        const handler = (data) => {
          this.repository = data.shareRepository;
          this.root = this.repository.folder;
          this.folders = this.root.folders;
          this.files = this.root.files;
          this.loadShareStatus = true;
          this.dialogVisible = false;
        };
        const catcher = (code, content) => {
          message(content, "warning")
        };
        getShare(data, handler, catcher);
      },
      cancel() {
        this.dialogVisible = false;
        this.$router.push("/index");
      },
      changeIsRepository(isRepository) {
        this.isRepository = isRepository;
        this.isShare = !isRepository;
        this.isRecycleBin = !isRepository;
        this.isSearch = !isRepository;
      },
      changeIsShare(isShare) {
        this.isShare = isShare;
        this.isRepository = !isShare;
        this.isRecycleBin = !isShare;
        this.isSearch = !isShare;
      },
      changeIsRecycleBin(isRecycleBin) {
        this.isRecycleBin = isRecycleBin;
        this.isShare = !isRecycleBin;
        this.isRepository = !isRecycleBin;
        this.isSearch = !isRecycleBin;
      },
      changeIsSearch(isSearch) {
        this.isSearch = isSearch;
        this.isRecycleBin = !isSearch;
        this.isShare = !isSearch;
        this.isRepository = !isSearch;
      },
      changeFolders(folders) {
        this.folders = folders;
      },
      changeFiles(files) {
        this.files = files
      }
    }
  }
</script>

<style scoped>
  span {
    -webkit-user-select: none;
    -moz-user-select: none;
    -ms-user-select: none;
    user-select: none;
    -webkit-user-drag: none;
  }

  .share {
    height: 100vh;
  }

  .el-container {
    position: relative;
    margin: 0;
    padding: 0;
  }

  .el-header {
    position: relative;
    margin: 0;
    padding: 0;
    font-size: 16px;
    box-shadow: 0 2px 4px rgba(0, 0, 0, .12), 0 0 6px rgba(0, 0, 0, .04)
  }

  .el-main {
    position: relative;
    margin: 0;
    padding: 0;
  }

  .el-header div {
    display: inline-block;
  }

  .el-header img {
    float: left;
    margin: 0 55px;
    width: auto;
    height: auto;
    max-width: 100%;
    max-height: 100%;
  }

  .el-menu-item {
    font-size: 16px;
  }

  .el-header ul {
    padding-left: 0;
  }

  .el-header li {
    display: inline;
    padding: 0 50px;
  }

  .el-header .user {
    float: right;
  }


</style>
