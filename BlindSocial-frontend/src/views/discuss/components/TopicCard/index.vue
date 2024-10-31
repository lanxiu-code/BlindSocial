<template>
  <a-card hoverable class="topicCard">
    <template #actions>
      <!-- 点赞 -->
      <a-row v-if="!newPost.hasThumb"
        ><icon-heart size="large" @click="doThumb(newPost.id)" />
        {{ newPost.thumbNum }}
      </a-row>
      <a-row v-else
        ><icon-heart-fill style="color: red" @click="doThumb(newPost.id)" />
        {{ newPost.thumbNum }}
      </a-row>
      <!-- 收藏  -->
      <a-row v-if="!newPost.hasFavour"
        ><icon-star size="large" @click="doFavour(newPost.id)" />
        {{ newPost.favourNum }}</a-row
      >
      <a-row v-else>
        <icon-star-fill
          style="color: skyblue"
          @click="doFavour(newPost.id)"
        />{{ newPost.favourNum }}
      </a-row>
    </template>
    <template #cover>
      <div
        :style="{
          height: '100px',
          overflow: 'hidden',
        }"
      >
        <img
          :style="{ width: '100%', transform: 'translateY(-20px)' }"
          alt="image"
          :src="newPost.image"
        />
      </div>
    </template>
    <a-card-meta :title="newPost.title">
      <template #description>
        <a-typography-text
          @click="toDetail(newPost.id)"
          style="cursor: pointer"
          :ellipsis="{
            rows: 2,
          }"
        >
          {{ newPost.description }}
        </a-typography-text>
      </template>
      <template #avatar>
        <div
          :style="{ display: 'flex', alignItems: 'center', color: '#1D2129' }"
        >
          <a-avatar shape="circle" :size="24" :style="{ marginRight: '8px' }">
            <img alt="avatar" :src="newPost.user?.userAvatar" />
          </a-avatar>
          <a-typography-text>{{ newPost.user?.userName }}</a-typography-text>
        </div>
      </template>
    </a-card-meta>
  </a-card>
</template>

<script setup lang="ts">
import { useRouter } from "vue-router";
import {
  PostFavourControllerService,
  PostThumbControllerService,
  PostVO,
} from "../../../../servers";
import { reactive } from "vue";
const router = useRouter();
interface Props {
  data: PostVO;
}
const props = defineProps<Props>();
const newPost: PostVO = reactive({ ...props.data });
const toDetail = (id: any) => {
  router.push(`/detail/${id}`);
};
// 点赞
const doThumb: any = async (id: number) => {
  await PostThumbControllerService.doThumbUsingPost({ postId: id });
  const num = newPost.hasThumb ? -1 : 1;
  Object.assign(newPost, {
    ...newPost,
    hasThumb: !newPost.hasThumb,
    thumbNum: (newPost.thumbNum as number) + num,
  });
};
// 收藏
const doFavour: any = async (id: number) => {
  await PostFavourControllerService.doPostFavourUsingPost({ postId: id });
  const num = newPost.hasFavour ? -1 : 1;
  Object.assign(newPost, {
    ...newPost,
    hasFavour: !newPost.hasFavour,
    favourNum: (newPost.favourNum as number) + num,
  });
};
</script>

<style lang="scss" scoped>
.topicCard {
  width: 250px;
  display: table-cell;
  .icon-hover {
    display: flex;
    align-items: center;
    justify-content: center;
    width: 24px;
    height: 24px;
    border-radius: 50%;
    transition: all 0.1s;
  }
  .icon-hover:hover {
    background-color: rgb(var(--gray-2));
  }
}
</style>
