<template>
  <div class="postDetail">
    <a-typography-title bold>
      {{ postDetail.title }}
    </a-typography-title>
    <a-row>
      <a-button
        style="width: 100%; height: 100px"
        @click="flag = !flag"
        v-read
        :data-text="`点击切换盲人模式`"
        >盲人模式</a-button
      >
    </a-row>
    <div style="margin-top: 50px" v-show="flag">
      <p
        v-read
        :data-text="text"
        v-for="(text, index) in fragmentTextList"
        :key="index"
      >
        {{ text }}
      </p>
    </div>
    <MdViewer v-show="!flag" :content="postDetail.content" />
    <a-divider></a-divider>
    <a-typography-title :heading="3" id="评论">评论</a-typography-title>
    <a-comment align="right" :avatar="currentUser.userAvatar">
      <template #actions>
        <a-button key="1" type="primary" @click="publishRootComment">
          发布
        </a-button>
      </template>
      <template #content>
        <a-textarea
          v-model:modelValue="rootComment.content"
          placeholder="快来评论吧~"
          allow-clear
        />
      </template>
    </a-comment>
    <a-comment
      :author="comment.userVO?.userName"
      :avatar="comment.userVO?.userAvatar"
      :content="comment.content"
      :datetime="dayjs(comment.createTime).format('YYYY-MM-DD HH:mm:ss')"
      v-for="comment in commentList"
      :key="comment.id"
    >
      <template #actions>
        <span class="action" @click="replyComment(comment.id, comment)">
          <IconMessage /> 回复 {{ comment.userVO?.userName }}</span
        >
      </template>
      <Reply
        :data="currentReplyData"
        :postId="postDetail.id"
        :onCancel="onCancel"
        :onComfirm="onComfirm"
        v-show="currentReplyId == comment.id"
      />
      <a-collapse :bordered="false">
        <a-collapse-item
          :header="`查看${comment.children?.length}条回复`"
          key="1"
        >
          <template v-if="comment.children?.length">
            <a-comment
              :author="`${child.userVO?.userName}${
                child.reply?.id ? ` 回复 ${child.reply?.userName}` : ''
              }`"
              :avatar="child.userVO?.userAvatar"
              :content="child.content"
              :datetime="dayjs(child.createTime).format('YYYY-MM-DD HH:mm:ss')"
              v-for="child in comment.children"
              :key="child.id"
            >
              <template #actions>
                <span class="action" @click="replyComment(child.id, child)">
                  <IconMessage /> 回复 {{ child.userVO?.userName }}</span
                >
              </template>
              <Reply
                :data="currentReplyData"
                :postId="postDetail.id"
                :onCancel="onCancel"
                :onComfirm="onComfirm"
                v-show="currentReplyId == child.id"
              />
            </a-comment>
          </template>
          <template v-else> 暂无回复 </template>
        </a-collapse-item>
      </a-collapse>
    </a-comment>
  </div>
</template>

<script setup lang="ts">
import { computed, onMounted, reactive, ref, watch } from "vue";
import { useRoute, useRouter } from "vue-router";
import dayjs from "dayjs";
import {
  CommentAddRequest,
  CommentControllerService,
  CommentQueryRequest,
  CommentVO,
  LoginUserVO,
  PostControllerService,
  PostVO,
} from "../../servers";
import { ResponseCode } from "../../servers/core/request";
//@ts-ignore
import MdViewer from "@/components/MdViewer/index.vue";
//@ts-ignore
import Reply from "./components/Reply/index.vue";
import { useUserStore } from "../../store/user";
const userStore = useUserStore();
const currentUser = computed<LoginUserVO>(() => userStore.currentUser);
const route = useRoute();
const router = useRouter();
const fragmentTextList = ref<string[]>([]);
const postDetail: any = reactive({});
const commentList = ref<CommentVO[]>([]);
const currentReplyId = ref(-1);
const flag = ref(false);
const currentReplyData: CommentVO = reactive({});
const rootComment: CommentAddRequest = reactive({});
const pageInfo: CommentQueryRequest = reactive({
  current: 1,
  pageSize: 10,
  postId: 0,
});
const replyComment: any = (id: any, data: CommentVO) => {
  currentReplyId.value = id;
  Object.assign(currentReplyData, data);
};
// 发布根评论
const publishRootComment = async () => {
  rootComment.postId = postDetail.id;
  const res = await CommentControllerService.addCommentUsingPost(rootComment);
  if (res.code == ResponseCode.SUCCESS) {
    await getComment();
    rootComment.content = "";
  }
};
const onComfirm = async (data: CommentVO) => {
  currentReplyId.value = -1;
  await getComment();
};
// 取消评论
const onCancel = () => {
  currentReplyId.value = -1;
};
// 获取详情
const getPostDetail = async (id: any) => {
  const res = await PostControllerService.getPostVoByIdUsingGet(id);
  if (res.code == ResponseCode.SUCCESS) {
    Object.assign(postDetail, res.data);
    if (res.data?.content?.length) {
      const increment = 100;
      for (
        let index = 0;
        index < res.data?.content.length;
        index += increment
      ) {
        fragmentTextList.value.push(
          res.data.content.slice(index, index + increment)
        );
      }
    }
  }
};
// 获取评论
const getComment = async () => {
  const res = await CommentControllerService.listCommentVoByPageUsingPost(
    pageInfo
  );
  if (res.code == ResponseCode.SUCCESS) {
    commentList.value = res.data.records;
  }
};
watch(
  () => route.params.id,
  async (newValue, oldValue) => {
    await getPostDetail(newValue);
    pageInfo.postId = newValue as any;
    await getComment();
  }
);
onMounted(async () => {
  const params = route.params;
  if (!params.id) {
    router.back();
  } else {
    await getPostDetail(params.id);
    pageInfo.postId = params.id as any;
    await getComment();
  }
});
</script>

<style lang="scss" scoped>
.postDetail {
  .action {
    display: inline-block;
    padding: 0 4px;
    color: var(--color-text-1);
    line-height: 24px;
    background: transparent;
    border-radius: 2px;
    cursor: pointer;
    transition: all 0.1s ease;
  }
  .action:hover {
    background: var(--color-fill-3);
  }
}
</style>
