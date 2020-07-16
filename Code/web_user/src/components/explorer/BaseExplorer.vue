<template>
  <el-container id="BaseExplorer" style="height: 100%;margin: 0;padding: 0"
                v-on:click.native="closeMenu"
                v-on:contextmenu.prevent.native="function() {}">
    <el-header height="60px" style="margin: 0;padding: 10px;">
      <el-button type="primary" style="position: relative;" v-if="isRepository">
        <i class="el-icon-upload"></i>
        <span>上传</span>
        <form>
          <input style="position: absolute;top: 0;bottom: 0;left: 0;right: 0;height: 40px;width: 88px;opacity: 0;"
                 type="file" v-on:change="upload($event)" multiple="true"/>
        </form>
      </el-button>
      <el-button icon="el-icon-folder-add" v-on:click="createDialogVisible = true" v-if="isRepository">新建文件夹</el-button>
      <el-button type="primary" icon="el-icon-folder-add" v-on:click="saveShare" v-if="isShare">保存分享</el-button>
      <el-button type="primary" icon="el-icon-delete" v-on:click="cleanRecycleBin" v-if="isRecycleBin">清空回收站
      </el-button>
      <el-dialog title="创建文件夹" :visible.sync="createDialogVisible">
        <el-form>
          <el-form-item label="文件夹名称" label-position="right" label-width="100px">
            <el-input v-model="folderName"></el-input>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="createDialogVisible = false">取 消</el-button>
          <el-button type="primary" @click="createFolder">确 定</el-button>
        </div>
      </el-dialog>
      <div style="display: inline-block;width: 300px;float: right;">
        <el-input type="text" v-model="searchValue" placeholder="请输入文件名称" v-on:change="search">
          <el-button slot="append" icon="el-icon-search"></el-button>
        </el-input>
      </div>
    </el-header>
    <el-container style="height: 100%;margin: 0;padding: 0">
      <el-header height="20px" style="margin: 0;padding: 0">
        <div class="path" style="height: 20px">
          <ul style="display: inline;padding: 0 0 0 10px;color: #09AAFF;font-size: 12px;">
            <li style="display: inline" v-if="path.length !== 1">
              <span v-on:click="lastParent" style="cursor:pointer"><a>返回上一级</a></span>
              <span style="margin: 0 3px;">|</span>
            </li>
            <li style="display: inline" v-for="(value,index) in path" :key="'path-'+index">
              <span v-on:click="selectParent(index)" style="cursor:pointer "><a>{{value}}</a></span>
              <span style="margin: 0 3px;" v-if="index < path.length - 1">></span>
            </li>
            <li style="display: inline;float:right;color: #666;padding-right: 10px">
              <span class="number">已全部加载，共<span> {{total}} </span>个</span>
            </li>
          </ul>
        </div>
      </el-header>
      <el-main style="position: absolute;top:82px;bottom:0;left:0;right: 0;margin: 0;padding: 0"
               v-on:contextmenu.prevent.native="showMenu($event,-1)">
        <div style="font-size: 12px">
          <div id="folder"
               class="base"
               style="float: left;height: 120px;width: 120px;margin: 5px;border-radius: 10px;text-align: center;cursor:pointer"
               v-for="(value,index) in folders"
               :key="'folder-'+index"
               v-on:dblclick="openFolder(index)"
               v-on:contextmenu.prevent.stop="showMenu($event,index)">
            <div style="width: 80px;height: 80px;margin: 10px 20px 0;display: flex;">
              <img style="margin: auto;width: auto; height: auto;max-width: 100%;max-height: 100%;"
                   src="../../assets/folder.png" alt="folder">
            </div>
            <span>{{value.name}}</span>
          </div>
          <div id="file"
               class="base"
               style="float: left;height: 120px;width: 120px;margin: 5px;border-radius: 10px;text-align: center;"
               v-for="(value,index) in files"
               :key="'file-'+index"
               v-on:contextmenu.prevent.stop="showMenu($event,index)">
            <div style="width: 80px;height: 80px;margin: 10px 20px 0;display: flex;">
              <img style="margin: auto;width: auto; height: auto;max-width: 100%;max-height: 100%;"
                   src="../../assets/file.png" alt="file">
            </div>
            <span>{{value.name}}</span>
          </div>
        </div>
      </el-main>
    </el-container>
    <Menu ref="ExplorerMenu" style="position: fixed;"
          v-bind:style="{display:menuDisplay,left:menuX,top:menuY}"
          @changeRepository="changeRepository"></Menu>
  </el-container>
