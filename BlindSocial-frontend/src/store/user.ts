import { defineStore } from "pinia";
import { reactive } from "vue";
import { UserControllerService } from "../servers";
import { ResponseCode } from "../servers/core/request";
export const useUserStore = defineStore("user", () => {
  const currentUser = reactive({});
  const getCurrentUser = async () => {
    const res = await UserControllerService.getLoginUserUsingGet();
    if (res.code == ResponseCode.SUCCESS) {
      Object.assign(currentUser, res.data);
      localStorage.setItem("isLogin", "true");
    }
  };
  return {
    currentUser,
    getCurrentUser,
  };
});
