<template>
  <div class="adminPost">
    <a-row align="center" justify="space-between">
      <a-col :span="7">
        <a-input-search
          size="large"
          placeholder="搜索用户"
          button-text="搜索"
          search-button
          @search="onSearch"
          v-model="searchParams.title"
        />
      </a-col>
      <a-col :span="2">
        <a-button type="primary" size="large" @click="add">添加</a-button>
      </a-col>
    </a-row>
    <a-divider></a-divider>
    <a-table
      :loading="loading"
      :columns="columns"
      :data="postList"
      :pagination="pagination"
      @page-change="pageChange"
    >
      <template #image="{ record }">
        <a-image :src="record.image" width="50" height="50"></a-image>
      </template>
      <template #tagList="{ record }">
        <span>{{ record.tags }}</span>
      </template>
      <template #user="{ record }">
        <span>{{ record.userId }}</span>
      </template>
      <template #description="{ record }">
        <a-typography-text :ellipsis="{ rows: 2 }">
          {{ record.description }}
        </a-typography-text>
      </template>
      <template #content="{ record }">
        <a-typography-text :ellipsis="{ rows: 2 }">
          {{ record.content }}
        </a-typography-text>
      </template>
      <template #createTime="{ record }">
        {{ dayjs(record.createTime).format("YYYY-MM-DD HH:mm:ss") }}
      </template>
      <template #updateTime="{ record }">
        {{ dayjs(record.updateTime).format("YYYY-MM-DD HH:mm:ss") }}
      </template>
      <template #optional="{ record }">
        <a-space>
          <a-button type="primary" @click="onEdit(record.id)">编辑</a-button>
          <a-button type="primary" status="danger" @click="onDelete(record.id)"
            >删除</a-button
          >
        </a-space>
      </template>
    </a-table>
    <a-drawer
      :width="1000"
      :visible="drawerVisible"
      @ok="onOk"
      @cancel="onCancel"
      :title="currentPost.id ? '编辑文章' : '添加文章'"
    >
      <a-form
        ref="formRef"
        :model="currentPost"
        label-align="left"
        :label-col-props="{ span: 2 }"
      >
        <a-form-item field="title" label="标题">
          <a-input v-model="currentPost.title" placeholder="请输入标题" />
        </a-form-item>
        <a-form-item field="description" label="描述">
          <a-input v-model="currentPost.description" placeholder="请输入描述" />
        </a-form-item>
        <a-form-item field="tagList" label="标签">
          <a-input-tag
            size="large"
            :max-tag-count="3"
            v-model:model-value="currentPost.tagList"
            placeholder="请输入标签"
            allow-clear
          />
        </a-form-item>
        <a-form-item field="topic" label="话题">
          <a-radio-group
            type="button"
            @change="topicChange"
            :model-value="currentPost.topic"
          >
            <a-radio
              v-read
              :data-text="topic.name"
              :value="topic.name"
              v-for="(topic, index) in topicsList"
              :key="index"
              >{{ topic.name }}</a-radio
            >
          </a-radio-group>
        </a-form-item>
      </a-form>
      <MdEditor :value="currentPost.content" :onChange="onChange" />
    </a-drawer>
  </div>
</template>

