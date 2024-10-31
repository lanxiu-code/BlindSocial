<template>
  <a-list
    @page-change="props.onPageChange"
    :pagination-props="props.pagination"
    :bordered="false"
  >
    <a-list-item v-for="thumb in props.list" :key="thumb.id">
      <a-row>
        <a-typography-text>{{ thumb.type }}信息</a-typography-text>
      </a-row>
      <a-row style="margin: 10px 0">
        <a-typography-text>
          <span style="color: #165dff">{{ thumb.userVO?.userName }}</span>
          {{ thumb.type }}了
          <span style="color: #165dff">{{ thumb.postVO?.title }}</span>
        </a-typography-text>
      </a-row>
      <a-row>
        <a-typography-text style="color: gray; font-size: 13px">
          {{ dayjs(thumb.createTime).format("YYYY-MM-DD HH:mm:ss") }}
        </a-typography-text>
      </a-row>
    </a-list-item>
  </a-list>
</template>

<script setup lang="ts">
import dayjs from "dayjs";
import { computed, reactive } from "vue";
import { LoginUserVO, PostThumbVO } from "../../../../servers";
import { useUserStore } from "../../../../store/user";
interface Props {
  list: any[];
  pagination: any;
  onPageChange: (page: number) => void;
}
const userStore = useUserStore();
const currentUser = computed<LoginUserVO>(() => userStore.currentUser);
const props = defineProps<Props>();
</script>

<style lang="sass" scoped></style>
