<template>
  <div class="homePage">
    <a-list
      class="list-layout"
      :bordered="false"
      :data="postsList"
      :pagination-props="paginationProps"
    >
      <template #item="{ item }">
        <a-list-item class="list-demo-item" action-layout="vertical">
          <template #actions>
            <span><icon-heart />{{ item.favourNum }}</span>
            <span><icon-star />{{ item.thumbNum }}</span>
            <span><icon-message />Reply</span>
          </template>
          <template #extra>
            <div className="image-area">
              <img alt="arco-design" :src="item.user.userAvatar" />
            </div>
          </template>
          <a-list-item-meta :title="item.title" :description="item.description">
            <template #avatar>
              <a-avatar shape="square">
                <img alt="avatar" :src="item.user.userAvatar" />
              </a-avatar>
            </template>
          </a-list-item-meta>
        </a-list-item>
      </template>
    </a-list>
  </div>
</template>

<script setup lang="ts">
import { onMounted, reactive, ref } from "vue";
import { PostControllerService, PostVO } from "../../servers";
import { ResponseCode } from "../../servers/core/request";
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
const loadPostData = async () => {
  const res = await PostControllerService.listPostVoByPageUsingPost(
    searchParams
  );
  if (res.code == ResponseCode.SUCCESS) {
    total.value = res.data.total;
    postsList.value = res.data.records;
  }
};
onMounted(() => {
  loadPostData();
});
</script>

<style lang="scss" scoped>
.homePage {
  height: 100%;
  .list-layout {
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
</style>
