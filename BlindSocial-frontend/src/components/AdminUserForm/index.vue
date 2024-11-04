<template>
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
    :model="props.data"
    size="large"
    label-align="left"
    :label-col-props="{ span: 3 }"
  >
    <a-form-item field="userAccount" label="账号" v-if="!props.id">
      <a-input v-model="props.data.userAccount" placeholder="请输入账号" />
    </a-form-item>
    <a-form-item field="userName" label="昵称">
      <a-input v-model="props.data.userName" placeholder="请输入昵称" />
    </a-form-item>
    <a-form-item field="userProfile" label="简介">
      <a-input v-model="props.data.userProfile" placeholder="请输入用户简介" />
    </a-form-item>
    <a-form-item field="userRole" label="权限">
      <a-select
        :model-value="props.data.userRole"
        style="width: 150px"
        placeholder="选择权限"
      >
        <a-option :value="UserRole.ADMIN">管理员</a-option>
        <a-option :value="UserRole.USER">普通用户</a-option>
        <a-option :value="UserRole.BAN">封号</a-option>
      </a-select>
    </a-form-item>
  </a-form>
</template>

<script setup lang="ts">
import { reactive, ref } from "vue";
import { User } from "../../servers";
import UserRole from "../../enum/UserRole";
import { uploadFile } from "../../utils/file";
import { ResponseCode } from "../../servers/core/request";
const formRef = ref(null);
interface Props {
  data: User;
  id?: any;
}
const props = defineProps<Props>();
//@ts-ignore
const formData: User = reactive({});
// 清空数据
PubSub.subscribe("getFormData", () => {
  return formData;
});
const getFormData = () => {
  Object.assign(formData, props.data);
  return formData;
};
defineExpose({ getFormData });
//上传头像
const uploadAvatar: any = async (option: any) => {
  const { onError, onSuccess, fileItem } = option;
  const form = new FormData();
  form.append("file", fileItem.file);
  form.append("biz", "user_avatar");
  const res: any = await uploadFile(form);
  if (res.code == ResponseCode.SUCCESS) {
    formData.userAvatar = res.data;
    onSuccess(res);
  } else {
    onError(res);
  }
};
</script>
