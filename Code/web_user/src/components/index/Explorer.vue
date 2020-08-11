<template>
  <a-layout style="position:relative;height: 100%;background:white;">
    <a-layout-sider id="index-explorer-aside" class="ant-layout-sider-light"
                    style="position:relative;min-height:450px;height: 100%;">
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
        <a-menu-item key="upload">
          <a-icon type="upload" @click="visible = true"/>
          上传列表
        </a-menu-item>
      </a-menu>
      <a-drawer
        title="上传列表"
        placement="right"
        :closable="false"
        :visible="visible"
        @close="visible = false"
        width="450px"
      >
        <div v-for="(value,index) in upload" :key="index">
          <div>
            <span>{{ value.name }}</span>
            <span v-if="value.status === 'normal'" style="float:right;">核对文件中...</span>
            <span v-else style="float:right;">
              {{ getFormatSize(value.finishSize) }}/{{ getFormatSize(value.fileSize) }}
            </span>
          </div>
          <a-progress :percent="Math.round((value.finishSize / value.fileSize) * 100)" :status="value.status"/>
        </div>
        <a-result v-if="Object.keys(upload).length === 0" title="暂未上传任何文件">
          <template #icon>
            <a-icon type="smile" theme="twoTone"/>
          </template>
        </a-result>
      </a-drawer>
      <div style="position: absolute;text-align:right;bottom: 30px;left:10px;width: 180px;height: 20px;">
        <a-progress :percent="percent" :show-info="false" size="small"/>
        <span>{{ useSize }} / {{ repoSize }}</span>
      </div>
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
  import {getAllFile, getFormatSize} from "../../util/Utils";
  import {FileType} from "../../util/Const";
  import {mapState} from 'vuex';
  import merge from "webpack-merge";

  export default {
    name: "Explorer",
    created() {
    },
    mounted() {
      this.refreshRepository();
      if (this.$route.query.menu == null) {
        this.$router.push({query: merge(this.$route.query, {'menu': 'all'})})
      } else {
        this.handleClick({key: this.$route.query.menu});
      }
    },
    components: {
      BaseExplorer
    }, data() {
      return {
        key: ["all"],
        visible: false,
        isRepository: true,
        isSearch: false,
        isRecycleBin: false,
        root: {},
        files: {},
        folders: {},
      }
    },
    computed: {
      ...mapState(["repository", "upload"]),
      percent() {
        let percent = (this.repository.useSize / this.repository.repoSize) * 100;
        if (percent < 3) {
          percent = 3;
        }
        return percent;
      },
      useSize() {
        return getFormatSize(this.repository.useSize, 0);
      },
      repoSize() {
        return getFormatSize(this.repository.repoSize, 0);
      }
    },
    watch: {
      repository() {
        this.refreshRepository();
        this.handleClick({key: this.$route.query.menu});
      }
    },
    methods: {
      refreshRepository() {
        this.files = this.repository.folder.files === null ? {} : this.repository.folder.files;
        this.folders = this.repository.folder.folders === null ? {} : this.repository.folder.folders;
        this.root = this.repository.folder;
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
        this.$store.commit("updateRepository", repository);
        this.key = ["all"];
        this.$router.push({query: merge(this.$route.query, {'menu': 'all'})})
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
        let types;
        if (e.key === "all") {
          if (this.key[0] === "all") {
            return;
          }
          this.changeIsRepository(true);
          this.files = this.repository.folder.files === null ? {} : this.repository.folder.files;
          this.folders = this.repository.folder.folders === null ? {} : this.repository.folder.folders;
          this.$router.push({query: merge(this.$route.query, {'menu': 'all'})})
          return;
        } else if (e.key === "document") {
          //TODO 搜说结果之间切换没有办法初始化排序
          this.$router.push({query: merge(this.$route.query, {'menu': 'document'})})
          this.changeIsSearch(true);
          types = FileType.document;
        } else if (e.key === "photo") {
          this.$router.push({query: merge(this.$route.query, {'menu': 'photo'})})
          this.changeIsSearch(true);
          types = FileType.photo;
        } else if (e.key === "video") {
          this.$router.push({query: merge(this.$route.query, {'menu': 'video'})})
          this.changeIsSearch(true);
          types = FileType.video;
        } else if (e.key === "bt") {
          this.$router.push({query: merge(this.$route.query, {'menu': 'bt'})})
          this.changeIsSearch(true);
          types = FileType.torrent;
        } else if (e.key === "audio") {
          this.$router.push({query: merge(this.$route.query, {'menu': 'audio'})})
          this.changeIsSearch(true);
          types = FileType.audio;
        } else if (e.key === "recycleBin") {
          this.$router.push({query: merge(this.$route.query, {'menu': 'recycleBin'})})
          this.changeIsRecycleBin(true);
          this.files = this.repository.recycleBin.files === null ? {} : this.repository.recycleBin.files;
          this.folders = this.repository.recycleBin.folders === null ? {} : this.repository.recycleBin.folders;
          this.key = [e.key];
          return;
        } else if (e.key === "upload") {
          this.visible = true;
          return;
        } else {
          this.$router.push({query: merge(this.$route.query, {'menu': 'all'})})
          this.changeIsRepository(true);
          this.files = this.repository.folder.files === null ? {} : this.repository.folder.files;
          this.folders = this.repository.folder.folders === null ? {} : this.repository.folder.folders;
          return;
        }
        this.key = [e.key];
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
      getFormatSize(size) {
        return getFormatSize(size, 0);
      }
    },
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
