<template>
  <div class="message">
    <a-menu
      mode="horizontal"
      :selected-keys="selectKey"
      @menu-item-click="switchTab"
    >
      <a-menu-item :key="navKey[0]">评论互动</a-menu-item>
      <a-menu-item :key="navKey[1]">点赞</a-menu-item>
      <a-menu-item :key="navKey[2]">收藏</a-menu-item>
    </a-menu>

    <CommentList
      v-show="selectKey == navKey[0]"
      :list="myCommentList"
      :pagination="commentPaginationProps"
      :onPageChange="onCommentPageChange"
    />
    <ThumbList
      v-show="selectKey == navKey[1]"
      :list="myThumbList"
      :pagination="thumbPaginationProps"
      :onPageChange="onThumbPageChange"
    />
    <ThumbList
      v-show="selectKey == navKey[2]"
      :list="myFavourList"
      :pagination="favourPaginationProps"
      :onPageChange="onFavourPageChange"
    />
  </div>
</template>

<script setup lang="ts">
import { onMounted, reactive, ref, watch } from "vue";
import {
  CommentControllerService,
  CommentVO,
  PostFavourControllerService,
  PostFavourVO,
  PostThumbControllerService,
  PostThumbVO,
} from "../../servers";
//@ts-ignore
import CommentList from "./components/CommentList/index.vue";
//@ts-ignore
import ThumbList from "./components/ThumbList/index.vue";

import { ResponseCode } from "../../servers/core/request";
const navKey = ["1", "2", "3"];
const selectKey = ref(navKey[0]);
const myCommentList = ref<CommentVO[]>([]);
const myThumbList = ref<PostThumbVO[]>([]);
const myFavourList = ref<PostFavourVO[]>([]);

// 评论分页信息
const commentPaginationProps = reactive({
  defaultPageSize: 10,
  current: 1,
  pageSize: 10,
  total: 10,
});
// 点赞分页信息
const thumbPaginationProps = reactive({
  defaultPageSize: 10,
  current: 1,
  pageSize: 10,
  total: 10,
});
// 收藏分页信息
const favourPaginationProps = reactive({
  defaultPageSize: 10,
  current: 1,
  pageSize: 10,
  total: 10,
});
const switchTab = async (key: string) => {
  selectKey.value = key;
  switch (key) {
    case navKey[0]:
      await loadMyCommentList();

      break;
    case navKey[1]:
      await loadMyThumbList();

      break;
    case navKey[2]:
      await loadMyFavourList();

      break;
    default:
      break;
  }
};
// 分页获取
const onCommentPageChange = (page: number) => {
  commentPaginationProps.current = page;
};
const onThumbPageChange = (page: number) => {
  thumbPaginationProps.current = page;
};
const onFavourPageChange = (page: number) => {
  favourPaginationProps.current = page;
};
// 获取评论列表
const loadMyCommentList = async () => {
  const res = await CommentControllerService.listMyCommentVoByPageUsingPost(
    commentPaginationProps
  );
  if (res.code == ResponseCode.SUCCESS) {
    myCommentList.value = res.data.records;
    commentPaginationProps.total = res.data.total;
  }
};
// 获取点赞列表
// 获取评论列表
const loadMyThumbList = async () => {
  const res = await PostThumbControllerService.listMyThumbVoByPageUsingPost(
    thumbPaginationProps
  );
  if (res.code == ResponseCode.SUCCESS) {
    myThumbList.value = res.data.records;
    thumbPaginationProps.total = res.data.total;
  }
};
// 获取收藏列表
const loadMyFavourList = async () => {
  const res = await PostFavourControllerService.listMyFavourVoByPageUsingPost(
    favourPaginationProps
  );
  if (res.code == ResponseCode.SUCCESS) {
    myFavourList.value = res.data.records;
    favourPaginationProps.total = res.data.total;
  }
};
watch(() => commentPaginationProps.current, loadMyCommentList);
watch(() => thumbPaginationProps.current, loadMyThumbList);
watch(() => favourPaginationProps.current, loadMyFavourList);

onMounted(async () => {
  await loadMyCommentList();
});
</script>

<style lang="scss" scoped></style>
