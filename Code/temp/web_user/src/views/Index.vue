<template>
  <a-layout id="Index" style="height: 100%;background:white;">
    <a-layout-header
      style="position:relative;margin:0 0 6px 0;padding:0;background:white;box-shadow:0 0 6px 0 #B39696;">
      <Header></Header>
    </a-layout-header>
    <a-layout-content style="position:relative;margin:0;padding:0;">
      <Explorer></Explorer>
    </a-layout-content>
  </a-layout>
</template>

<script>
  import Header from "../components/common/Header";
  import Explorer from "../components/index/Explorer";

  export default {
    name: "Index",
    beforeCreate() {
      //TODO 加载数据
      localStorage.setItem("repository", JSON.stringify({}));
      localStorage.setItem("user", JSON.stringify({}));
    },
    components: {
      Header,
      Explorer
    }
  };
</script>

<style scoped>
</style>
