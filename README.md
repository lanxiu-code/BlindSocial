# 项目介绍
基于Vue3+SpringBoot+MySql+Mybatis-plus+Web Speech API实现的盲人社交平台网站，用户可以发布文章，评论文章，点赞文章，收藏文章，浏览文章，并通过Web Speech API 自动读出内容帮助用户导航，浏览内容。管理员可以快速管理和检索文章、话题、用户。



# 架构设计图
![](https://cdn.nlark.com/yuque/0/2024/png/35349136/1730688435252-d58e8a89-21aa-40a4-8a13-64ce34e03224.png)



# 技术选型
后端

+ SpringBoot开发框架
+ MySQL数据库
+ 腾讯云COS存储
+ MyBatis-Plus 及

前端

+ Vue3+Vite脚手架
+ Vue-Router
+ Pinia 状态存储
+ Web Speech API
+ 富文本编辑器
+ Arco Design 组件库
+ Axios 请求库
+ PubSub 发布订阅库
+ OpenAPI 前端代码生成

# 项目三大阶段
## 阶段一
开发前端和后端功能，让用户能够在线发布文章，评论文章，点赞文章，收藏文章，浏览文章。该阶段涉及Vue3+SpringBoot 技术

![](https://cdn.nlark.com/yuque/0/2024/png/35349136/1730688751824-e6e66bb0-d4cf-4853-b93f-ca06548441bb.png)



## 阶段二
为前端加上Web Speech API 的功能，使用户鼠标悬停在导航或文章上能够读出内容。该阶段涉及到Vue自定义指令+speechSynthesis对象

> 自定义 v-read指令，通过为标签加上data-text属性，自动获取出内容并朗读出来
>

![](https://cdn.nlark.com/yuque/0/2024/png/35349136/1730688904143-83d45451-cf68-4e07-b3e5-78280e7cfd50.png)



> 使用示例
>

![](https://cdn.nlark.com/yuque/0/2024/png/35349136/1730689021700-2164aa01-57d7-47a3-a525-41234e66e4e3.png)

![](https://cdn.nlark.com/yuque/0/2024/png/35349136/1730689067921-c48a257a-de0d-4075-a31f-4ad6c491bba2.png)



## 阶段三
进行系统的测试与优化，特别是用户体验和语音识别的精度。



# 项目展示
部分页面

## 登录页
![](https://cdn.nlark.com/yuque/0/2024/png/35349136/1730689181205-e753c261-ad2d-4641-8020-10541fda8298.png)



##  首页
![](https://cdn.nlark.com/yuque/0/2024/png/35349136/1730689203422-9b90971e-e65b-47a7-8dff-e7de2860f714.png)



## 发布页
![](https://cdn.nlark.com/yuque/0/2024/png/35349136/1730689523893-1a055763-edc5-495f-b584-cbd8999f9f22.png)



## 讨论区
![](https://cdn.nlark.com/yuque/0/2024/png/35349136/1730689538297-b1db69be-beb4-4cdc-b91c-f76e1b757191.png)





## 消息
![](https://cdn.nlark.com/yuque/0/2024/png/35349136/1730689553416-9293cc4a-92df-4cee-949f-c09f8dee5869.png)



## 个人主页
![](https://cdn.nlark.com/yuque/0/2024/png/35349136/1730689571193-d51c5804-72d7-4a3f-aba3-a1cd0bf30b8b.png)



## 详情页
![](https://cdn.nlark.com/yuque/0/2024/png/35349136/1730689593528-f16c254f-f363-47ba-8031-ec0e570f01dd.png)



## 管理页
![](https://cdn.nlark.com/yuque/0/2024/png/35349136/1730689608888-0f130c91-e1a6-464c-b226-460b38cad350.png)

