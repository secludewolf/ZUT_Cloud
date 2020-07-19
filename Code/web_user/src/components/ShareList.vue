<template>
  <el-container class="shareList">
    <el-header>
      <Header v-bind:index="2+''" :name=user.name></Header>
    </el-header>
    <el-container>
      <el-main>
        <el-table
          :data="tableData"
          style="position: relative;top: 0;left: 0;right: 0;bottom: 0">
          <el-table-column
            fixed
            prop="name"
            label="分享名称">
            <template slot-scope="scope">
              <router-link :to='"/share?id="+scope.row.id' target="_blank">{{ scope.row.name }}</router-link>
            </template>
          </el-table-column>
          <el-table-column
            fixed
            prop="password"
            label="分享密码">
          </el-table-column>
          <el-table-column
            fixed
            prop="createTime"
            label="创建日期">
          </el-table-column>
          <el-table-column
            fixed
            prop="validTime"
            label="截止日期">
          </el-table-column>
          <el-table-column
            fixed="right"
            label="操作">
            <template slot-scope="scope">
              <el-button
                @click.native.prevent="deleteRow(scope.$index, tableData)"
                type="text"
                size="small">
                移除
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-main>
    </el-container>
  </el-container>
</template>

<script>
  import Header from "../components/common/Header";
  import {loginByToken} from "../api/user";
  import {deleteShare, getShareList} from "../api/share";
  import {message} from "../util/message";

  export default {
    name: "ShareList",
    created() {
      //TODO 将Repository和User变为共享数据,防止请求次数过多
      if (this.$route.params.user != null && this.$route.params.repository != null) {
        this.flash(this.$route.params.user, this.$route.params.repository);
      } else {
        const handler = (data) => {
          this.user = data.user;
        };
        const catcher = () => {
        };
        loginByToken(handler);
      }
      const handler = (data) => {
        this.tableData = data.shareList;
        console.log(data);
      };
      const catcher = () => {
      };
      getShareList(handler, catcher());
    },
    components: {
      Header
    }, methods: {
      deleteRow(index, rows) {
        const data = this.tableData[index].id;
        const handler = () => {
          rows.splice(index, 1);
          message("删除成功")
        };
        const catcher = (code, content) => {
          message(content, "warning");
        };
        deleteShare(data, handler, catcher);
      }
    },
    data() {
      return {
        user: {},
        tableData: []
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

  .shareList {
    height: 100vh;
  }

  .el-header {
    position: relative;
    margin-bottom: 3px;
    padding: 0;
  }

  .el-container {
    position: relative;
    margin: 0;
    padding: 0;
  }

  .el-main {
    position: relative;
    margin: 0;
    padding: 0;
    height: 100%;
  }
</style>
