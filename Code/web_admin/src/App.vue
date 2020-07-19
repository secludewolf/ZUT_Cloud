<template>
  <div id="app">
    <router-view/>
  </div>
</template>

<script>
  import Index from "./components/Index";

  export default {
    name: 'App',
    components: {
      Index
    },
    data() {
      return {
        collapsed: false,
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
