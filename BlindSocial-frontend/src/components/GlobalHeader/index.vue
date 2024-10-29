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
          <a-avatar :size="50" v-if="currentUser?.id">
            <img :src="currentUser.userAvatar" />
          </a-avatar>
          <a-typography-text style="cursor: pointer" v-else @click="toLogin"
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
import { LoginUserVO, PostControllerService } from "../../servers";
import PubSub from "pubsub-js";
const searchParams = reactive({
  title: "",
});
const router = useRouter();
const userStore = useUserStore();
const currentUser = computed<LoginUserVO>(() => userStore.currentUser);

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
