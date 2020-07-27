<template>
  <a-layout style="position:relative;height: 100%;background:white;">
    <a-layout-content style="height: 100%;">
      <BaseExplorer v-if="repositoryVisible"
                    :isShare="isShare"
                    :isSearch="isSearch"
                    :isRecycleBin="isRecycleBin"
                    :repository="repository"
                    :root="root"
                    :files="files"
                    :folders="folders"
                    :password="password"
                    @changeIsRepository="changeIsRepository"
                    @changeIsSearch="changeIsSearch"
                    @changeIsRecycleBin="changeIsRecycleBin"
                    @changeRepository="changeRepository"
                    @changeFiles="changeFiles"
                    @changeFolders="changeFolders"/>
      <div>
        <a-modal title="分享密码"
                 :visible="passwordVisible"
                 :centered="true"
                 :keyboard="false"
                 :maskClosable="false"
                 :mask="false"
                 :closable="false">
          <div style="position: relative;padding: 10px 0" @keydown.enter="loadData">
            <a-input size="large" placeholder="请输入分享密码" v-model="password"/>
            <span style="position: absolute;left: 0;top:55px;;color: red"
                  v-if="passwordErrorVisible">
              <a-icon type="warning" style="padding: 0 5px;"/>密码错误
            </span>
          </div>
          <template slot="footer">
            <a-button key="submit" type="primary" :loading="loading" @click="loadData">
              确认
            </a-button>
          </template>
        </a-modal>
      </div>
      <a-result :title="resultTitle" v-if="resultVisible">
        <template #extra>
          <router-link to="/index">
            <a-button type="primary">
              返回主页
            </a-button>
          </router-link>
        </template>
      </a-result>
    </a-layout-content>
  </a-layout>
</template>

<script>
  import BaseExplorer from "../common/BaseExplorer";
  import {getShare} from "../../api/share";

  export default {
    name: "Explorer",
    created() {
      //模拟加载数据
      this.loadData();
      // if (this.$route.query["id"] == null) {
      //   this.resultTitle = "分享不存在";
      //   this.resultVisible = true;
      // } else if (this.$route.query["id"] === "1") {
      //   this.resultTitle = "分享已删除";
      //   this.resultVisible = true;
      // } else if (this.$route.query["id"] === "2") {
      //   this.resultTitle = "分享已禁用";
      //   this.resultVisible = true;
      // } else {
      //   this.resultVisible = false;
      //   this.passwordVisible = true;
      // }
    },
    mounted() {
    },
    components: {
      BaseExplorer
    },
    data() {
      return {
        resultVisible: false,
        repositoryVisible: false,
        passwordVisible: false,
        passwordErrorVisible: false,
        resultTitle: "",
        loading: false,
        password: "",
        key: ["all"],
        visible: false,
        isShare: true,
        isSearch: false,
        isRecycleBin: false,
        repository: {
          "id": "5f06b20d17e32b20664aa4c6",
          "userId": 1,
          "status": 1,
          "repoSize": 1099511627776,
          "useSize": 0,
          "folder": {
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
                    "size": 1123,
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
                "size": 1123,
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
          },
          "recycleBin": {"folders": null, "files": null}
        },
        root: {},
        files: {},
        folders: {},
      }
    },
    methods: {
      loadData() {
        this.loading = true;
        const parent = this;
        const data = {
          shareId: this.$route.query["id"],
          password: this.password.length === 0 ? null : this.password
        };
        const handler = function (data) {
          parent.files = parent.repository.folder.files === null ? {} : parent.repository.folder.files;
          parent.folders = parent.repository.folder.folders === null ? {} : parent.repository.folder.folders;
          parent.root = parent.repository.folder;
          parent.passwordVisible = false;
          parent.repositoryVisible = true;
          parent.loading = false;
        };
        const catcher = function (code, content) {
          parent.loading = false;
          if (code === -6 || code === -7) {
            parent.resultVisible = false;
            parent.passwordVisible = true;
          } else {
            parent.$message.warn(content);
            parent.resultTitle = content;
            parent.resultVisible = false;
          }
        };
        getShare(data, handler, catcher);
      },
      changeIsRepository(isRepository) {
        this.isRepository = isRepository;
        this.isSearch = !isRepository;
        this.isRecycleBin = !isRepository;
        this.key = ["all"];
      },
      changeIsSearch(isSearch) {
        this.isSearch = isSearch;
        this.isRecycleBin = !isSearch;
        this.isRepository = !isSearch;
      },
      changeIsRecycleBin(isRecycleBin) {
        this.isRecycleBin = isRecycleBin;
        this.isSearch = !isRecycleBin;
        this.isRepository = !isRecycleBin;
      },
      changeRepository(repository) {
        this.repository = repository;
        this.isRepository = true;
        this.isSearch = false;
        this.isRecycleBin = false;
      },
      changeFiles(files) {
        this.files = files;
      },
      changeFolders(folders) {
        this.folders = folders;
      },
    }
  }
</script>

<style scoped>

</style>
