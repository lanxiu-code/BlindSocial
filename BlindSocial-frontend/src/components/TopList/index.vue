<template>
  <p style="text-align: center; font-size: 20px; font-weight: 500">
    热门文章TOP7
  </p>
  <a-list class="list-layout" :bordered="false" :data="dataSource">
    <template #item="{ item }">
      <a-list-item class="list-demo-item" action-layout="vertical">
        <a-row>
          <a-col :span="7">
            <a-image :src="item.image" height="50" width="50"></a-image>
          </a-col>
          <a-col :span="17">
            <a-typography-text
              :ellipsis="{
                rows: 2,
              }"
            >
              Lorem ipsum dolor sit amet consectetur adipisicing elit. Quisquam
              qui perspiciatis necessitatibus deleniti mollitia laboriosam,
              perferendis rerum facilis voluptate iusto recusandae ex labore
              odio rem adipisci voluptates alias dolorem nesciunt, consequatur
              voluptas quaerat harum eum fugiat magnam. Accusamus aliquid est
              totam nostrum? Saepe, modi dolore ullam id possimus perferendis a!
            </a-typography-text>
          </a-col>
        </a-row>
      </a-list-item>
    </template>
  </a-list>
</template>

<script setup lang="ts">
import { reactive } from "vue";

const dataSource = reactive([
  {
    id: 1,
    image:
      "https://img2.baidu.com/it/u=2440185027,2853238687&fm=253&app=120&size=w931&n=0&f=JPEG&fmt=auto",
    title: "拉升阶段立法解释了的经历",
  },
  {
    id: 1,
    image:
      "https://img2.baidu.com/it/u=2440185027,2853238687&fm=253&app=120&size=w931&n=0&f=JPEG&fmt=auto",
    title: "拉升阶段立法解释了的经历",
  },
  {
    id: 1,
    image:
      "https://img2.baidu.com/it/u=2440185027,2853238687&fm=253&app=120&size=w931&n=0&f=JPEG&fmt=auto",
    title: "拉升阶段立法解释了的经历",
  },
]);
</script>

<style></style>
