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
            <!-- 点赞 -->
            <span v-if="!item.hasThumb"
              ><icon-heart @click="doThumb(item.id)" />{{ item.thumbNum }}</span
            >
            <span v-else
              ><icon-heart-fill
                style="color: red"
                @click="doThumb(item.id)"
              />{{ item.thumbNum }}</span
            >
            <!-- 收藏 -->
            <span v-if="!item.hasFavour"
              ><icon-star @click="doFavour(item.id)" />{{
                item.favourNum
              }}</span
            >
            <span v-else
              ><icon-star-fill
                style="color: skyblue"
                @click="doFavour(item.id)"
              />{{ item.favourNum }}</span
            >

            <span><icon-message />Reply</span>
          </template>
          <template #extra>
            <div className="image-area">
              <img v-if="item.image" alt="arco-design" :src="item.image" />
              <span v-else></span>
            </div>
          </template>
          <a-list-item-meta
            style="cursor: pointer"
            @click="toDetail(item.id)"
            :title="item.title"
            :description="item.description"
          >
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
import {
  PostControllerService,
  PostFavourControllerService,
  PostThumbControllerService,
  PostVO,
} from "../../servers";
import { ResponseCode } from "../../servers/core/request";
import PubSub from "pubsub-js";
import router from "../../router";
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
// 点赞
const doThumb = async (id: number) => {
  await PostThumbControllerService.doThumbUsingPost({ postId: id });
  postsList.value = postsList.value.map((item) => {
    if (item.id == id) {
      const num = item.hasThumb ? -1 : 1;
      return {
        ...item,
        hasThumb: !item.hasThumb,
        thumbNum: (item.thumbNum as number) + num,
      };
    }
    return item;
  });
};
// 收藏
const doFavour = async (id: number) => {
  await PostFavourControllerService.doPostFavourUsingPost({ postId: id });
  postsList.value = postsList.value.map((item) => {
    if (item.id == id) {
      const num = item.hasFavour ? -1 : 1;
      return {
        ...item,
        hasFavour: !item.hasFavour,
        favourNum: (item.favourNum as number) + num,
      };
    }
    return item;
  });
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
const toDetail = (id: string) => {
  router.push(`/detail/${id}`);
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
