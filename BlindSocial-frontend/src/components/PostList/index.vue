<template>
  <a-list
    class="list-layout"
    :bordered="false"
    :data="props.postsList"
    :pagination-props="props.paginationProps"
    @page-change="props.handlePageChange"
  >
    <template #item="{ item }">
      <a-list-item class="list-demo-item" action-layout="vertical">
        <template #actions>
          <!-- 点赞 -->
          <span v-if="!item.hasThumb"
            ><icon-heart @click="doThumb(item.id)" />{{ item.thumbNum }}</span
          >
          <span v-else
            ><icon-heart-fill style="color: red" @click="doThumb(item.id)" />{{
              item.thumbNum
            }}</span
          >
          <!-- 收藏 -->
          <span v-if="!item.hasFavour"
            ><icon-star @click="doFavour(item.id)" />{{ item.favourNum }}</span
          >
          <span v-else
            ><icon-star-fill
              style="color: skyblue"
              @click="doFavour(item.id)"
            />{{ item.favourNum }}</span
          >

          <span @click="toDetail(item.id)"><icon-message />评论</span>
          <a-space>
            <a-tag
              v-for="(tag, index) in item.tagList"
              :key="index"
              color="purple"
              >{{ tag }}</a-tag
            >
          </a-space>
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
</template>

<script setup lang="ts">
import { useRouter } from "vue-router";
import {
  PostFavourControllerService,
  PostThumbControllerService,
  PostVO,
} from "../../servers";
import { ref } from "vue";
const router = useRouter();
interface Props {
  postsList: PostVO[];
  paginationProps: any;
  handlePageChange: (page: number) => void;
  updatePostList: (list: PostVO[]) => void;
}
const props = defineProps<Props>();
const newList = ref<PostVO[]>([]);
// 点赞
const doThumb = async (id: number) => {
  await PostThumbControllerService.doThumbUsingPost({ postId: id });
  newList.value = props.postsList.map((item) => {
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
  props.updatePostList(newList.value);
};
// 收藏
const doFavour = async (id: number) => {
  await PostFavourControllerService.doPostFavourUsingPost({ postId: id });
  newList.value = props.postsList.map((item) => {
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
  props.updatePostList(newList.value);
};
// 暴露新数组
defineExpose({
  newList,
});
const toDetail = (id: string) => {
  router.push(`/detail/${id}`);
};
</script>

<style lang="scss" scoped>
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
</style>
