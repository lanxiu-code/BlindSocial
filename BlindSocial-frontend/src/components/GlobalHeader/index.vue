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
        <a-input-search size="large" placeholder="搜索文章" />
      </a-col>
      <a-col :span="2">
        <a-space>
          <icon-notification size="20" />
          <a-avatar :size="50">
            <img
              src="https://p1-arco.byteimg.com/tos-cn-i-uwbnlip3yd/a8c8cdb109cb051163646151a4a5083b.png~tplv-uwbnlip3yd-webp.webp"
              alt=""
            />
          </a-avatar>
        </a-space>
      </a-col>
    </a-row>
  </div>
</template>
<script setup lang="ts">
import { useRouter } from "vue-router";
const router = useRouter();
const switchTabs = (key: string) => {
  router.push(key);
};
</script>
<style lang="scss" scoped>
.globalHeader {
  width: 100%;
  background: white;
}
</style>
