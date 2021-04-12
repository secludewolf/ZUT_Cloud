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
    if (sessionStorage.getItem(this.$route.query["id"]) != null)
      this.autoLoadData();
    else
      this.passwordVisible = true;
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
      repository: {},
      root: {},
      files: {},
      folders: {},
    }
  },
  methods: {
    autoLoadData() {
      const parent = this;
      const data = {
        shareId: this.$route.query["id"],
        password: sessionStorage.getItem(this.$route.query["id"])
      };
      const handler = function (data) {
        parent.files = data.shareRepository.folder.files === null ? {} : data.shareRepository.folder.files;
        parent.folders = data.shareRepository.folder.folders === null ? {} : data.shareRepository.folder.folders;
        parent.repository = data.shareRepository;
        parent.root = data.shareRepository.folder;
        parent.passwordVisible = false;
        parent.repositoryVisible = true;
        parent.loading = false;
      };
      const catcher = function () {};
      getShare(data, handler, catcher);
    },
    loadData() {
      this.loading = true;
      const parent = this;
      const data = {
        shareId: this.$route.query["id"],
        password: this.password.length === 0 ? null : this.password
      };
      const handler = function (data) {
        parent.files = data.shareRepository.folder.files === null ? {} : data.shareRepository.folder.files;
        parent.folders = data.shareRepository.folder.folders === null ? {} : data.shareRepository.folder.folders;
        parent.repository = data.shareRepository;
        parent.root = data.shareRepository.folder;
        parent.passwordVisible = false;
        parent.repositoryVisible = true;
        parent.loading = false;
        sessionStorage.setItem(parent.$route.query["id"], parent.password);
      };
      const catcher = function (code, content) {
        parent.loading = false;
        if (code === -6 || code === -7) {
          parent.resultVisible = false;
          parent.passwordVisible = true;
          parent.passwordErrorVisible = true;
        } else {
          parent.resultTitle = content;
          parent.resultVisible = true;
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
