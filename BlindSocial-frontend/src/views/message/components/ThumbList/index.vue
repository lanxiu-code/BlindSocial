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
        <a-typography-text
          @click="jump(thumb.postVO?.id)"
          style="cursor: pointer"
          v-read
          :data-text="`${thumb.userVO?.userName}${thumb.type}${thumb.postVO?.title}文章`"
        >
          <span style="color: #165dff">{{ thumb.userVO?.userName }}</span>
          {{ thumb.type }}了
          <span style="color: #165dff">{{ thumb.postVO?.title }}</span>
        </a-typography-text>
      </a-row>
      <a-row>
        <a-typography-text
          style="color: gray; font-size: 13px"
          v-read
          :data-text="dayjs(thumb.createTime).format('YYYY-MM-DD HH:mm:ss')"
        >
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
import { useRouter } from "vue-router";
interface Props {
  list: any[];
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
