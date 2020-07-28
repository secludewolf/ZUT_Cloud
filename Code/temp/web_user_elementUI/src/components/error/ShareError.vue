<template>
  <el-container class="share">
    <el-header>
      <Header></Header>
    </el-header>
    <el-main>
      {{msg}}
    </el-main>
  </el-container>
</template>

<script>
  import Header from "@/components/common/Header";
  import Menu from "@/components/index/Menu";
  import BaseExplorer from "@/components/explorer/BaseExplorer";

  export default {
    mounted() {
    },
    components: {
      Header,
      Menu,
      BaseExplorer
    },
    name: "Share",
    data() {
      return {
        msg: "分享错误"
      }
    },
    methods: {}
  }
</script>

<style scoped>
  .share {
    height: 100vh;
  }

  .el-container {
    position: relative;
    margin: 0;
    padding: 0;
  }

  .el-header {
    position: relative;
    margin: 0;
    padding: 0;
  }

  .el-main {
    position: relative;
    margin: 0;
    padding: 0;
  }

  .text {
    font-size: 14px;
  }

  .item {
    padding: 18px 0;
  }

  .box-card {
    width: 480px;
  }

</style>