</template>

<script>
  import Menu from "./Menu";
  import {cleanRecycleBin, createFile, createFolder} from "../../api/repository";
  import {message} from "../../util/message";
  import {saveShare} from "../../api/share";
  import {getMd5, getMd5BigFile, getMd5SliceFile, getMd5SmallFile} from "../../util/util";
  import {uploadBigFile, uploadSmallFile} from "../../api/upload";
  //TODO 将UserID与RepositoryID设置为共享变量
  export default {
    name: 'BaseExplorer',
    components: {
      Menu
    },
    props: {
      isRepository: {
        type: Boolean,
        default: true
      },
      isShare: {
        type: Boolean,
        default: false
      },
      isSearch: {
        type: Boolean,
        default: false
      },
      isRecycleBin: {
        type: Boolean,
        default: false
      },
      repository: {
        type: Object,
        default: () => {
        }
      },
      root: {
        type: Object,
        default: () => {
        }
      },
      folders: {
        type: Object,
        default: () => {
        }
      },
      files: {
        type: Object,
        default: () => {
        }
      },
      password: {
        type: String,
        default: null
      }
    },
    mounted() {

    },
    data() {
      return {
        path: ["全部文件"],
        folderName: "",
        createDialogVisible: false,
        searchValue: "",
        menuDisplay: "none",
        menuX: "200px",
        menuY: "200px"
      }
    },
    computed: {
      total: function () {
        return Object.keys(this.files).length + Object.keys(this.folders).length;
      }
    },
    methods: {
      //TODO 优化上传方法结构
      //TODO 可视化下载进度
      upload: function (event) {
        console.log("上传操作");
        const repositoryId = this.repository.id;
        const changeRepository = this.changeRepository;
        let path = "/root";
        //获取当前位置
        for (let i = 1; i < this.path.length; i++) {
          path = path + "/" + this.path[i];
        }
        //获取文件列表
        const files = event.target.files;
        for (let i = 0; i < files.length; i++) {
          //大文件上传
          if (files[i].size > 1024 * 1024 * 5) {
            const next = (md5) => {
              console.log(md5);
              const data = {
                repositoryId: this.repository.id,
                fileId: md5,
                name: files[i].name,
                path: path
              };
              const handler = (data) => {
                this.changeRepository(data.repository);
                message("秒传成功");
              };
              const catcher = (code, content) => {
                let chunkSize = 1024 * 1024 * 5,
                  chunks = Math.ceil(files[i].size / chunkSize),
                  currentChunk = 0,
                  fileReader = new FileReader();
                let start;
                let end;
                fileReader.onload = function (event) {
                  let blockMd5 = getMd5SliceFile(event);
                  console.log(blockMd5);
                  //TODO 忽略了返回的已接收分片数据
                  //上传文件分片
                  uploadBigFile(files[i].slice(start, end), files[i].name, blockMd5, md5, currentChunk, chunks, (data) => {
                    //如果上传完成则尝试保存文件
                    if (data.code === 1 && data.data === null) {
                      const data = {
                        repositoryId: repositoryId,
                        fileId: md5,
                        name: files[i].name,
                        path: path
                      };
                      const handler = (data) => {
                        changeRepository(data.repository);
                        message("上传成功");
                      };
                      const catcher = (code, content) => {
                        message("网络异常,请重试!");
                      };
                      createFile(data, handler, catcher);
                    } else {
                      //TODO 暂时无法并发上传,redis没有写同步锁
                      currentChunk += 1;
                      if (currentChunk < chunks) {
                        loadNext();
                      }
                    }
                  })
                }

                function loadNext() {
                  start = currentChunk * chunkSize
                  end = start + chunkSize >= files[i].size ? files[i].size : start + chunkSize;
                  fileReader.readAsBinaryString(File.prototype.slice.call(files[i], start, end));
                }

                loadNext();
              }
              createFile(data, handler, catcher);
            };
            getMd5BigFile(files[i], next);
          }
          //小文件上传
          else {
            const next = (md5) => {
              console.log(md5);
              const data = {
                repositoryId: this.repository.id,
                fileId: md5,
                name: files[i].name,
                path: path
              };
              const handler = (data) => {
                this.changeRepository(data.repository);
                message("秒传成功");
              };
              const catcher = (code, content) => {
                uploadSmallFile(files[i], files[i].name, md5, () => {
                  const data = {
                    repositoryId: this.repository.id,
                    fileId: md5,
                    name: files[i].name,
                    path: path
                  };
                  const handler = (data) => {
                    this.changeRepository(data.repository);
                    message("上传成功");
                  };
                  const catcher = (code, content) => {
                    message("网络异常,请重试!");
                  };
                  createFile(data, handler, catcher);
                });
              };
              createFile(data, handler, catcher);
            };
            getMd5SmallFile(files[i], next);
          }
        }
      },
      createFolder: function () {
        console.log("创建文件夹操作");
        let folder = this.root;
        for (let i = 1; i <= this.path.length - 1; i++) {
          folder = folder.folders[this.path[i]];
        }
        if (this.folderName == null || this.folderName === "" || this.folderName.length === 0) {
          message("名称不合法", "warning");
          return;
        }
        const path = folder.path + "/" + folder.name;
        const data = {
          repositoryId: this.repository.id,
          name: this.folderName,
          path: path
        };
        const handler = (data) => {
          this.changeRepository(data.repository);
          this.createDialogVisible = false;
          this.folderName = "";
        };
        const catcher = (code, content) => {
          message(content, "warning");
        };
        createFolder(data, handler, catcher);
      },
      search: function () {
        if (this.searchValue === null || this.searchValue.length === 0) {
          return;
        }
        let allFiles = {};
        let allFolders = {};
        this.getFile(this.root, allFiles);
        this.getFolder(this.root, allFolders);
        let files = {};
        let folders = {};
        for (let key in allFiles) {
          if (allFiles[key].name.indexOf(this.searchValue) !== -1) {
            files[Math.random()] = allFiles[key];
          }
        }
        for (let key in allFolders) {
          if (allFolders[key].name.indexOf(this.searchValue) !== -1) {
            folders[Math.random()] = allFolders[key];
          }
        }
        this.$emit("changeFiles", files);
        this.$emit("changeFolders", folders);
      },
      saveShare: function () {
        console.log("保存分享");
        let folder = this.root;
        for (let i = 1; i <= this.path.length - 1; i++) {
          folder = folder.folders[this.path[i]];
        }
        const path = folder.path + "/" + folder.name;
        const data = {
          shareId: this.repository.shareId,
          password: this.password,
          path: path
        };
        const handler = () => {
          message("保存成功")
        };
        const catcher = (code, content) => {
          message(content, "warning");
        };
        saveShare(data, handler, catcher);
      },
      cleanRecycleBin: function () {
        console.log("清空回收站");
        const data = {
          repositoryId: this.repository.id,
        };
        const handler = (data) => {
          this.$emit("changeRecycleBin", data.repository);
        };
        const catcher = (code, content) => {
          message(content, "warning");
        };
        cleanRecycleBin(data, handler, catcher);
      },
      openFolder: function (index) {
        if (this.folders[index].files == null) {
          this.folders[index].files = {}
        }
        if (this.folders[index].folders == null) {
          this.folders[index].folders = {}
        }
        this.$emit("changeFiles", this.folders[index].files);
        this.$emit("changeFolders", this.folders[index].folders);
        this.path.push(index);
      },
      lastParent: function () {
        this.selectParent(this.path.length - 2);
      },
      selectParent: function (index) {
        if (!this.isRepository && !this.isShare) return;
        let folders = this.root.folders;
        let files = this.root.files;
        let path = ["全部文件"];
        for (let i = 1; i <= index; i++) {
          files = folders[this.path[i]].files;
          folders = folders[this.path[i]].folders;
          path.push(this.path[i]);
        }
        this.$emit("changeFiles", files);
        this.$emit("changeFolders", folders);
        this.path = path;
      }
      , showMenu: function (event, index) {
        this.closeMenu(event);
        let x = event.clientX + "px";
        let y = event.clientY + "px";
        let flag = -1;
        // let index = -1;
        for (let i = 0; i < event.path.length; i++) {
          if (event.path[i].id === "folder") {
            flag = 1;
            break;
          } else if (event.path[i].id === "file") {
            flag = 2;
            break;
          } else {
            flag = 0;
          }
        }
        if (flag === 1) {//文件夹菜单
          this.$refs.ExplorerMenu.set("isFolder", true);
          this.$refs.ExplorerMenu.set("folder", this.folders[index]);
          this.$refs.ExplorerMenu.set("recycleId", index);
        } else if (flag === 2) {//文件菜单
          this.$refs.ExplorerMenu.set("isFile", true);
          this.$refs.ExplorerMenu.set("file", this.files[index]);
          this.$refs.ExplorerMenu.set("recycleId", index);
        } else {//空白菜单
          if (this.isRecycleBin || this.isShare || this.isSearch) {
            return;
          }
          let path = "/root";
          for (let i = 1; i < this.path.length; i++) {
            path = path + "/" + this.path[i];
          }
          this.$refs.ExplorerMenu.set("path", path);
          this.$refs.ExplorerMenu.set("isBackground", true);
        }
        this.$refs.ExplorerMenu.set("isRepository", this.isRepository);
        this.$refs.ExplorerMenu.set("isRecycle", this.isRecycleBin);
        this.$refs.ExplorerMenu.set("isShare", this.isShare);
        this.$refs.ExplorerMenu.set("isRecycleBin", this.isRecycleBin);
        this.$refs.ExplorerMenu.set("repositoryId", this.repository.id);
        this.$refs.ExplorerMenu.set("shareId", this.shareId);
        this.menuX = x;
        this.menuY = y;
        this.menuDisplay = "block";
      }
      , closeMenu: function (event) {
        this.$refs.ExplorerMenu.clean();
        for (let i = 0; i < event.path.length; i++) {
          if (event.path[i].id === "ExplorerMenu") {
            return
          }
        }
        this.menuDisplay = "none"
      },
      getFile: function (folder, files) {
        for (let key in folder.files) {
          if (!folder.files.hasOwnProperty(key)) continue;
          //防止出现重名文件
          files[Math.random() + ""] = folder.files[key];
        }
        for (let key in folder.folders) {
          if (!folder.folders.hasOwnProperty(key)) continue;
          this.getFile(folder.folders[key], files)
        }
      },
      getFolder: function (folder, folders) {
        for (let key in folder.folders) {
          if (!folder.folders.hasOwnProperty(key)) continue;
          folders[Math.random() + ""] = folder.folders[key];
          this.getFile(folder.folders[key], folders)
        }
      },
      changeRepository(repository) {
        this.path = ["全部文件"];
        this.$emit("changeRepository", repository);
      }
    }
  }
</script>

<style scoped>
  .base:hover {
    background-color: rgb(204, 232, 255);
  }
</style>
