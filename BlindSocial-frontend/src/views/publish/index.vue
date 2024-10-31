<template>
  <div class="publishPage">
    <a-row align="center" v-read data-text="输入标题">
      <a-col :span="2">
        <a-typography-text>标题</a-typography-text>
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
    <a-row
      style="margin: 20px 0"
      align="center"
      v-read
      data-text="输入文章描述"
    >
      <a-col :span="2">
        <a-typography-text>描述</a-typography-text>
      </a-col>
      <a-col :span="15">
        <a-input
          v-model:model-value="postData.description"
          placeholder="请输入文章描述"
          allow-clear
        />
      </a-col>
    </a-row>
    <a-row style="margin: 20px 0" align="center" v-read data-text="输入标签">
      <a-col :span="2">
        <a-typography-text>标签</a-typography-text>
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
    <a-row style="margin: 20px 0" align="center">
      <a-col :span="2" v-read data-text="输入话题">
        <a-typography-text>话题</a-typography-text>
      </a-col>
      <a-col :span="15">
        <a-radio-group type="button" @change="topicChange">
          <a-radio
            v-read
            :data-text="topic.name"
            :value="topic.name"
            v-for="(topic, index) in topicsList"
            :key="index"
            >{{ topic.name }}</a-radio
          >
        </a-radio-group>
      </a-col>
    </a-row>
    <MdEditor
      v-read
      data-text="输入文章内容"
      :value="postData.content"
      :onChange="onChange"
    />
    <a-row align="center" style="margin: 20px" justify="end">
      <a-button
        type="primary"
        size="large"
        @click="doPublish"
        v-read
        data-text="发布文章"
        >发布</a-button
      >
    </a-row>
  </div>
</template>

<script setup lang="ts">
//@ts-ignore
import MdEditor from "@/components/MdEditor/index.vue";
import PubSub from "pubsub-js";
import { onMounted, reactive, ref } from "vue";
import {
  PostAddRequest,
  PostControllerService,
  PostVO,
  TopicsControllerService,
  TopicsVO,
} from "../../servers";
import { ResponseCode } from "../../servers/core/request";
import { Message } from "@arco-design/web-vue";
import router from "../../router";
import { useUserStore } from "../../store/user";
const tags = ref([]);
const userStore = useUserStore();
const postData: PostAddRequest = reactive({});
const topicsList: any = ref<TopicsVO[]>([]);
const onChange = (val: string) => {
  postData.content = val;
};
const topicChange: any = (val: any) => {
  postData.topic = val;
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

// 获取话题
const loadTopicsList = async () => {
  const res = await TopicsControllerService.listTopicsVoByPageUsingPost({
    current: 1,
    pageSize: 20,
  });
  if (res.code == ResponseCode.SUCCESS) {
    topicsList.value = res.data.records;
  }
};
onMounted(async () => {
  await userStore.getCurrentUser();
  await loadTopicsList();
});
</script>

<style>
.publishPage {
  height: 100%;
}
</style>
