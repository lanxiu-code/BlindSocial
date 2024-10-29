import axios from "axios";

// 文件上传
export const uploadFile = (form: FormData) => {
  return new Promise((resolve, reject) => {
    axios
      .post("http://127.0.0.1:8101/dev-api/file/upload", form, {
        withCredentials: true,
        headers: {
          "Content-Type": "multipart/form-data",
        },
      })
      .then((res) => {
        resolve(res.data);
      })
      .catch((err) => {
        reject(err);
      });
  });
};
