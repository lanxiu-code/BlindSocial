import { defineStore } from "pinia";
import { reactive } from "vue";
export const useUserStore = defineStore("user", () => {
  const currentUser = reactive({});
  return {
    currentUser,
  };
});
