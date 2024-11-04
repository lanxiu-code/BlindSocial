<template>
  <div class="globalHeader">
    <a-row
      justify="space-between"
      style="padding: 10px; box-sizing: border-box"
      align="center"
    >
      <a-col :span="currentUser.userRole == UserRole.ADMIN ? 4 : 2">
        <a-image
          :preview="false"
          width="150"
          src="/src/assets/image/login_title.png"
        />
      </a-col>
      <a-col :span="10">
        <a-input-search
          v-read
          data-text="搜索文章"
          size="large"
          @search="onSearch"
          placeholder="搜索文章"
        />
      </a-col>
      <a-col :span="4" v-if="currentUser.userRole == UserRole.ADMIN">
        <a-menu
          mode="horizontal"
          @menu-item-click="subNavSwitch"
          v-read
          data-text="导航管理"
        >
          <a-sub-menu key="admin">
            <template #title>
              <a-image
                :preview="false"
                src="/src/assets/image/admin.png"
                height="25px"
                width="25px"
              ></a-image>
              管理
            </template>
            <a-menu-item :key="adminKeys[0]" v-read data-text="导航用户管理"
              >用户管理</a-menu-item
            >
            <a-menu-item :key="adminKeys[1]" v-read data-text="导航文章管理"
              >文章管理</a-menu-item
            >
            <a-menu-item :key="adminKeys[2]" v-read data-text="导航话题管理"
              >话题管理</a-menu-item
            >
            <a-menu-item :key="adminKeys[3]" v-read data-text="导航返回首页"
              >返回首页</a-menu-item
            >
          </a-sub-menu>
        </a-menu>
      </a-col>
      <a-col :span="2">
        <a-space>
          <icon-notification
            size="20"
            v-read
            style="cursor: pointer"
            data-text="消息"
            @click="jumpMessage"
          />
          <a-dropdown trigger="hover" @select="handleSelect">
            <a-avatar
              :size="50"
              v-if="currentUser?.id"
              v-read
              data-text="头像，悬浮弹出下拉菜单"
            >
              <img :src="currentUser.userAvatar" />
            </a-avatar>
            <template #content>
              <a-doption value="logout" v-read data-text="退出登录"
                >退出登录</a-doption
              >
            </template>
          </a-dropdown>

          <a-typography-text
            v-read
            data-text="链接未登录"
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
import { computed, reactive } from "vue";
import { LoginUserVO, UserControllerService } from "../../servers";
import PubSub from "pubsub-js";
import { ResponseCode } from "../../servers/core/request";
import { Message } from "@arco-design/web-vue";
import UserRole from "../../enum/UserRole";
const searchParams = reactive({
  title: "",
});
const router = useRouter();
const userStore = useUserStore();
const adminKeys = ["/admin/user", "/admin/post", "/admin/topic", "/home"];
const currentUser = computed<LoginUserVO>(() => userStore.currentUser);
const jumpMessage = () => {
  router.push("/message");
};
const subNavSwitch = (key: string) => {
  router.push(key);
};
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
