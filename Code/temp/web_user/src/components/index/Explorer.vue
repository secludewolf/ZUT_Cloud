<template>
  <a-layout style="height: 100%;background:white;">
    <a-layout-sider id="index-explorer-aside" class="ant-layout-sider-light" style="height: 100%;">
      <a-menu mode="vertical"
              :selectedKeys="key"
              @click="handleClick">
        <a-menu-item key="all">
          <a-icon type="home"/>
          全部文件
        </a-menu-item>
        <a-menu-item key="document">
          <a-icon type="edit"/>
          文档
        </a-menu-item>
        <a-menu-item key="photo">
          <a-icon type="file-image"/>
          图片
        </a-menu-item>
        <a-menu-item key="video">
          <a-icon type="video-camera"/>
          视频
        </a-menu-item>
        <a-menu-item key="bt">
          <a-icon type="link"/>
          种子
        </a-menu-item>
        <a-menu-item key="audio">
          <a-icon type="audio"/>
          音频
        </a-menu-item>
        <a-menu-item key="recycleBin">
          <a-icon type="delete"/>
          回收站
        </a-menu-item>
      </a-menu>
    </a-layout-sider>
    <a-layout-content style="height: 100%;">
      <BaseExplorer :isRepository="isRepository"
                    :isSearch="isSearch"
                    :isRecycleBin="isRecycleBin"
                    :repository="repository"
                    :root="root"
                    :files="files"
                    :folders="folders"
                    @changeIsRepository="changeIsRepository"
                    @changeIsSearch="changeIsSearch"
                    @changeIsRecycleBin="changeIsRecycleBin"
                    @changeRepository="changeRepository"
                    @changeFiles="changeFiles"
                    @changeFolders="changeFolders"/>
    </a-layout-content>
  </a-layout>
</template>

<script>
  import BaseExplorer from "../common/BaseExplorer";
  import {getAllFile, getAllFolder} from "../../util/Utils";
  import {FileType} from "../../util/Const";

  export default {
    name: "Explorer",
    created() {
    }, mounted() {
      this.files = this.repository.folder.files === null ? {} : this.repository.folder.files;
      this.folders = this.repository.folder.folders === null ? {} : this.repository.folder.folders;
      this.root = this.repository.folder;
      this.sortFile();
    },
    components: {
      BaseExplorer
    }, data() {
      return {
        key: ["all"],
        isRepository: true,
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
      handleClick(e) {
        this.key = [e.key];
        let types;
        if (e.key === "all") {
          console.log("全部文件");
          this.changeIsRepository(true);
          this.files = this.repository.folder.files === null ? {} : this.repository.folder.files;
          this.folders = this.repository.folder.folders === null ? {} : this.repository.folder.folders;
          return;
        } else if (e.key === "document") {
          this.changeIsSearch(true);
          console.log("文档");
          types = FileType.document;
        } else if (e.key === "photo") {
          this.changeIsSearch(true);
          console.log("图片");
          types = FileType.photo;
        } else if (e.key === "video") {
          this.changeIsSearch(true);
          console.log("视频");
          types = FileType.video;
        } else if (e.key === "bt") {
          this.changeIsSearch(true);
          console.log("种子");
          types = FileType.torrent;
        } else if (e.key === "audio") {
          this.changeIsSearch(true);
          console.log("音频");
          types = FileType.audio;
        } else if (e.key === "recycleBin") {
          this.changeIsRecycleBin(true);
          console.log("回收站");
          this.files = this.repository.recycleBin.files === null ? {} : this.repository.recycleBin.files;
          this.folders = this.repository.recycleBin.folders === null ? {} : this.repository.recycleBin.folders;
          return;
        }
        let allFiles = {};
        getAllFile(this.root, allFiles);
        let files = {};
        for (let key in allFiles) {
          if (types.indexOf(allFiles[key].type.toLowerCase()) !== -1) {
            files[Math.random()] = allFiles[key];
          }
        }
        this.files = files;
        this.folders = {};
      },
      sortFile() {
        let folders = Object.values(this.folders);
        let files = Object.values(this.files);
        folders.sort((a, b) => a.name > b.name ? 1 : -1);
        files.sort((a, b) => a.name > b.name ? 1 : -1);
        let foldersMap = {};
        let filesMap = {};
        for (const index in folders) {
          foldersMap[folders[index].name] = folders[index];
        }
        for (const index in files) {
          filesMap[files[index].name] = files[index];
        }
        this.files = filesMap;
        this.folders = foldersMap;
      }
    }
  }
</script>

<style>
  #index-explorer-aside .ant-layout-sider-children {
    background: rgb(247, 247, 247);
  }

  #index-explorer-aside .ant-layout-sider-children ul {
    background: rgb(247, 247, 247);
  }
</style>

<style scoped>

</style>
