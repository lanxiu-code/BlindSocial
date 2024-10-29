import axios from "axios";

import type { ApiRequestOptions } from "./ApiRequestOptions";
import { CancelablePromise } from "./CancelablePromise";
import type { OpenAPIConfig } from "./OpenAPI";
import { Message } from "@arco-design/web-vue";
import { useRoute } from "vue-router";
export enum ResponseCode {
  SUCCESS = 0,
  SYSTEM_ERROR = 50000,
  PARAMS_ERROR = 40000,
  NOT_LOGIN_ERROR = 40100,
  NOT_PERMISSION_ERROR = 40300,
  NOT_FOUND_ERROR = 40400,
  OPERATION_ERROR = 50001,
}
const codeMessage = {
  50000: "系统内部异常",
  40000: "请求参数错误",
  40100: "未登录",
  40300: "无权限",
  40400: "请求数据不存在",
  50001: "操作失败",
};
export interface BaseResponse {
  code: number;
  message: string;
  data: any;
}
const axiosInstance = axios.create({
  // Your custom Axios instance config
  baseURL: "http://127.0.0.1:8101",
  timeout: 3000,
  withCredentials: true,
});
// 添加请求拦截器
axiosInstance.interceptors.request.use(
  function (config) {
    // 在发送请求之前做些什么
    return config;
  },
  function (error) {
    // 对请求错误做些什么
    return Promise.reject(error);
  }
);
// 添加响应拦截器
axiosInstance.interceptors.response.use(
  function (response) {
    // 2xx 范围内的状态码都会触发该函数。
    // 对响应数据做点什么
    const data = response.data;

    if (
      data.code == ResponseCode.NOT_LOGIN_ERROR &&
      !window.location.hash.includes("/login")
    ) {
      Message.error(data.messagge);
      window.location.href = "/#/user/login";
    } else if (data.code != ResponseCode.SUCCESS) {
      Message.error(data.messagge);
    }
    return data;
  },
  function (error) {
    // 超出 2xx 范围的状态码都会触发该函数。
    // 对响应错误做点什么
    return Promise.reject(error);
  }
);

export const request = <T>(
  config: OpenAPIConfig,
  options: ApiRequestOptions
): CancelablePromise<T> => {
  return new CancelablePromise((resolve, reject, onCancel) => {
    // Get the request URL. Depending on your needs, this might need additional processing,
    // @see ./src/templates/core/functions/getUrl.hbs
    const url = `${options.url}`;
    onCancel(() => {});
    // Execute the request. This is a minimal example, in real world scenarios
    // you will need to add headers, process form data, etc.
    // @see ./src/templates/core/axios/request.hbs
    axiosInstance
      .request({
        url,
        data: options.body,
        method: options.method,
        params: options.query,
        headers: { ...options.headers },
      })
      .then((data) => {
        resolve(data as any);
      })
      .catch((error) => {
        reject(error);
      });
  });
};
