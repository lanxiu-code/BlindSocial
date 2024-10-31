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
      :model="loginData"
      @submit="handleSubmit"
    >
      <a-form-item
        field="userAccount"
        tooltip="Please enter username"
        label="账号"
      >
        <a-input v-model="loginData.userAccount" placeholder="请输入账号" />
      </a-form-item>
      <a-form-item
        field="userPassword"
        tooltip="Please enter password"
        label="密码"
      >
        <a-input-password
          v-model="loginData.userPassword"
          v-model:visibility="visibility"
          placeholder="请输入密码"
          allow-clear
        />
      </a-form-item>
      <a-form-item>
        <a-row justify="space-between" align="center" style="width: 100%">
          <a-col :span="11">
            <a-button html-type="submit" style="width: 100%" type="primary"
              >登录</a-button
            >
          </a-col>
          <a-col :span="11">
            <a-button style="width: 100%" type="secondary" href="/user/register"
              >注册</a-button
            >
          </a-col>
        </a-row>
      </a-form-item>
    </a-form>
  </div>
</template>

<script setup>
import { reactive, ref } from "vue";
import { UserControllerService } from "@/servers";
import { ResponseCode } from "../../../servers/core/request";
import { Message } from "@arco-design/web-vue";
import { useUserStore } from "../../../store/user";
import router from "../../../router";
const visibility = ref(true);
const userStore = useUserStore();
const loginData = reactive({
  userAccount: "lanxiu",
  userPassword: "12345678",
});
const handleSubmit = async (data) => {
  let res = await UserControllerService.userLoginUsingPost(data.values);
  console.log(res);
  if (res.code == ResponseCode.SUCCESS) {
    // 设置用户信息
    userStore.currentUser = res.data;
    Message.success("登录成功");
    router.push("/home");
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
