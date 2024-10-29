<template>
  <div class="homePage">
    <PostList
      :postsList="postsList"
      :paginationProps="paginationProps"
      :handlePageChange="handlePageChange"
      :updatePostList="updatePostList"
    />
  </div>
</template>

<script setup lang="ts">
import { onMounted, reactive, ref, watch } from "vue";
import { PostControllerService, PostVO } from "../../servers";
import { ResponseCode } from "../../servers/core/request";
//@ts-ignore
import PostList from "@/components/PostList/index.vue";
import PubSub from "pubsub-js";
const searchParams = reactive({
  current: 1,
  pageSize: 4,
  title: "",
});
const total = ref(0);
const postsList = ref<PostVO[]>([]);
const paginationProps = reactive({
  defaultPageSize: 4,
  total,
});
// 监听搜索数据
PubSub.subscribe("searchEvent", (msg, params) => {
  searchParams.title = params.title;
  loadPostData();
});

// 更新列表
const updatePostList = (list: PostVO[]) => {
  postsList.value = list;
};
// 页码变化
const handlePageChange = (page: number) => {
  searchParams.current = page;
};

const loadPostData = async () => {
  const res = await PostControllerService.listPostVoByPageUsingPost(
    searchParams
  );
  if (res.code == ResponseCode.SUCCESS) {
    total.value = res.data.total;
    postsList.value = res.data.records;
  }
};
watch(
  () => searchParams.current,
  () => loadPostData
);
onMounted(() => {
  loadPostData();
});
</script>

<style lang="scss" scoped>
.homePage {
  height: 100%;
}
</style>
