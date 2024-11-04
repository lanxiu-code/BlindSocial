<template>
  <p
    style="text-align: center; font-size: 20px; font-weight: 500"
    v-read
    data-text="热门文章TOP7"
  >
    热门文章TOP7
  </p>
  <a-list class="list-layout" :bordered="false" :data="topList">
    <template #item="{ item }">
      <a-list-item class="list-demo-item" action-layout="vertical">
        <a-row>
          <a-col :span="7">
            <a-image
              :src="item.user.userAvatar"
              height="50"
              width="50"
            ></a-image>
          </a-col>
          <a-col :span="17">
            <a-typography-text
              v-read
              :data-text="`热门文章,${item.title}`"
              @click="goToDetail(item.id)"
              style="cursor: pointer"
              :ellipsis="{
                rows: 2,
              }"
            >
              {{ item.title }}
            </a-typography-text>
          </a-col>
        </a-row>
      </a-list-item>
    </template>
  </a-list>
</template>

<script setup lang="ts">
import { onMounted, reactive, ref } from "vue";
import { PostControllerService, PostVO } from "../../servers";
import { ResponseCode } from "../../servers/core/request";
import { useRoute, useRouter } from "vue-router";
const topList = ref<PostVO[]>([]);
const router = useRouter();
const route = useRoute();
const loadData = async () => {
  const res = await PostControllerService.listTopPostUsingGet();
  if (res.code == ResponseCode.SUCCESS) {
    //@ts-ignore
    topList.value = res.data;
  }
};
const goToDetail = (id: any) => {
  router.push(`/detail/${id}`);
};
onMounted(() => {
  loadData();
});
</script>

<style></style>