<script setup lang="ts">
import { onMounted, reactive, ref, watch } from "vue";
import {
  PostAddRequest,
  PostControllerService,
  PostQueryRequest,
  PostVO,
  TopicsControllerService,
  TopicsVO,
} from "../../../servers";
import { ResponseCode } from "../../../servers/core/request";
import dayjs from "dayjs";
import { Message, PaginationProps } from "@arco-design/web-vue";
//@ts-ignore
import AdminPostForm from "@/components/AdminPostForm/index.vue";
//@ts-ignore
import MdEditor from "@/components/MdEditor/index.vue";
const postList = ref([]);
const loading = ref(false);
const drawerVisible = ref(false);
const formRef = ref(null);
const currentPost: PostVO = reactive({});
const currentId = ref("");
const topicsList: any = ref<TopicsVO[]>([]);
const columns: any = [
  {
    title: "ID",
    dataIndex: "id",
    fixed: "left",
    align: "center",
    width: 200,
  },
  {
    title: "封面",
    slotName: "image",
  },
  {
    title: "标题",
    dataIndex: "title",
    width: 150,
  },
  {
    title: "话题",
    align: "center",
    dataIndex: "topic",
    width: 150,
  },
  {
    title: "描述",
    align: "center",
    slotName: "description",
    width: 150,
  },
  {
    title: "内容",
    align: "center",
    slotName: "content",
    width: 150,
  },
  {
    title: "点赞数",
    align: "center",
    dataIndex: "thumbNum",
    width: 150,
  },
  {
    title: "收藏数",
    align: "center",
    dataIndex: "favourNum",
    width: 150,
  },
  {
    title: "标签",
    align: "center",
    slotName: "tagList",
    width: 150,
  },
  {
    title: "创建人",
    align: "center",
    slotName: "user",
    width: 190,
  },
  {
    title: "创建时间",
    slotName: "createTime",
    align: "center",
    width: 200,
  },
  {
    title: "更新时间",
    align: "center",
    slotName: "updateTime",
    width: 200,
  },
  {
    title: "操作",
    slotName: "optional",
    fixed: "right",
    align: "center",
    width: 200,
  },
];
const searchParams: PostQueryRequest = reactive({
  current: 1,
  pageSize: 10,
});
// 分页属性
const pagination: PaginationProps = reactive({
  defaultPageSize: 10,
  current: 1,
  pageSize: 10,
  total: 0,
});
PubSub.subscribe("addPostImgEvent", (msg, img: string) => {
  if (!currentPost.image) {
    currentPost.image = img;
  }
  if (currentPost.content) {
    currentPost.content += `![](${img})`;
  } else {
    currentPost.content = `![](${img})`;
  }
});
const topicChange: any = (val: string) => {
  currentPost.topic = val;
};
const onChange = (val: any) => {
  currentPost.content = val;
};
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
const onOk = async () => {
  drawerVisible.value = false;
  let res = null;
  if (currentPost.id) {
    res = await PostControllerService.updatePostUsingPost(currentPost);
  } else {
    let data = {} as PostAddRequest;
    Object.assign(data, { ...currentPost, tags: currentPost.tagList });
    res = await PostControllerService.addPostUsingPost(data);
  }
  currentPost.content = "";
  if (res.code == ResponseCode.SUCCESS) {
    await loadPostList();
    //@ts-ignore
    Message.success({ content: currentPost.id ? "修改成功" : "添加成功" });
  }
};
const onCancel = () => {
  currentPost.id = null as any;
  currentPost.content = "";
  Object.assign(currentPost, {});
  drawerVisible.value = false;
};
// 搜索
const onSearch = async (val: string) => {
  await loadPostList();
};
const pageChange = (page: number) => {
  searchParams.current = page;
  pagination.current = page;
};
// 获取用户列表
const loadPostList = async () => {
  loading.value = true;
  const res = await PostControllerService.listPostByPageUsingPost(searchParams);
  if (res.code == ResponseCode.SUCCESS) {
    postList.value = res.data.records;
    pagination.total = res.data.total;
  }
  loading.value = false;
};
const add = () => {
  currentId.value = "";
  //@ts-ignore
  formRef.value.resetFields();
  drawerVisible.value = true;
};
const onEdit = async (id: any) => {
  const res = await PostControllerService.getPostVoByIdUsingGet(id);
  if (res.code == ResponseCode.SUCCESS) {
    //@ts-ignore
    Object.assign(currentPost, res.data);
  }
  drawerVisible.value = true;
};
const onDelete = async (id: any) => {
  const res = await PostControllerService.deletePostUsingPost({ id });
  if (res.code == ResponseCode.SUCCESS) {
    await loadPostList();
    Message.success({ content: "删除成功" });
  }
};
watch(() => pagination.current, loadPostList);
onMounted(async () => {
  await loadPostList();
  await loadTopicsList();
});
</script>

<style lang="scss" scoped>
.adminPost {
}
</style>
