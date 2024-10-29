<template>
  <div class="publishPage">
    <a-row align="center">
      <a-col :span="2">
        <a-typography-text>文章标题</a-typography-text>
      </a-col>
      <a-col :span="15">
        <a-input
          :model-value="postData.title"
          size="large"
          placeholder="请输入标题"
          allow-clear
        />
      </a-col>
    </a-row>
    <a-row style="margin: 20px 0">
      <a-col :span="2">
        <a-typography-text>文章描述</a-typography-text>
      </a-col>
      <a-col :span="15">
        <a-input
          :model-value="postData.description"
          placeholder="请输入文章描述"
          allow-clear
        />
      </a-col>
    </a-row>
    <MdEditor :value="content" :onChange="onChange" />
    <a-row align="center" style="margin: 20px" justify="end">
      <a-button type="primary" size="large" @click="doPublish">发布</a-button>
    </a-row>
  </div>
</template>

<script setup lang="ts">
//@ts-ignore
import MdEditor from "@/components/MdEditor/index.vue";
import PubSub from "pubsub-js";
import { reactive, ref } from "vue";
import { PostAddRequest, PostControllerService, PostVO } from "../../servers";
import { ResponseCode } from "../../servers/core/request";
import { Message } from "@arco-design/web-vue";
import router from "../../router";
let content = ref("");
const postData: PostAddRequest = reactive({});
const onChange = (val: string) => {
  content.value = val;
};
const doPublish = async () => {
  const res = await PostControllerService.addPostUsingPost(postData);
  if (res.code == ResponseCode.SUCCESS) {
    console.log(res);
    Message.success("发布成功");
    router.push("/home");
  }
};
// 监听添加图片
PubSub.subscribe("addPostImgEvent", (msg, img: string) => {
  content.value += `![](${img})`;
});
</script>

<style>
.publishPage {
  height: 100%;
}
</style>
