<template>
  <div class="publishPage">
    <MdEditor v-model="content" />
  </div>
</template>

<script setup lang="ts">
//@ts-ignore
import MdEditor from "@/components/MdEditor/index.vue";
import { ref } from "vue";
let content = ref("###  hello");
const onChange = (val: string) => {
  content.value = val;
};
</script>

<style>
.publishPage {
  height: 100%;
}
</style>
