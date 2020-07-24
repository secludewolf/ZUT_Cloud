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
          {{userName}}
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
      <a-badge :count="1" dot>
        <a-icon type="notification" style="padding: 3px;cursor:pointer;" @click="showDrawer"/>
        <a-drawer
          title="通知"
          placement="right"
          :closable="false"
          :visible="visible"
          @close="onClose"
          width="450px"
        >
          <div>
            <a-card title="通知1">
              <a slot="extra">已读</a>
              <span>内容内容内容内容内容内容内容内容内容内容内容</span>
            </a-card>
            <br/>
          </div>
          <div>
            <a-card title="通知2">
              <a slot="extra">已读</a>
              <span>内容内容内容内容内容内容内容内容内容内容内容</span>
            </a-card>
            <br/>
          </div>
        </a-drawer>
      </a-badge>
    </div>
  </div>
</template>

<script>
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
        visible: false,
      };
    },
    components: {},
    created() {
    },
    mounted() {
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
    }
  };
</script>
<style scoped>
</style>
