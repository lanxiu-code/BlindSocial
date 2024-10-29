import { createApp } from "vue";
import ArcoVue from "@arco-design/web-vue";
// 额外引入图标库
import ArcoVueIcon from "@arco-design/web-vue/es/icon";
import "@arco-design/web-vue/dist/arco.css";
import "./style.css";
import App from "./App.vue";
import { createPinia } from "pinia";
import router from "./router";
import registerDirectives from "./directives";
import "./permission";
const pinia = createPinia();
const app = createApp(App);
registerDirectives(app);
app.use(pinia);
app.use(router);
app.use(ArcoVue);
app.use(ArcoVueIcon);
app.mount("#app");
