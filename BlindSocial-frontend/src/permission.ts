import router from "./router";
import { useUserStore } from "./store/user";

// 前置路由守卫
router.beforeEach(async (to, from, next) => {
  // 设置标题
  document.title = (to.meta.title as string) || "盲人社交网站";
  // 是否登录
  const isLogin = localStorage.getItem("isLogin");
  try {
    if (isLogin == "true") {
      const userStore = useUserStore();
      // 获取用户信息
      await userStore.getCurrentUser();
    }
    next();
  } catch (e) {
    next();
  }
});
