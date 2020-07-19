<template>
  <a-layout id="Index" style="height: 100%;background:white;">
    <a-layout-header style="position:relative;margin:0 0 6px 0;padding:0;background:white;box-shadow:0 0 6px 0 #B39696;">
      <Header></Header>
    </a-layout-header>
    <a-layout-content style="position:relative;margin:0;padding:0;">
      <BaseExplorer></BaseExplorer>
    </a-layout-content>
  </a-layout>
</template>

<script>
  import Header from "./common/Header";
  import BaseExplorer from "./common/BaseExplorer";

  export default {
    name: "Index",
    beforeCreate() {
      localStorage.setItem("repository", JSON.stringify({}));
      localStorage.setItem("user", JSON.stringify({}));
    },
    components: {
      Header,
      BaseExplorer,
    }
  };
</script>

<style scoped>
</style>
