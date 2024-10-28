import { createRouter, createWebHashHistory } from "vue-router";
const routes = [
  {
    path: "/",
    redirect: "/home",
    hideInMenu: true,
  },
  {
    path: "/",
    name: "基础布局",
    component: import("@/layouts/BasicLayout/index.vue"),
    children: [
      {
        path: "home",
        name: "首页",
        component: import("@/views/home/index.vue"),
        meta: { title: "首页" },
      },
      {
        path: "publish",
        name: "发布帖子",
        component: import("@/views/publish/index.vue"),
        meta: { title: "发布帖子" },
      },
    ],
  },
  {
    path: "/user",
    name: "用户",
    component: import("@/layouts/UserLayout/index.vue"),
    hideInMenu: true,
    children: [
      {
        path: "login",
        name: "登录",
        component: import("@/views/user/login/index.vue"),
      },
      {
        path: "register",
        name: "注册",
        component: import("@/views/user/register/index.vue"),
      },
    ],
  },
];

const router = createRouter({
  history: createWebHashHistory(),
  routes,
});
export default router;
