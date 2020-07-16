<template>
  <el-container>
    <el-header>
      <router-link to="/index"><img src="../../assets/logo.png" alt="Logo"></router-link>
      <div class="menu">
        <el-menu :default-active="index" mode="horizontal" @select="handleSelect"
                 active-text-color="#09AAFF">
          <el-menu-item index="1"><span>网盘</span></el-menu-item>
          <el-menu-item index="2"><span>分享</span></el-menu-item>
        </el-menu>
      </div>
      <div class="user">
        <ul style="margin:0;padding:0;height: 60px;font-size: 16px;color: #909399;">
          <li style="position:relative;float: right;top: 15px;right:30px;padding: 0">
            <el-badge :value="unReadNumber" :hidden="unReadNumber === 0" class="item">
              <el-button size="small" @click="drawer = true">通知</el-button>
            </el-badge>
          </li>
          <li style="float: right;height: 60px;line-height: 60px">
            <span><router-link to="/user" class="name">用户名：{{name}}</router-link></span>
          </li>
        </ul>
      </div>
    </el-header>
    <el-drawer
      :visible.sync="drawer"
      :with-header="false">
      <el-card class="box-card"
               v-for="(value,index) in informs"
               :key="index">
        <div slot="header" class="clearfix">
          <span>{{value.header}}</span>
          <el-button class="read" type="text" v-if="value.status === 0" @click="read(index)">已读</el-button>
        </div>
        <div class="text item">
          <span>{{value.content}}</span>
        </div>
      </el-card>
    </el-drawer>
  </el-container>
</template>

<script>
  import {changeInformStatus, getInformList} from "../../api/inform";
  import {message} from "../../util/message";

  export default {
    name: "Header",
    created() {
      //TODO 将通知设置为公共变量
      this.loadInforms();
    },
    props: {
      index: {
        type: String,
        default: "0"
      },
      name: {
        type: String,
        default: "测试用户"
      }
    },
    data() {
      return {
        drawer: false,
        informs: [
          {
            id: "inform1",
            header: "测试标题",
            content: "测试内容",
            status: 0
          }
        ],
      }
    },
    computed: {
      unReadNumber() {
        let number = 0;
        for (let i = 0; i < this.informs.length; i++) {
          if (this.informs[i].status === 0) {
            number++;
          }
        }
        return number;
      }
    },
    methods: {
      handleSelect(key, keyPath) {
        if (key === "1") {
          this.$router.push("/index")
        } else {
          this.$router.push("/shareList")
        }
      },
      loadInforms() {
        const handler = (data) => {
          this.informs = data.informList;
        };
        const catcher = (code, content) => {
          message("获取通知失败", "error");
        };
        getInformList(handler, catcher);
      },
      read(index) {
        console.log(this.informs[index].id);
        const data = this.informs[index].id;
        const handler = () => {
          this.informs[index].status = 1;
        };
        const catcher = (code, content) => {
          message("操作失败", "error");
        };
        changeInformStatus(data, handler, catcher);
      },
    }
  }
</script>

<style scoped>
  .el-container {
    position: relative;
    margin: 0;
    padding: 0;
  }

  .el-header {
    margin: 0;
    padding: 0;
    font-size: 16px;
    box-shadow: 0 2px 4px rgba(0, 0, 0, .12), 0 0 6px rgba(0, 0, 0, .04)
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

  .name {
    color: #424e67;
    text-decoration: none;
  }


  .text {
    font-size: 14px;
  }

  .item {
    margin-bottom: 18px;
  }

  .clearfix:before,
  .clearfix:after {
    display: table;
    content: "";
  }

  .clearfix:after {
    clear: both
  }

  .box-card {
    width: 480px;
    margin-top: 10px;
    position: relative;
  }

  .read {
    position: relative;
    left: 260px;
    padding: 3px 0
  }

</style>
