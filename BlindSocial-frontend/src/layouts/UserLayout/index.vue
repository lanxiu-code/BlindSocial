<template>
  <div class="userLayout">
    <a-layout style="height: 100%">
      <a-layout-header class="a-header"></a-layout-header>
      <a-layout-content class="a-content">
        <router-view />
      </a-layout-content>
      <a-layout-footer class="a-footer"></a-layout-footer>
    </a-layout>
  </div>
</template>

<script setup></script>

<style scoped lang="scss">
.userLayout {
  height: 100vh;
  width: 100%;
  background: black;
  color: white;
  .a-header {
    font-size: 30px;
    font-weight: bold;
    text-align: center;
    font-family: "微软雅黑";
    line-height: 6.25rem;
    height: 6.25rem;
  }
  .a-content {
    display: flex;
    align-items: center;
    justify-content: center;
  }
  .a-footer {
    font-size: 30px;
    font-weight: bold;
    text-align: center;
    font-family: "微软雅黑";
    color: #7a7373;
    line-height: 6.25rem;
    height: 6.25rem;
  }
}
</style>
