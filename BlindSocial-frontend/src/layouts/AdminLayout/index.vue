<template>
  <div class="adminLayout">
    <GlobalHeader />
    <a-layout class="layout">
      <a-layout-content class="content">
        <router-view />
      </a-layout-content>
    </a-layout>
  </div>
</template>

<script setup lang="ts">
import GlobalHeader from "@/components/GlobalHeader/index.vue";
</script>

<style lang="scss" scoped>
.adminLayout {
  background: #f5f5f5;
  height: 100%;
  .layout {
    height: 90vh;
    padding: 30px;
    box-sizing: border-box;
    .content {
      padding: 20px;
      box-sizing: border-box;
      background: white;
      border-radius: 10px;
    }
  }
}
</style>
