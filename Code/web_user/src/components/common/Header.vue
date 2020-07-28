<template>
  <div class="Header">
    <div style="float:left;height: 64px;width: 200px;">
      <router-link to="/index"><img style="display: flex;height: 100%;margin: auto;" src="../../assets/logo.png"
                                    alt="logo"/>
      </router-link>
    </div>
    <a-menu v-model="current" mode="horizontal" style="float:left;height:64px;line-height:64px;font-size: 16px;">
      <a-menu-item key="cloud" style="padding: 0 50px;">
        <router-link to="/index">
          <a-icon type="cloud"/>
          网盘
        </router-link>
      </a-menu-item>
      <a-menu-item key="share" style="padding: 0 50px;">
        <router-link to="/shareList">
          <a-icon type="share-alt"/>
          分享
        </router-link>
      </a-menu-item>
    </a-menu>
    <div style="float: right;height: 64px;padding:0 30px;background: white;line-height: 64px;">
      <a-dropdown style="padding: 3px;">
        <a class="ant-dropdown-link" @click="e => e.preventDefault()">
          <a-icon type="user" style="margin: 0 5px;"/>
          {{ this.$store.getters.getUserName }}
        </a>
        <a-menu slot="overlay">
          <a-menu-item>
            <router-link to="/user">用户信息</router-link>
          </a-menu-item>
          <a-menu-item>
            <router-link to="/login" @click.native="exit">退出</router-link>
          </a-menu-item>
        </a-menu>
      </a-dropdown>
      <span style="padding: 0 5px;">|</span>
      <a-badge :count="unReadNumber" dot>
        <a-icon type="notification" style="padding: 3px;cursor:pointer;" @click="showDrawer"/>
        <a-drawer
          title="通知"
          placement="right"
          :closable="false"
          :visible="visible"
          @close="onClose"
          width="450px"
        >
          <div v-for="(value,index) in informList"
               :key="index">
            <a-card :title="value.header">
              <a slot="extra"
                 v-if="value.status === 0"
                 @click="changeInformStatus(index)">已读</a>
              <span>{{value.content}}</span>
            </a-card>
            <br/>
          </div>
        </a-drawer>
      </a-badge>
    </div>
  </div>
</template>

<script>
  import {changeInformStatus, getInformList} from "../../api/inform";

  export default {
    name: "Header",
    props: {
      userName: {
        type: String,
        default: "用户昵称"
      },
    },
    data() {
      return {
        current: ["cloud"],
        informList: [],
        visible: false,
      };
    },
    computed: {
      unReadNumber() {
        let number = 0;
        for (let i = 0; i < this.informList.length; i++) {
          if (this.informList[i].status === 0) {
            number++;
          }
        }
        return number;
      }
    },
    components: {},
    created() {
    },
    mounted() {
      this.getInformList();
    },
    methods: {
      showDrawer() {
        this.visible = true;
      },
      onClose() {
        this.visible = false;
      },
      exit() {
        localStorage.setItem("token", "");
        this.$message.success("退出成功");
      },
      getInformList() {
        const parent = this;
        const handler = function (data) {
          parent.informList = data.informList;
        };
        const catcher = function (code, content) {
          parent.$message.warn(content);
        };
        getInformList(handler, catcher);
      },
      changeInformStatus(index) {
        const parent = this;
        const data = this.informList[index].id;
        const handler = function (data) {
          parent.informList[index].status = 1;
        };
        const catcher = function (code, content) {
          parent.$message.warn(content);
        };
        changeInformStatus(data, handler, catcher);
      },
    }
  };
</script>
<style scoped>
</style>
