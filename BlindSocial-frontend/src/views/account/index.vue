<template>
  <div class="account">
    <a-row>
      <a-col :span="4">
        <a-avatar :size="100">
          <img alt="avatar" :src="currentUser?.userAvatar" />
        </a-avatar>
      </a-col>
      <a-col :span="3">
        <a-upload
          :show-file-list="false"
          :custom-request="uploadAvatar"
          style="margin-top: 40px"
        >
          <template #upload-button>
            <a-space>
              <a-button type="outline">更换头像</a-button>
            </a-space>
          </template>
        </a-upload>
      </a-col>
    </a-row>
    <a-descriptions :data="infoData" title="用户信息" bordered size="large" />
    <a-menu @select="switchTab" mode="horizontal" :default-selected-keys="[1]">
      <a-menu-item :key="1">文章</a-menu-item>
      <a-menu-item :key="2">收藏</a-menu-item>
      <a-menu-item :key="3">喜欢</a-menu-item>
    </a-menu>
    <PostList
      :postsList="postsList"
      :paginationProps="paginationProps"
      :handlePageChange="handlePageChange"
      :updatePostList="updatePostList"
    />
  </div>
</template>

<script setup lang="ts">
import { computed, nextTick, onMounted, reactive, ref, watch } from "vue";
import { useUserStore } from "../../store/user";
import { LoginUserVO, PostControllerService, PostVO } from "../../servers";
import { uploadFile } from "../../utils/file";
import { ResponseCode } from "../../servers/core/request";
import { Message } from "@arco-design/web-vue";
//@ts-ignore
import PostList from "@/components/PostList/index.vue";
const userStore = useUserStore();
const currentUser = computed<LoginUserVO>(() => userStore.currentUser);
const infoData = ref<any[]>([]);
const postsList = ref<PostVO[]>([]);
const total = ref(0);
const paginationProps = reactive({
  defaultPageSize: 4,
  total,
});
const searchParams = reactive({
  current: 1,
  pageSize: 4,
  title: "",
});
// 更新列表
const updatePostList = (list: PostVO[]) => {
  postsList.value = list;
};
// 页码变化
const handlePageChange = (page: number) => {
  searchParams.current = page;
};
// 获取我的参数
//上传头像
const uploadAvatar = async (option: any) => {
  const { onError, onSuccess, fileItem } = option;
  const form = new FormData();
  form.append("file", fileItem.file);
  form.append("biz", "user_avatar");
  const res: any = await uploadFile(form);
  if (res.code == ResponseCode.SUCCESS) {
    await userStore.updateUserInfo({
      userAvatar: res.data,
    });
    await userStore.getCurrentUser();
    onSuccess(res);
    Message.success("修改成功");
  } else {
    onError(res);
  }
};
// 切换标签
const switchTab = async (key: string) => {
  switch (key) {
    case "1":
      searchParams.title = "";
      break;
  }
};
nextTick(() => {
  infoData.value = [
    {
      label: "昵称",
      value: currentUser.value.userName as string,
    },
    {
      label: "个人简介",
      value: currentUser.value.userProfile as string,
    },
  ];
});
//
const loadPostData = async () => {
  const res = await PostControllerService.listMyPostVoByPageUsingPost(
    searchParams
  );
  if (res.code == ResponseCode.SUCCESS) {
    total.value = res.data.total;
    postsList.value = res.data.records;
  }
};
watch(
  () => searchParams.current,
  () => loadPostData
);
onMounted(() => {
  loadPostData();
});
</script>

<style lang="scss" scoped>
.account {
}
</style>
