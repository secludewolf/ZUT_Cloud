<template>
  <a-layout id="Index" style="height: 100%;background:white;">
    <a-layout-header
      style="position:relative;margin:0 0 6px 0;padding:0;background:white;box-shadow:0 0 6px 0 #B39696;">
      <Header></Header>
    </a-layout-header>
    <a-layout-content style="position:relative;margin:0;padding:0;">
      <router-view/>
    </a-layout-content>
  </a-layout>
</template>

<script>
  import Header from "../components/common/Header";

  export default {
    name: "Index",
    created() {
      if (this.$store.state.user.id === 0 && (localStorage.getItem("token") !== "" || localStorage.getItem("token") !== null)) {
        this.$store.dispatch("loginByToken");
      }
    },
    components: {
      Header,
    }
  };
</script>

<style scoped>
</style>
