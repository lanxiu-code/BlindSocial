import { defineConfig } from "vite";
import vue from "@vitejs/plugin-vue";

// https://vite.dev/config/
export default defineConfig({
  plugins: [vue()],
  server: {
    port: 8080,
    open: false,
    proxy: {
      "/dev-api": {
        target: "http://127.0.0.1:8081",
      },
    },
  },
  resolve: {
    alias: {
      "@": "/src",
    },
  },
});
