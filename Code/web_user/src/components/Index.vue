<template>
  <el-container class="index">
    <el-header>
      <Header v-bind:index="1+''" :name=user.name></Header>
    </el-header>
    <el-container>
      <el-aside width="200px">
        <Menu :isRepository=isRepository
              :isRecycleBin=isRecycleBin
              :isSearch=isSearch
              :repository=repository
              :folders=folders
              :files=files
              @changeIsRepository="changeIsRepository"
              @changeIsRecycleBin="changeIsRecycleBin"
              @changeIsSearch="changeIsSearch"
              @changeFolders="changeFolders"
              @changeFiles="changeFiles"></Menu>
      </el-aside>
      <el-main>
        <BaseExplorer :isRepository=isRepository
                      :isShare=isShare
                      :isRecycleBin=isRecycleBin
                      :isSearch=isSearch
                      :repository=repository
                      :root=root
                      :folders=folders
                      :files=files
                      @changeIsRepository="changeIsRepository"
                      @changeIsShare="changeIsShare"
                      @changeIsRecycleBin="changeIsRecycleBin"
                      @changeRepository="changeRepository"
                      @changeRecycleBin="changeRecycleBin"
                      @changeIsSearch="changeIsSearch"
                      @changeFolders="changeFolders"
                      @changeFiles="changeFiles"></BaseExplorer>
      </el-main>
    </el-container>
  </el-container>
</template>

<script>
  import Header from "../components/common/Header";
  import Menu from "../components/index/Menu";
  import BaseExplorer from "../components/explorer/BaseExplorer";
  import {loginByToken} from "../api/user";
  import {message} from "../util/message";
  import {setRepository} from "../util/util";

  export default {
    name: "Index",
    created() {
      //TODO 将Repository和User变为共享数据,防止请求次数过多
      if (this.$route.params.user != null && this.$route.params.repository != null) {
        this.flash(this.$route.params.user, this.$route.params.repository);
      } else {
        const handler = (data) => {
          this.flash(data.user, data.repository);
        };
        const catcher = () => {
        };
        loginByToken(handler);
      }
    },
    components: {
      Header,
      Menu,
      BaseExplorer
    },
    data() {
      return {
        user: {},
        repository: {},
        isRepository: true,
        isShare: false,
        isSearch: false,
        isRecycleBin: false,
        root: {},
        folders: {},
        files: {}
      }
    },
    methods: {
      flash(user, repository) {
        this.user = user;
        this.repository = repository;
        this.root = this.repository.folder;
        this.folders = this.root.folders === null ? {} : this.root.folders;
        this.files = this.root.files === null ? {} : this.root.files;
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
      changeRepository(repository) {
        setRepository(repository);
        this.repository = repository;
        this.flash(this.user, repository);
      },
      changeRecycleBin(repository) {
        this.changeRepository(repository);
        this.changeFiles(this.repository.recycleBin.files == null ? {} : this.repository.recycleBin.files);
        this.changeFolders(this.repository.recycleBin.folders == null ? {} : this.repository.recycleBin.folders)
      },
      changeFolders(folders) {
        this.folders = folders === null ? {} : folders;
      },
      changeFiles(files) {
        this.files = files === null ? {} : files;
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

  .index {
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

  .el-aside {
    position: relative;
  }

  .el-main {
    position: relative;
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

  .el-main {
    position: relative;
    margin: 0;
    padding: 0;
  }

</style>
