<template>
  <div class="loginPage">
    <a-row justify="center" style="margin-bottom: 2.25rem">
      <a-image
        :preview="false"
        width="200"
        src="/src/assets/image/login_title.png"
      />
    </a-row>

    <div class="title">登录即可</div>
    <div class="title">开始浏览</div>
    <a-form
      layout="vertical"
      class="a-form"
      size="large"
      :model="registerData"
      @submit="handleSubmit"
    >
      <a-form-item
        field="userAccount"
        tooltip="Please enter username"
        label="账号"
      >
        <a-input v-model="registerData.userAccount" placeholder="请输入账号" />
      </a-form-item>
      <a-form-item
        field="userPassword"
        tooltip="Please enter password"
        label="密码"
      >
        <a-input-password
          v-model="registerData.userPassword"
          v-model:visibility="visibility"
          placeholder="请输入密码"
          allow-clear
        />
      </a-form-item>
      <a-form-item
        field="userPassword"
        tooltip="Please enter password"
        label="确认密码"
      >
        <a-input-password
          v-model="registerData.checkPassword"
          v-model:visibility="visibility"
          placeholder="确认密码"
          allow-clear
        />
      </a-form-item>
      <a-form-item>
        <a-space direction="vertical" style="width: 100%; text-align: right">
          <a-button style="width: 100%" type="primary" html-type="submit"
            >确认</a-button
          >
          <a-link href="/#/user/login">有账号，去登陆</a-link>
        </a-space>
      </a-form-item>
    </a-form>
  </div>
</template>

<script setup lang="ts">
import { reactive, ref } from "vue";
import { UserControllerService, UserRegisterRequest } from "../../../servers";
import { ResponseCode } from "../../../servers/core/request";
import { Message } from "@arco-design/web-vue";
import router from "../../../router";
const visibility = ref(true);
const registerData: UserRegisterRequest = reactive({});
const handleSubmit = async (data: any) => {
  const res = await UserControllerService.userRegisterUsingPost(data.values);
  if (res.code == ResponseCode.SUCCESS) {
    // 设置用户信息
    Message.success("注册成功");
    router.replace("/user/login");
  }
};
</script>

<style lang="scss" scoped>
.loginPage {
  width: 18.75rem;
  .title {
    font-size: 30px;
    font-weight: bold;
    text-align: center;
    font-family: "微软雅黑";
  }
}
</style>
