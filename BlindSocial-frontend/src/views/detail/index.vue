<template>
  <div class="postDetail">
    <a-typography-title bold> {{ postDetail.title }} </a-typography-title>
    <MdViewer :content="postDetail.content" />
  </div>
</template>

<script setup lang="ts">
import { onMounted, reactive, watch } from "vue";
import { useRoute, useRouter } from "vue-router";
import { PostControllerService, PostVO } from "../../servers";
import { ResponseCode } from "../../servers/core/request";
//@ts-ignore
import MdViewer from "@/components/MdViewer/index.vue";
const route = useRoute();
const router = useRouter();
const postDetail: PostVO = reactive({});
// 获取详情
const getPostDetail = async (id: any) => {
  const res = await PostControllerService.getPostVoByIdUsingGet(id);
  if (res.code == ResponseCode.SUCCESS) {
    Object.assign(postDetail, res.data);
  }
};
watch(
  () => route.params.id,
  (newValue, oldValue) => getPostDetail(newValue)
);
onMounted(() => {
  const params = route.params;
  if (!params.id) {
    router.back();
  } else {
    getPostDetail(params.id);
  }
});
</script>

<style lang="scss" scoped>
.postDetail {
}
</style>
