/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { UserVO } from './UserVO';
export type CommentVO = {
    children?: Array<CommentVO>;
    content?: string;
    createTime?: string;
    favourNum?: number;
    id?: number;
    parentId?: number;
    postId?: number;
    reply?: UserVO;
    thumbNum?: number;
    updateTime?: string;
    userId?: number;
    userVO?: UserVO;
};

