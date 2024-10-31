import {
  createRouter,
  createWebHashHistory,
  createWebHistory,
} from "vue-router";
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
        path: "detail/:id",
        name: "帖子详情",
        component: import("@/views/detail/index.vue"),
        meta: { title: "帖子详情" },
      },
      {
        path: "discuss",
        name: "讨论区",
        component: import("@/views/discuss/index.vue"),
        meta: { title: "讨论区" },
      },
      {
        path: "publish",
        name: "发布帖子",
        component: import("@/views/publish/index.vue"),
        meta: { title: "发布帖子" },
      },
      {
        path: "message",
        name: "消息",
        component: import("@/views/message/index.vue"),
        meta: { title: "消息" },
      },
      {
        path: "account",
        name: "我的",
        component: import("@/views/account/index.vue"),
        meta: { title: "我的" },
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
  history: createWebHistory(),
  routes,
});
export default router;
