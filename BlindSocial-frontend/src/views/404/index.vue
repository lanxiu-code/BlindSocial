<template>
  <a-result style="margin-top: 20%" status="404" subtitle="页面没有找到...">
    <template #extra>
      <a-space>
        <a-button type="primary" @click="goHome">返回首页</a-button>
      </a-space>
    </template>
  </a-result>
</template>

<script setup lang="ts">
import { useRouter } from "vue-router";

const router = useRouter();

const goHome = () => {
  router.replace("/");
};
</script>

<style lang="scss" scoped></style>
