<template>
  <div class="basicLayout">
    <GlobalHeader />
    <a-layout style="height: 46.875rem" class="layout">
      <a-layout-sider style="border-radius: 10px">
        <a-menu
          style="text-align: center"
          mode="vertical"
          @menu-item-click="switchTabs"
          :selected-keys="selectedKey"
        >
          <a-menu-item class="nav-item" key="/home" data-text="导航首页" v-read>
            <a-image
              :preview="false"
              src="/src/assets/image/home.png"
              height="25px"
              width="25px"
            ></a-image>
            首页</a-menu-item
          >
          <a-menu-item
            class="nav-item"
            key="/publish"
            data-text="导航发布帖子"
            v-read
          >
            <a-image
              :preview="false"
              src="/src/assets/image/publish.png"
              height="25px"
              width="25px"
            ></a-image>
            发布帖子</a-menu-item
          >
          <a-menu-item
            class="nav-item"
            key="/discuss"
            data-text="导航讨论区"
            v-read
          >
            <a-image
              :preview="false"
              src="/src/assets/image/discuss.png"
              height="25px"
              width="25px"
            ></a-image>
            讨论区</a-menu-item
          >
          <a-menu-item
            class="nav-item"
            key="/message"
            data-text="导航消息"
            v-read
          >
            <a-image
              :preview="false"
              src="/src/assets/image/message.png"
              height="25px"
              width="25px"
            ></a-image>
            消息</a-menu-item
          >
          <a-menu-item
            class="nav-item"
            key="/account"
            data-text="导航我的"
            v-read
          >
            <a-image
              :preview="false"
              src="/src/assets/image/me.png"
              height="25px"
              width="25px"
            ></a-image>
            我的</a-menu-item
          >
        </a-menu>
      </a-layout-sider>
      <a-layout-content class="content">
        <router-view />
      </a-layout-content>
      <a-layout-sider
        style="
          border-radius: 10px;
          width: 250px;
          padding: 10px;
          box-sizing: border-box;
        "
      >
        <TopList />
      </a-layout-sider>
    </a-layout>
  </div>
</template>
<script setup>
import GlobalHeader from "@/components/GlobalHeader/index.vue";
import TopList from "@/components/TopList/index.vue";
import { onMounted, reactive, ref, watch } from "vue";
import { useRoute, useRouter } from "vue-router";
const router = useRouter();
const route = useRoute();
const selectedKey = ref("/home");
const switchTabs = (path) => {
  router.push(path);
};
watch(
  () => route.fullPath,
  (val) => {
    selectedKey.value = val;
  }
);
onMounted(() => {
  selectedKey.value = route.fullPath;
});
</script>

<style scoped lang="scss">
.basicLayout {
  background: #f5f5f5;
  height: 100vh;
  .layout {
    padding: 10px 50px;
    box-sizing: border-box;
    .nav-item {
      margin: 40px 0;
    }
    .content {
      padding: 20px;
      box-sizing: border-box;
      background: white;
      margin: 0 30px;
      border-radius: 10px;
      &::-webkit-scrollbar {
        width: 5px;
        height: 8px;
        background-color: pink; /* or add it to the track */
      }
    }
    .list-layout .image-area {
      width: 183px;
      height: 119px;
      border-radius: 2px;
      overflow: hidden;
    }

    .list-layout .list-demo-item {
      padding: 20px 0;
      border-bottom: 1px solid var(--color-fill-3);
    }

    .list-layout .image-area img {
      width: 100%;
    }

    .list-layout .arco-list-item-action .arco-icon {
      margin: 0 4px;
    }
  }
}
</style>
