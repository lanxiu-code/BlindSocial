<template>
  <a-list
    @page-change="props.onPageChange"
    :pagination-props="props.pagination"
    :bordered="false"
  >
    <a-list-item v-for="comment in props.list" :key="comment.id">
      <a-row>
        <a-typography-text
          v-show="currentUser.id != comment.userId"
          style="cursor: pointer"
          @click="jump(comment.postId)"
        >
          <span
            style="color: #165dff"
            v-read
            :data-text="`${comment.userVO?.userName}评论了你`"
            >{{ comment.userVO?.userName }}</span
          >
          评论了你
        </a-typography-text>
        <a-typography-text
          style="cursor: pointer"
          v-show="currentUser.id == comment.userId"
          @click="jump(comment.postId)"
        >
          <span
            style="color: #165dff"
            v-read
            :data-text="`${comment.userVO?.userName}回复了${comment.reply?.userName}`"
            >{{ comment.userVO?.userName }}</span
          >
          回复了
          <span style="color: #165dff">{{ comment.reply?.userName }}</span>
        </a-typography-text>
      </a-row>
      <a-row style="margin: 10px 0">
        <a-typography-text
          :ellipsis="{ rows: 2 }"
          v-read
          :data-text="`${comment.content}`"
        >
          {{ comment.content }}
        </a-typography-text>
      </a-row>
      <a-row>
        <a-typography-text
          style="color: gray; font-size: 13px"
          v-read
          :data-text="dayjs(comment.createTime).format('YYYY-MM-DD HH:mm:ss')"
        >
          {{ dayjs(comment.createTime).format("YYYY-MM-DD HH:mm:ss") }}
        </a-typography-text>
      </a-row>
    </a-list-item>
  </a-list>
</template>

<script setup lang="ts">
import dayjs from "dayjs";
import { computed, reactive } from "vue";
import { CommentVO, LoginUserVO } from "../../../../servers";
import { useUserStore } from "../../../../store/user";
import { useRouter } from "vue-router";
interface Props {
  list: CommentVO[];
  pagination: any;
  onPageChange: (page: number) => void;
}
const router = useRouter();
const jump = (id: any) => {
  router.push(`/detail/${id}`);
};
const userStore = useUserStore();
const currentUser = computed<LoginUserVO>(() => userStore.currentUser);
const props = defineProps<Props>();
</script>

<style lang="sass" scoped></style>
