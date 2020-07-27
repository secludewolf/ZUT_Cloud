<template>
  <div id="app">
    <router-view></router-view>
  </div>
</template>

<script>

  export default {
    name: 'App',
    mounted() {
      if (this.$store.state.user.id === 0 && localStorage.getItem("token") !== "") {
        this.$store.dispatch("loginByToken", localStorage.getItem("token"));
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
