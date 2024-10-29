<template>
  <MdPreview style="height: 100%" v-model="props.content" />
</template>

<script setup lang="ts">
import { MdPreview } from "md-editor-v3";
import "md-editor-v3/lib/style.css";
interface Props {
  content: string;
}
const props = defineProps<Props>();
</script>

<style lang="scss" scoped>
.MdEditor {
}
</style>
