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
              <a-button type="outline" v-read data-text="更换头像"
                >更换头像</a-button
              >
            </a-space>
          </template>
        </a-upload>
      </a-col>
    </a-row>
    <a-descriptions
      :data="infoData"
      v-read
      data-text="用户信息"
      title="用户信息"
      bordered
      size="large"
    />
    <a-menu
      @menu-item-click="switchTab"
      mode="horizontal"
      :selected-keys="selectKey"
    >
      <a-menu-item :key="navKey[0]" v-read data-text="导航文章"
        >文章</a-menu-item
      >
      <a-menu-item :key="navKey[1]" v-read data-text="导航收藏"
        >收藏</a-menu-item
      >
      <a-menu-item :key="navKey[2]" v-read data-text="导航喜欢"
        >喜欢</a-menu-item
      >
      <a-menu-item :key="navKey[3]" v-read data-text="导航修改信息">
        修改信息</a-menu-item
      >
    </a-menu>
    <template v-if="selectKey != navKey[3]">
      <PostList
        :postsList="postsList"
        :paginationProps="paginationProps"
        :handlePageChange="handlePageChange"
        :updatePostList="updatePostList"
      />
    </template>
    <template v-else>
      <a-row align="center">
        <a-col :span="10">
          <a-form layout="vertical" :model="currentUser" @submit="handleSubmit">
            <a-form-item
              field="userName"
              label="昵称"
              v-read
              data-text="输入昵称"
            >
              <a-input
                v-model="currentUser.userName"
                placeholder="请输入昵称"
              />
            </a-form-item>
            <a-form-item
              field="userProfile"
              label="简介"
              v-read
              data-text="输入简介"
            >
              <a-input
                v-model="currentUser.userProfile"
                placeholder="请输入简介"
              />
            </a-form-item>
            <a-form-item v-read data-text="保存">
              <a-button html-type="submit" type="primary">保存</a-button>
            </a-form-item>
          </a-form>
        </a-col>
      </a-row>
    </template>
  </div>
</template>

<script setup lang="ts">
import { computed, nextTick, onMounted, reactive, ref, watch } from "vue";
import { useUserStore } from "../../store/user";
import {
  LoginUserVO,
  PostControllerService,
  PostVO,
  UserControllerService,
} from "../../servers";
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
const selectKey = ref("1");
const navKey = ["1", "2", "3", "4"];
const paginationProps = reactive({
  defaultPageSize: 4,
  total,
});
const searchParams = reactive({
  current: 1,
  pageSize: 4,
  title: "",
});
// 更新用户
const handleSubmit = async (data: any) => {
  const res = await UserControllerService.updateMyUserUsingPost(data.values);
  if (res.code == ResponseCode.SUCCESS) {
    await userStore.getCurrentUser();
    Message.success("修改成功");
  }
};
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
const uploadAvatar: any = async (option: any) => {
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
  selectKey.value = key;
  searchParams.current = 1;
  switch (key) {
    case navKey[0]:
      loadMyPostData();
      break;
    case navKey[1]:
      loadFavourPostData();
      break;
    case navKey[2]:
      loadThumbPostData();
      break;
    default:
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
// 加载我的文章数据
const loadMyPostData = async () => {
  const res = await PostControllerService.listMyPostVoByPageUsingPost(
    searchParams
  );
  if (res.code == ResponseCode.SUCCESS) {
    total.value = res.data.total;
    postsList.value = res.data.records;
  }
};
// 加载收藏列表
const loadFavourPostData = async () => {
  const res = await PostControllerService.listFavourPostVoByPageUsingPost(
    searchParams
  );
  if (res.code == ResponseCode.SUCCESS) {
    total.value = res.data.total;
    postsList.value = res.data.records;
  }
};
// 加载点赞列表
const loadThumbPostData = async () => {
  const res = await PostControllerService.listThumbPostVoByPageUsingPost(
    searchParams
  );
  if (res.code == ResponseCode.SUCCESS) {
    total.value = res.data.total;
    postsList.value = res.data.records;
  }
};
watch(
  () => searchParams.current,
  () => loadMyPostData
);
onMounted(() => {
  loadMyPostData();
});
</script>

<style lang="scss" scoped>
.account {
}
</style>
