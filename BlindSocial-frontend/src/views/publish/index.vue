<template>
  <div class="publishPage">
    <a-row align="center">
      <a-col :span="2">
        <a-typography-text>文章标题</a-typography-text>
      </a-col>
      <a-col :span="15">
        <a-input
          v-model:model-value="postData.title"
          size="large"
          placeholder="请输入标题"
          allow-clear
        />
      </a-col>
    </a-row>
    <a-row style="margin: 20px 0" align="center">
      <a-col :span="2">
        <a-typography-text>文章描述</a-typography-text>
      </a-col>
      <a-col :span="15">
        <a-input
          v-model:model-value="postData.description"
          placeholder="请输入文章描述"
          allow-clear
        />
      </a-col>
    </a-row>
    <a-row style="margin: 20px 0" align="center">
      <a-col :span="2">
        <a-typography-text>文章标签</a-typography-text>
      </a-col>
      <a-col :span="15">
        <a-input-tag
          size="large"
          :max-tag-count="3"
          v-model:model-value="tags"
          placeholder="请输入标签"
          allow-clear
        />
      </a-col>
    </a-row>
    <MdEditor :value="postData.content" :onChange="onChange" />
    <a-row align="center" style="margin: 20px" justify="end">
      <a-button type="primary" size="large" @click="doPublish">发布</a-button>
    </a-row>
  </div>
</template>

<script setup lang="ts">
//@ts-ignore
import MdEditor from "@/components/MdEditor/index.vue";
import PubSub from "pubsub-js";
import { onMounted, reactive, ref } from "vue";
import { PostAddRequest, PostControllerService, PostVO } from "../../servers";
import { ResponseCode } from "../../servers/core/request";
import { Message } from "@arco-design/web-vue";
import router from "../../router";
import { useUserStore } from "../../store/user";
const tags = ref([]);
const userStore = useUserStore();
const postData: PostAddRequest = reactive({});
const onChange = (val: string) => {
  postData.content = val;
};
const doPublish = async () => {
  postData.tags = tags.value;
  const res = await PostControllerService.addPostUsingPost(postData);
  if (res.code == ResponseCode.SUCCESS) {
    Message.success("发布成功");
    router.push("/home");
  }
};
// 监听添加图片
PubSub.subscribe("addPostImgEvent", (msg, img: string) => {
  if (!postData.image) {
    postData.image = img;
  }
  postData.content += `![](${img})`;
});
onMounted(async () => {
  await userStore.getCurrentUser();
});
</script>

<style>
.publishPage {
  height: 100%;
}
</style>
