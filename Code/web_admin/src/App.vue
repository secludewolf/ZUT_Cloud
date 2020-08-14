<template>
  <div id="app">
    <a-config-provider :locale="zh_CN">
      <router-view/>
    </a-config-provider>
  </div>
</template>

<script>
  import Index from "./views/Index";
  import zh_CN from 'ant-design-vue/lib/locale-provider/zh_CN';


  export default {
    name: 'App',
    created() {
    },
    components: {
      Index
    },
    data() {
      return {
        collapsed: false,
        zh_CN,
      }
    }
  }
</script>

<style>
  html {
    -moz-user-select: none;
    -webkit-user-select: none;
    -ms-user-select: none;
    -khtml-user-select: none;
    user-select: none;
  }

  #app {
    height: 100vh;
    position: relative;
  }
</style>
