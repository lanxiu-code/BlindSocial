<template>
  <div class="publishPage">
    <MdViewer :value="content" />
  </div>
</template>

<script setup lang="ts">
//@ts-ignore
import MdEditor from "@/components/MdEditor/index.vue";
//@ts-ignore
import MdViewer from "@/components/MdViewer/index.vue";

import { ref } from "vue";
const content = ref("###  hello");
const onChange = (val: string) => {
  content.value = val;
};
</script>

<style>
.publishPage {
  height: 100%;
}
</style>
