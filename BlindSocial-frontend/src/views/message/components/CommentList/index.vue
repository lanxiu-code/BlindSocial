<template>
  <a-list
    @page-change="props.onPageChange"
    :pagination-props="props.pagination"
    :bordered="false"
  >
    <a-list-item v-for="comment in props.list" :key="comment.id">
      <a-row>
        <a-typography-text v-show="currentUser.id != comment.userId">
          <span style="color: #165dff">{{ comment.userVO?.userName }}</span>
          评论了你
        </a-typography-text>
        <a-typography-text v-show="currentUser.id == comment.userId">
          <span style="color: #165dff">{{ comment.userVO?.userName }}</span>
          回复了
          <span style="color: #165dff">{{ comment.reply?.userName }}</span>
        </a-typography-text>
      </a-row>
      <a-row style="margin: 10px 0">
        <a-typography-text :ellipsis="{ rows: 2 }">
          {{ comment.content }}
        </a-typography-text>
      </a-row>
      <a-row>
        <a-typography-text style="color: gray; font-size: 13px">
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
interface Props {
  list: CommentVO[];
  pagination: any;
  onPageChange: (page: number) => void;
}
const userStore = useUserStore();
const currentUser = computed<LoginUserVO>(() => userStore.currentUser);
const props = defineProps<Props>();
</script>

<style lang="sass" scoped></style>
