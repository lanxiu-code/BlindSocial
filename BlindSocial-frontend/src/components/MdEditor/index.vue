<template>
  <MdEditor
    @on-upload-img="handleUploadImg"
    style="height: 100%"
    @on-change="props.onChange"
    :value="props.value"
    placeholder="请输入内容....."
  />
</template>

<script setup lang="ts">
import { MdEditor } from "md-editor-v3";
import "md-editor-v3/lib/style.css";
interface Props {
  value: string;
  onChange: (v: string) => void;
}
const handleUploadImg = (img) => {
  console.log(img);
};
const props = defineProps<Props>();
</script>

<style lang="scss" scoped>
.MdEditor {
}
</style>
