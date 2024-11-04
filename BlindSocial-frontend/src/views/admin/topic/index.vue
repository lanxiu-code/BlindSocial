<template>
  <div class="adminTopic">
    <a-row align="center" justify="space-between">
      <a-col :span="7">
        <a-input-search
          size="large"
          placeholder="搜索话题"
          button-text="搜索"
          search-button
          @search="onSearch"
          v-model="searchParams.name"
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
      :data="topicList"
      :pagination="pagination"
      @page-change="pageChange"
    >
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
      :width="500"
      :visible="drawerVisible"
      @ok="onOk"
      @cancel="onCancel"
      :title="currentTopic.id ? '编辑话题' : '添加话题'"
    >
      <a-form
        ref="formRef"
        :model="currentTopic"
        label-align="left"
        :label-col-props="{ span: 3 }"
      >
        <a-form-item field="name" label="话题名">
          <a-input v-model="currentTopic.name" placeholder="请输入话题名" />
        </a-form-item>
      </a-form>
    </a-drawer>
  </div>
</template>

<script setup lang="ts">
import { onMounted, reactive, ref, watch } from "vue";
import {
  TopicsControllerService,
  TopicsQueryRequest,
  TopicsVO,
} from "../../../servers";
import { ResponseCode } from "../../../servers/core/request";
import dayjs from "dayjs";
import { Message, PaginationProps } from "@arco-design/web-vue";
const topicList = ref([]);
const loading = ref(false);
const drawerVisible = ref(false);
const formRef = ref(null);
const currentTopic: TopicsVO = reactive({});
const currentId = ref("");
const columns: any = [
  {
    title: "ID",
    dataIndex: "id",
    fixed: "left",
    align: "center",
    width: 200,
  },
  {
    title: "话题名",
    dataIndex: "name",
    width: 150,
  },
  {
    title: "创建人",
    align: "center",
    dataIndex: "userId",
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
const searchParams: TopicsQueryRequest = reactive({
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

const onOk = async () => {
  drawerVisible.value = false;
  let res = null;
  if (currentTopic.id) {
    res = await TopicsControllerService.updateTopicsUsingPost(currentTopic);
  } else {
    res = await TopicsControllerService.addTopicsUsingPost(currentTopic);
  }
  currentTopic.id = null as any;
  if (res.code == ResponseCode.SUCCESS) {
    await loadTopicList();
    //@ts-ignore
    Message.success({ content: currentTopic.id ? "修改成功" : "添加成功" });
  }
};
const onCancel = () => {
  currentTopic.id = null as any;
  Object.assign(currentTopic, {});
  drawerVisible.value = false;
};
// 搜索
const onSearch = async (val: string) => {
  await loadTopicList();
};
const pageChange = (page: number) => {
  searchParams.current = page;
  pagination.current = page;
};
// 获取话题列表
const loadTopicList = async () => {
  loading.value = true;
  const res = await TopicsControllerService.listTopicsByPageUsingPost(
    searchParams
  );
  if (res.code == ResponseCode.SUCCESS) {
    topicList.value = res.data.records;
    pagination.total = res.data.total;
  }
  loading.value = false;
};
const add = () => {
  //@ts-ignore
  formRef.value.resetFields();
  drawerVisible.value = true;
};
const onEdit = async (id: any) => {
  const res = await TopicsControllerService.getTopicsVoByIdUsingGet(id);
  if (res.code == ResponseCode.SUCCESS) {
    //@ts-ignore
    Object.assign(currentTopic, res.data);
  }
  drawerVisible.value = true;
};
const onDelete = async (id: any) => {
  const res = await TopicsControllerService.deleteTopicsUsingPost({ id });
  if (res.code == ResponseCode.SUCCESS) {
    await loadTopicList();
    Message.success({ content: "删除成功" });
  }
};
watch(() => pagination.current, loadTopicList);
onMounted(async () => {
  await loadTopicList();
});
</script>

<style lang="scss" scoped>
.adminTopic {
}
</style>
