<template>
  <a-result status="404" title="404" sub-title="对不起, 您访问的页面不存在">
    <template #extra>
      <router-link to="/index">
        <a-button type="primary">
          返回主页
        </a-button>
      </router-link>
    </template>
  </a-result>
</template>

<script>
  export default {
    name: "NotFoundTarget"
  }
</script>

<style scoped>

</style>
