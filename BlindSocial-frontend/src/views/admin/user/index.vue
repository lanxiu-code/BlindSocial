<template>
  <div class="adminUser">
    <a-row align="center" justify="space-between">
      <a-col :span="7">
        <a-input-search
          size="large"
          placeholder="搜索用户"
          button-text="搜索"
          search-button
          @search="onSearch"
          v-model="searchParams.userName"
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
      :data="userList"
      :pagination="pagination"
      @page-change="pageChange"
    >
      <template #avatar="{ record }">
        <a-image :src="record.userAvatar" width="50" height="50"></a-image>
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
      :width="500"
      :visible="drawerVisible"
      @ok="onOk"
      @cancel="onCancel"
      :title="currentId ? '编辑用户' : '添加用户'"
    >
      <a-row style="margin-bottom: 10px">
        <a-col :span="3">
          <span>头像</span>
        </a-col>
        <a-col :span="4">
          <a-upload
            list-type="picture-card"
            image-preview
            :custom-request="uploadAvatar"
          >
          </a-upload>
        </a-col>
      </a-row>
      <a-form
        ref="formRef"
        :model="currentUser"
        size="large"
        label-align="left"
        :label-col-props="{ span: 3 }"
      >
        <a-form-item field="userAccount" label="账号" v-show="!currentId">
          <a-input v-model="currentUser.userAccount" placeholder="请输入账号" />
        </a-form-item>
        <a-form-item field="userName" label="昵称">
          <a-input v-model="currentUser.userName" placeholder="请输入昵称" />
        </a-form-item>
        <a-form-item field="userProfile" label="简介">
          <a-input
            v-model="currentUser.userProfile"
            placeholder="请输入用户简介"
          />
        </a-form-item>
        <a-form-item field="userRole" label="权限">
          <a-select
            :model-value="currentUser.userRole"
            style="width: 150px"
            placeholder="选择权限"
          >
            <a-option :value="UserRole.ADMIN">管理员</a-option>
            <a-option :value="UserRole.USER">普通用户</a-option>
            <a-option :value="UserRole.BAN">封号</a-option>
          </a-select>
        </a-form-item>
      </a-form>
    </a-drawer>
  </div>
</template>

<script setup lang="ts">
import { onMounted, reactive, ref, watch } from "vue";
import {
  User,
  UserControllerService,
  UserQueryRequest,
} from "../../../servers";
import { ResponseCode } from "../../../servers/core/request";
import dayjs from "dayjs";
import { Message, PaginationProps } from "@arco-design/web-vue";
//@ts-ignore
import AdminUserForm from "@/components/AdminUserForm/index.vue";
//@ts-ignore
import AdminDrawer from "@/components/AdminDrawer/index.vue";
import UserRole from "../../../enum/UserRole";
import { uploadFile } from "../../../utils/file";
const userList = ref([]);
const loading = ref(false);
const drawerVisible = ref(false);
const formRef = ref(null);
const currentUser: User = reactive({});
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
    title: "头像",
    slotName: "avatar",
  },
  {
    title: "账号",
    align: "center",
    dataIndex: "userAccount",
    width: 150,
  },
  {
    title: "密码",
    align: "center",
    dataIndex: "userPassword",
    width: 150,
  },
  {
    title: "昵称",
    align: "center",
    dataIndex: "userName",
    width: 150,
  },
  {
    title: "简介",
    align: "center",
    dataIndex: "userProfile",
    width: 150,
  },
  {
    title: "角色",
    align: "center",
    dataIndex: "userRole",
    width: 150,
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
const searchParams: UserQueryRequest = reactive({
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
//上传头像
const uploadAvatar: any = async (option: any) => {
  const { onError, onSuccess, fileItem } = option;
  const form = new FormData();
  form.append("file", fileItem.file);
  form.append("biz", "user_avatar");
  const res: any = await uploadFile(form);
  if (res.code == ResponseCode.SUCCESS) {
    currentUser.userAvatar = res.data;
    onSuccess(res);
  } else {
    onError(res);
  }
};
const onOk = async () => {
  drawerVisible.value = false;
  let res = null;
  if (currentId.value) {
    res = await UserControllerService.updateUserUsingPost(currentUser);
  } else {
    res = await UserControllerService.addUserUsingPost(currentUser);
  }
  if (res.code == ResponseCode.SUCCESS) {
    await loadUserList();
    //@ts-ignore
    Message.success({ content: currentId.value ? "修改成功" : "添加成功" });
  }
};
const onCancel = () => {
  Object.assign(currentUser, {});
  drawerVisible.value = false;
};
// 搜索
const onSearch = async (val: string) => {
  await loadUserList();
};
const pageChange = (page: number) => {
  searchParams.current = page;
  pagination.current = page;
};
// 获取用户列表
const loadUserList = async () => {
  loading.value = true;
  const res = await UserControllerService.listUserByPageUsingPost(searchParams);
  if (res.code == ResponseCode.SUCCESS) {
    userList.value = res.data.records;
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
  drawerVisible.value = true;
  currentId.value = id;
  const res = await UserControllerService.getUserByIdUsingGet(id);
  if (res.code == ResponseCode.SUCCESS) {
    //@ts-ignore
    Object.assign(currentUser, res.data);
  }
};
const onDelete = async (id: any) => {
  const res = await UserControllerService.deleteUserUsingPost({ id });
  if (res.code == ResponseCode.SUCCESS) {
    await loadUserList();
    Message.success({ content: "删除成功" });
  }
};
watch(() => pagination.current, loadUserList);
onMounted(async () => {
  await loadUserList();
});
</script>

<style lang="scss" scoped>
.adminUser {
}
</style>
