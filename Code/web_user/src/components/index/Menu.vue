<template>
  <el-menu
    :default-active="activeIndex"
    background-color="#EAEAEA"
    text-color="#999"
    active-text-color="#09AAFF">
    <el-menu-item index="1" v-on:click="selectRoot">
      <i class="el-icon-s-home"></i>
      <span slot="title">全部文件</span>
    </el-menu-item>
    <el-menu-item index="2" v-on:click="selectPhoto">
      <i class="el-icon-picture-outline"></i>
      <span slot="title">图片</span>
    </el-menu-item>
    <el-menu-item index="3" v-on:click="selectDocument">
      <i class="el-icon-document"></i>
      <span slot="title">文档</span>
    </el-menu-item>
    <el-menu-item index="4" v-on:click="selectVideo">
      <i class="el-icon-video-camera"></i>
      <span slot="title">视频</span>
    </el-menu-item>
    <el-menu-item index="5" v-on:click="selectMagnet">
      <i class="el-icon-attract"></i>
      <span slot="title">种子</span>
    </el-menu-item>
    <el-menu-item index="6" v-on:click="selectMusic">
      <i class="el-icon-headset"></i>
      <span slot="title">音乐</span>
    </el-menu-item>
    <el-menu-item index="8" v-on:click="selectRecycleBin">
      <i class="el-icon-delete"></i>
      <span slot="title">回收站</span>
    </el-menu-item>
  </el-menu>
</template>

<script>
  export default {
    name: "Menu",
    props: {
      isRepository: {
        type: Boolean,
        default: true
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
      folders: {
        type: Object,
        default: () => {
        }
      },
      files: {
        type: Object,
        default: () => {
        }
      }
    }, data() {
      return {
        activeIndex: '1'
      }
    },
    methods: {
      selectRoot: function () {
        console.log("全部文件");
        this.$emit("changeIsRepository", true);
        this.$emit("changeFiles", this.repository.folder.files);
        this.$emit("changeFolders", this.repository.folder.folders);
      },
      selectPhoto: function () {
        console.log("图片");
        let files = {};
        this.getFile(this.repository.folder, ["webp", "bmp", "pcx", "tif", "gif", "jpeg", "tga", "exif", "fpx", "svg", "psd", "sdr", "pcd", "dxf", "ufo", "eps", "png", "hdri", "raw", "wmf", "flic", "emf", "ico"], files);
        this.$emit("changeIsSearch", true);
        this.$emit("changeFiles", files);
        this.$emit("changeFolders", {});
      },
      selectDocument: function () {
        console.log("文档");
        let files = {};
        this.getFile(this.repository.folder, ["doc", "docx", "xlsx", "xls", "csv", "pdf", "txt", "ppt", "pptx"], files);
        this.$emit("changeIsSearch", true);
        this.$emit("changeFiles", files);
        this.$emit("changeFolders", {});
      },
      selectVideo: function () {
        console.log("视频");
        let files = {};
        this.getFile(this.repository.folder, ["mp4", "mov", "avi", "flv", "wmv", "mpeg", "mkv", "asf", "rm", "rmvb", "vob", "ts", "dat"], files);
        this.$emit("changeIsSearch", true);
        this.$emit("changeFiles", files);
        this.$emit("changeFolders", {});
      },
      selectMagnet: function () {
        console.log("种子");
        let files = {};
        this.getFile(this.repository.folder, ["torrent"], files);
        this.$emit("changeIsSearch", true);
        this.$emit("changeFiles", files);
        this.$emit("changeFolders", {});
      },
      selectMusic: function () {
        console.log("音乐");
        let files = {};
        this.getFile(this.repository.folder, ["mp3", "wma", "avi", "rm", "rmvb", "awv", "flac", "wav", "wma"], files);
        this.$emit("changeIsSearch", true);
        this.$emit("changeFiles", files);
        this.$emit("changeFolders", {});
      },
      selectRecycleBin: function () {
        console.log("回收站");
        this.$emit("changeIsRecycleBin", true);
        this.$emit("changeFiles", this.repository.recycleBin.files == null ? {} : this.repository.recycleBin.files);
        this.$emit("changeFolders", this.repository.recycleBin.folders == null ? {} : this.repository.recycleBin.folders);
      },
      getFile: function (folder, types, files) {
        for (let key in folder.files) {
          if (!folder.files.hasOwnProperty(key)) continue;
          if (types.indexOf(folder.files[key].type.toLowerCase()) !== -1) {
            //防止出现重名文件
            files[Math.random() + ""] = folder.files[key];
          }
        }
        for (let key in folder.folders) {
          if (!folder.folders.hasOwnProperty(key)) continue;
          this.getFile(folder.folders[key], types, files)
        }
      }
    }
  }
</script>

<style scoped>
  .el-menu {
    position: absolute;
    top: 0;
    left: 0;
    bottom: 0;
    right: 0;
  }
</style>
