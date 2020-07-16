<template>
  <div id="ExplorerMenu">
    <ul>
      <li v-on:click="copy" v-if="isCopy">复制</li>
      <li v-on:click="move" v-if="isMove">移动</li>
      <li v-on:click="paste" v-if="isPaste">粘贴</li>
      <li v-on:click="rename" v-if="isRename">重命名</li>
      <li v-on:click="share" v-if="isShareOption">分享</li>
      <li v-on:click="download" v-if="isDownload">下载</li>
      <li v-on:click="restore" v-if="isRestore">恢复</li>
      <li v-on:click="this.delete" v-if="isDelete">删除</li>
    </ul>
    <el-dialog title="重命名" :visible.sync="renameDialogVisible">
      <el-form>
        <el-form-item label="新文件名" label-position="right" label-width="70px">
          <el-input v-model="newName"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="renameDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="rename">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import {message} from "../../util/message";
  import {
    copyFile,
    copyFolder, deleteFromRecycleBin,
    deleteFromRepository,
    moveFile,
    moveFolder,
    renameFile,
    renameFolder, restoreFromRecycleBin
  } from "../../api/repository";
  import {createShare} from "../../api/share";
  import {getDownloadId} from "../../api/download";
  //TODO 回收站删除功能
  export default {
    name: 'ExplorerMenu',
    components: {},
    data() {
      return {
        renameDialogVisible: false,
        repositoryId: null,
        shareId: null,
        recycleId: null,
        folder: null,
        file: null,
        target: null,
        path: null,
        newName: null,
        isFile: false,
        isFolder: false,
        isBackground: false,
        isRepository: false,
        isRecycleBin: false,
        isShare: false,
        isSearch: false,
        lastOption: null
      }
    },
    computed: {
      isCopy: function () {
        return (this.isFile || this.isFolder) && (this.isRepository || this.isSearch)
      },
      isMove: function () {
        return (this.isFile || this.isFolder) && (this.isRepository || this.isSearch)
      },
      isShareOption: function () {
        return (this.isFolder) && (this.isRepository || this.isSearch)
      },
      isPaste: function () {
        return this.isBackground && this.isRepository
      },
      isDownload: function () {
        return this.isFile;
      },
      isRename: function () {
        return (this.isFile || this.isFolder) && (this.isRepository || this.isSearch)
      },
      isDelete: function () {
        return (this.isFile || this.isFolder) && (this.isRepository || this.isSearch)
      },
      isRestore: function () {
        return (this.isFile || this.isFolder) && this.isRecycleBin
      }
    },
    methods: {
      set(key, value) {
        this[key] = value;
      },
      clean() {
        this.isFile = false;
        this.isFolder = false;
        this.isBackground = false;
        this.isRepository = false;
        this.isRecycleBin = false;
        this.isShare = false;
        this.isSearch = false;
        this.file = null;
        this.folder = null;
      },
      copy() {
        console.log("复制");
        if (this.file != null) {
          this.target = {
            target: this.file,
            type: "file"
          };
        } else {
          this.target = {
            target: this.folder,
            type: "folder"
          };
        }
        this.lastOption = "复制";
        console.log("选中:" + this.target.target.name);
      },
      move() {
        console.log("移动");
        if (this.file != null) {
          this.target = {
            target: this.file,
            type: "file"
          };
        } else {
          this.target = {
            target: this.folder,
            type: "folder"
          };
        }
        this.lastOption = "移动";
        console.log("选中:" + this.target.target.name);
      },
      paste() {
        console.log("粘贴");
        if (this.target === null) {
          console.log("未选择文件");
          message("未选择文件!", "warning");
        } else {
          const data = {
            repositoryId: this.repositoryId,
            name: this.target.target.name,
            oldPath: this.target.target.path,
            newPath: this.path
          };
          const handler = (data) => {
            this.$emit("changeRepository", data.repository);
            message("操作成功");
          };
          const catcher = (code, content) => {
            message(content, "warning");
          };
          //TODO 刷新仓库信息
          if (this.target.type === "file") {
            if (this.lastOption === "复制") {
              copyFile(data, handler, catcher);
            } else if (this.lastOption === "移动") {
              moveFile(data, handler, catcher);
            }
          } else if (this.target.type === "folder") {
            if (this.lastOption === "复制") {
              copyFolder(data, handler, catcher);
            } else if (this.lastOption === "移动") {
              moveFolder(data, handler, catcher);
            }
          }
          console.log("将" + this.target.target.name + "从" + this.target.target.path + this.lastOption + "至" + this.path);
        }
      },
      rename() {
        console.log("重命名");
        if (this.file != null) {
          this.target = {
            target: this.file,
            type: "file"
          };
        } else {
          this.target = {
            target: this.folder,
            type: "folder"
          };
        }
        this.$prompt('请输入新名称', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
        }).then(({value}) => {
          console.log(1);
          this.newName = value;
          const data = {
            repositoryId: this.repositoryId,
            oldName: this.target.target.name,
            newName: this.newName,
            path: this.target.target.path
          };
          const handler = (data) => {
            this.$emit("changeRepository", data.repository);
            message("操作成功");
          };
          const catcher = (code, content) => {
            message(content, "warning");
          };
          if (this.target.type === "file") {
            renameFile(data, handler, catcher);
          } else if (this.target.type === "folder") {
            renameFolder(data, handler, catcher);
          }
          console.log("将位于" + data.path + "的" + data.oldName + "修改为" + data.newName);
          this.renameDialogVisible = false;
          this.newName = null;
        }).catch((error) => {
          console.log(error);
          message('取消输入', 'info');
        });
      },
      download() {
        console.log("下载");
        console.log("下载位于" + this.file.path + "的" + this.file.name);
        const data = {
          repositoryId: null,
          userFileId: this.file.userFileId,
          fileName: this.file.name,
          shareId: null,
          folder: null,//暂不支持多文件下载
        };
        if (this.isRepository) {
          data.repositoryId = this.repositoryId
        } else if (this.isShare) {
          data.shareId = this.shareId;
        }
        const handler = (data) => {
          console.log(data.id);
          window.open("/api/download/" + data.id, '_blank');
        }
        const catcher = (code, content) => {
          message(content, "warning");
        }
        getDownloadId(data, handler, catcher);
      },
      restore() {
        console.log("恢复");
        if (this.file != null) {
          this.target = {
            target: this.file,
            type: "file"
          };
        } else {
          this.target = {
            target: this.folder,
            type: "folder"
          };
        }
        const data = {
          repositoryId: this.repositoryId,
          isFile: this.target.type === "file",
          recycleId: this.recycleId
        };
        const handler = (data) => {
          this.$emit("changeRepository", data.repository);
          message("操作成功");
        };
        const catcher = (code, content) => {
          message(content, "warning");
        };
        restoreFromRecycleBin(data, handler, catcher);
      },
      delete() {
        console.log("删除");
        if (this.file != null) {
          this.target = {
            target: this.file,
            type: "file"
          };
        } else {
          this.target = {
            target: this.folder,
            type: "folder"
          };
        }
        const data = {
          repositoryId: this.repositoryId,
          isFile: this.target.type === 'file',
        };
        const handler = (data) => {
          this.$emit("changeRepository", data.repository);
          message("操作成功");
        };
        const catcher = (code, content) => {
          message(content, "warning");
        };
        if (this.isRepository) {
          data.name = this.target.target.name;
          data.path = this.target.target.path;
          deleteFromRepository(data, handler, catcher);
        } else if (this.isRecycleBin) {
          data.recycleId = this.recycleId;
          deleteFromRecycleBin(data, handler, catcher);
        }
      },
      share() {
        //TODO 没有设置标题
        //TODO 没有设置有效期
        this.target = this.folder;
        this.$prompt('请输入分享密码', '创建分享', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          inputPattern: /^\d{6}$/,
          inputErrorMessage: '密码必须为六位数字'
        }).then(({value}) => {
          const data = {
            repositoryId: this.repositoryId,
            name: this.target.name,
            path: this.target.path + "/" + this.target.name,
            password: this.password,
            validTime: 9995367437536
          };
          const handler = (data) => {
            this.$alert('<a href="/share?id=' + data.share.id + '" target="_blank">' + data.share.name + '</a>', '创建成功', {
              dangerouslyUseHTMLString: true
            });
          };
          const catcher = (code, content) => {
            message(content, "warning");
          };
          createShare(data, handler, catcher);
        }).catch((error) => {
          console.log(error);
        });
      },
    }
  }
</script>

<style scoped>
  #ExplorerMenu {
    background: white;
    border-radius: 10px;
    color: #5b667b;
    font-size: 14px;
    border: 1px solid #dde0e4;
    box-shadow: 0 0 8px #ccc;
    text-align: center;
  }

  ul {
    padding: 0;
    margin: 5px 0;
  }

  li {
    list-style: none;
    padding: 5px 20px;
    margin: 0;
  }

  li:hover {
    background-color: rgb(66, 129, 224);
    color: white;
  }
</style>
