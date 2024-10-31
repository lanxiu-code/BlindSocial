<template>
  <div class="discuss">
    <a-row>
      <a-typography-title :heading="3">浏览话题</a-typography-title>
    </a-row>
    <a-scrollbar style="height: 80px; overflow: auto">
      <a-space size="large">
        <a-tag
          style="width: 100px; height: 50px; text-align: center"
          v-for="(topic, index) in topicsList"
          :key="index"
          :color="colors[index % colors.length]"
          size="large"
        >
          <a-link :href="`#${topic.name}`">{{ topic.name }} </a-link>
        </a-tag>
      </a-space>
    </a-scrollbar>
    <div v-for="(topic, index) in topicsList" :key="index" :id="topic.name">
      <a-row>
        <a-typography-title :heading="5">{{ topic.name }}</a-typography-title>
      </a-row>
      <a-scrollbar style="height: 250px; overflow: auto">
        <a-space size="large">
          <template v-if="topicPostMap.get(topic.name)?.length">
            <TopicCard
              :key="item.id"
              v-for="item in topicPostMap.get(topic.name)?.slice(0, 4)"
              :data="item"
            />
          </template>
          <template v-else>
            <a-empty />
          </template>
        </a-space>
      </a-scrollbar>
      <a-collapse :default-active-key="[1]" :bordered="false" accordion>
        <a-collapse-item header="查看更多" key="1">
          <div class="more">
            <template v-if="topicPostMap.get(topic.name)?.length">
              <TopicCard
                :key="item.id"
                v-for="item in topicPostMap.get(topic.name)"
                :data="item"
              />
            </template>
            <template v-else>
              <a-empty style="width: 100%; height: 100%" />
            </template>
          </div>
        </a-collapse-item>
      </a-collapse>
      <a-divider></a-divider>
    </div>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref } from "vue";
import {
  PostControllerService,
  PostVO,
  TopicsControllerService,
  TopicsVO,
} from "../../servers";
import { ResponseCode } from "../../servers/core/request";
//@ts-ignore
import TopicCard from "./components/TopicCard/index.vue";
import { reactive } from "vue";
const topicsList: any = ref<TopicsVO[]>([]);
const colors = ["orangered", "orangered", "arcoblue", "purple"];
// 话题列表
const topicPostMap: Map<string, PostVO[]> = reactive(new Map());
// 获取话题
const loadTopicsList = async () => {
  const res = await TopicsControllerService.listTopicsVoByPageUsingPost({
    current: 1,
    pageSize: 20,
  });
  if (res.code == ResponseCode.SUCCESS) {
    topicsList.value = res.data.records;
    await loadPostListByTopic();
  }
};
// 根据话题获取帖子
const loadPostListByTopic = async () => {
  topicsList.value.forEach(async (topic: TopicsVO) => {
    const res = await PostControllerService.listPostVoByPageUsingPost({
      current: 1,
      pageSize: 20,
      topic: topic.name,
    });
    if (res.code == ResponseCode.SUCCESS) {
      topicPostMap.set(topic.name as string, res.data.records);
    }
  });
};
onMounted(() => {
  loadTopicsList();
});
</script>
<style>
.arco-collapse-item-content {
  padding: 10px;
  box-sizing: border-box;
}
</style>
<style lang="scss" scoped>
.discuss {
  .arco-tag.arco-tag-checked {
    justify-content: center;
  }
  .scrollBar {
    // height: 250px;
    overflow: auto;
  }
  .arco-link {
    color: unset;
  }
  .more {
    display: flex;
    flex-wrap: wrap;
    & > * {
      margin: 10px 20px 10px 10px;
    }
  }
}
</style>
