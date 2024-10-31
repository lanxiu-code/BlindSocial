<template>
  <a-comment align="right" :avatar="currentUser.userAvatar">
    <template #actions>
      <a-button key="0" type="secondary" @click="props.onCancel">
        取消
      </a-button>
      <a-button key="1" type="primary" @click="publish"> 发布 </a-button>
    </template>
    <template #content>
      <a-input
        v-model:model-value="content"
        :placeholder="`回复@${props.data.userVO?.userName}`"
      />
    </template>
  </a-comment>
</template>

<script setup lang="ts">
import { computed, ref } from "vue";
import {
  CommentControllerService,
  CommentVO,
  LoginUserVO,
} from "../../../../servers";
import { useUserStore } from "../../../../store/user";
import { ResponseCode } from "../../../../servers/core/request";
const userStore = useUserStore();
const currentUser = computed<LoginUserVO>(() => userStore.currentUser);
interface Props {
  data: CommentVO;
  postId: string;
  onCancel: () => void;
  onComfirm: (data: CommentVO) => void;
}
const props = defineProps<Props>();
const content = ref("");
const publish = async () => {
  const res = await CommentControllerService.addCommentUsingPost({
    content: content.value,
    postId: props.postId as any,
    parentId: props?.data?.id,
    replyId: props?.data?.userId,
  });
  if (res.code == ResponseCode.SUCCESS) {
    content.value = "";
    props?.onComfirm(res.data);
  }
};
</script>

<style></style>
