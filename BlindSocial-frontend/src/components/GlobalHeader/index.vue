<template>
  <div class="globalHeader">
    <a-row
      justify="space-between"
      style="padding: 10px; box-sizing: border-box"
      align="center"
    >
      <a-col :span="4">
        <a-image
          :preview="false"
          width="150"
          src="/src/assets/image/login_title.png"
        />
      </a-col>
      <a-col :span="8">
        <a-input-search
          size="large"
          @search="onSearch"
          placeholder="搜索文章"
        />
      </a-col>
      <a-col :span="2">
        <a-space>
          <icon-notification size="20" />
          <a-dropdown trigger="hover" @select="handleSelect">
            <a-avatar :size="50" v-if="currentUser?.id">
              <img :src="currentUser.userAvatar" />
            </a-avatar>
            <template #content>
              <a-doption value="logout">退出登录</a-doption>
            </template>
          </a-dropdown>

          <a-typography-text
            style="cursor: pointer"
            v-if="!currentUser.id"
            @click="toLogin"
            >未登录</a-typography-text
          >
        </a-space>
      </a-col>
    </a-row>
  </div>
</template>
<script setup lang="ts">
import { useRouter } from "vue-router";
import { useUserStore } from "../../store/user";
import { computed, reactive, ref, watch } from "vue";
import {
  LoginUserVO,
  PostControllerService,
  UserControllerService,
} from "../../servers";
import PubSub from "pubsub-js";
import { ResponseCode } from "../../servers/core/request";
import { Message } from "@arco-design/web-vue";
const searchParams = reactive({
  title: "",
});
const router = useRouter();
const userStore = useUserStore();
const currentUser = computed<LoginUserVO>(() => userStore.currentUser);
const handleSelect = async (key: string) => {
  switch (key) {
    case "logout":
      const res = await UserControllerService.userLogoutUsingPost();
      if (res.code == ResponseCode.SUCCESS) {
        userStore.currentUser = {};
        localStorage.setItem("isLogin", "false");
        Message.success("退出成功");
      }
      break;
    default:
      break;
  }
};
const toLogin = () => {
  router.replace("/user/login");
};
const onSearch = (value: string) => {
  searchParams.title = value;
  // 向首页发布搜索事件
  PubSub.publish("searchEvent", searchParams);
};
</script>
<style lang="scss" scoped>
.globalHeader {
  width: 100%;
  background: white;
}
</style>
