<template>
  <div class="basicLayout">
    <GlobalHeader />
    <a-layout style="height: 46.875rem" class="layout">
      <a-layout-sider style="border-radius: 10px">
        <a-menu
          style="text-align: center"
          mode="vertical"
          @menu-item-click="switchTabs"
          :default-selected-keys="['/publish']"
        >
          <a-menu-item class="nav-item" key="/home" v-read>首页</a-menu-item>
          <a-menu-item class="nav-item" key="/publish" v-read
            >发布帖子</a-menu-item
          >
          <a-menu-item class="nav-item" key="/discussion" v-read
            >讨论区</a-menu-item
          >
          <a-menu-item class="nav-item" key="/message" v-read>消息</a-menu-item>
          <a-menu-item class="nav-item" key="/account" v-read>我的</a-menu-item>
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
        <p style="text-align: center; font-size: 20px; font-weight: 500">
          热门文章TOP7
        </p>
        <a-list class="list-layout" :bordered="false" :data="dataSource">
          <template #item="{ item }">
            <a-list-item class="list-demo-item" action-layout="vertical">
              <a-row>
                <a-col :span="7">
                  <a-image :src="item.image" height="50" width="50"></a-image>
                </a-col>
                <a-col :span="17">
                  <a-typography-text
                    :ellipsis="{
                      rows: 2,
                    }"
                  >
                    Lorem ipsum dolor sit amet consectetur adipisicing elit.
                    Quisquam qui perspiciatis necessitatibus deleniti mollitia
                    laboriosam, perferendis rerum facilis voluptate iusto
                    recusandae ex labore odio rem adipisci voluptates alias
                    dolorem nesciunt, consequatur voluptas quaerat harum eum
                    fugiat magnam. Accusamus aliquid est totam nostrum? Saepe,
                    modi dolore ullam id possimus perferendis a!
                  </a-typography-text>
                </a-col>
              </a-row>
            </a-list-item>
          </template>
        </a-list>
      </a-layout-sider>
    </a-layout>
  </div>
</template>
<script setup>
import GlobalHeader from "@/components/GlobalHeader/index.vue";
import { reactive } from "vue";
import { useRouter } from "vue-router";
const router = useRouter();
const dataSource = reactive([
  {
    id: 1,
    image:
      "https://img2.baidu.com/it/u=2440185027,2853238687&fm=253&app=120&size=w931&n=0&f=JPEG&fmt=auto",
    title: "拉升阶段立法解释了的经历",
  },
  {
    id: 1,
    image:
      "https://img2.baidu.com/it/u=2440185027,2853238687&fm=253&app=120&size=w931&n=0&f=JPEG&fmt=auto",
    title: "拉升阶段立法解释了的经历",
  },
  {
    id: 1,
    image:
      "https://img2.baidu.com/it/u=2440185027,2853238687&fm=253&app=120&size=w931&n=0&f=JPEG&fmt=auto",
    title: "拉升阶段立法解释了的经历",
  },
]);
const switchTabs = (path) => {
  router.push(path);
};
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
