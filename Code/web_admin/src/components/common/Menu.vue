<template>
  <div style="position:relative;top:0;bottom: 0;width: 100%;">
    <div class="logo">
      <img src="../../assets/logo.png" alt="logo" style="height: 100%;margin: 0 60px;"/>
    </div>
    <a-menu
      :default-selected-keys="['0']"
      :default-open-keys="['sub1','sub2','sub3']"
      mode="inline"
      theme="dark"
      :inline-collapsed="collapsed"
      style="position:absolute;width: 100%"
    >
      <a-menu-item key="0">
        <a-icon type="pie-chart"/><span>控制台</span>
        <router-link to="/controller">
          控制台
        </router-link>
      </a-menu-item>
      <a-sub-menu key="sub1">
        <span slot="title"><a-icon type="user"/><span>账号管理</span></span>
        <a-menu-item key="1">
          <router-link to="/userManager">
            用户
          </router-link>
        </a-menu-item>
        <a-menu-item key="2">
          <router-link to="/adminManager">
            管理员
          </router-link>
        </a-menu-item>
      </a-sub-menu>
      <a-sub-menu key="sub2">
        <span slot="title"><a-icon type="file"/><span>数据管理</span></span>
        <a-menu-item key="3">
          <router-link to="/fileManager">
            文件
          </router-link>
        </a-menu-item>
        <a-menu-item key="4">
          <router-link to="/shareManager">
            分享
          </router-link>
        </a-menu-item>
      </a-sub-menu>
      <a-sub-menu key="sub3">
        <span slot="title"><a-icon type="notification"/><span>通知管理</span></span>
        <a-menu-item key="5">
          <router-link to="/userInform">
            用户
          </router-link>
        </a-menu-item>
        <a-menu-item key="6">
          <router-link to="/adminInform">
            管理员
          </router-link>
        </a-menu-item>
        <a-menu-item key="7">
          <router-link to="/createInform">
            发布
          </router-link>
        </a-menu-item>
      </a-sub-menu>
      <a-menu-item key="8">
        <a-icon type="container"/><span>日志</span>
        <router-link to="/log">
          日志
        </router-link>
      </a-menu-item>
    </a-menu>
  </div>
</template>

<script>
export default {
  name: "Menu",
  props: {
    collapsed: {
      type: Boolean,
      default: true
    }
  },
  data() {
    return {};
  },
  methods: {
    toggleCollapsed() {
      this.collapsed = !this.collapsed;
    },
  },
};
</script>

<style scoped>
.logo {
  height: 32px;
  margin: 16px;
}
</style>
