<template>
  <MdEditor
    @on-upload-img="handleUploadImg"
    style="height: 100%"
    @on-change="props.onChange"
    :model-value="props.value"
    placeholder="请输入内容....."
  />
</template>

<script setup lang="ts">
import { MdEditor } from "md-editor-v3";
import "md-editor-v3/lib/style.css";
import PubSub from "pubsub-js";
import { uploadFile } from "../../utils/file";
import { ResponseCode } from "../../servers/core/request";
interface Props {
  value: string;
  onChange: (v: string) => void;
}
const handleUploadImg = async (file: any) => {
  const form = new FormData();
  form.append("file", file[0]);
  form.append("biz", "post_img");
  const res: any = await uploadFile(form);
  if (res.code == ResponseCode.SUCCESS) {
    // 发送添加图片事件
    PubSub.publish("addPostImgEvent", res.data);
  }
};
const props = defineProps<Props>();
</script>

<style lang="scss" scoped>
.MdEditor {
}
</style>
