/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { BaseResponse_boolean_ } from '../models/BaseResponse_boolean_';
import type { BaseResponse_CommentVO_ } from '../models/BaseResponse_CommentVO_';
import type { BaseResponse_long_ } from '../models/BaseResponse_long_';
import type { BaseResponse_Page_Comment_ } from '../models/BaseResponse_Page_Comment_';
import type { BaseResponse_Page_CommentVO_ } from '../models/BaseResponse_Page_CommentVO_';
import type { CommentAddRequest } from '../models/CommentAddRequest';
import type { CommentEditRequest } from '../models/CommentEditRequest';
import type { CommentQueryRequest } from '../models/CommentQueryRequest';
import type { CommentUpdateRequest } from '../models/CommentUpdateRequest';
import type { DeleteRequest } from '../models/DeleteRequest';
import type { CancelablePromise } from '../core/CancelablePromise';
import { OpenAPI } from '../core/OpenAPI';
import { request as __request } from '../core/request';
export class CommentControllerService {
    /**
     * addComment
     * @param commentAddRequest commentAddRequest
     * @returns BaseResponse_long_ OK
     * @returns any Created
     * @throws ApiError
     */
    public static addCommentUsingPost(
        commentAddRequest: CommentAddRequest,
    ): CancelablePromise<BaseResponse_long_ | any> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/dev-api/comment/add',
            body: commentAddRequest,
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }
    /**
     * deleteComment
     * @param deleteRequest deleteRequest
     * @returns BaseResponse_boolean_ OK
     * @returns any Created
     * @throws ApiError
     */
    public static deleteCommentUsingPost(
        deleteRequest: DeleteRequest,
    ): CancelablePromise<BaseResponse_boolean_ | any> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/dev-api/comment/delete',
            body: deleteRequest,
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }
    /**
     * editComment
     * @param commentEditRequest commentEditRequest
     * @returns BaseResponse_boolean_ OK
     * @returns any Created
     * @throws ApiError
     */
    public static editCommentUsingPost(
        commentEditRequest: CommentEditRequest,
    ): CancelablePromise<BaseResponse_boolean_ | any> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/dev-api/comment/edit',
            body: commentEditRequest,
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }
    /**
     * getCommentVOById
     * @param id id
     * @returns BaseResponse_CommentVO_ OK
     * @throws ApiError
     */
    public static getCommentVoByIdUsingGet(
        id?: number,
    ): CancelablePromise<BaseResponse_CommentVO_> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/dev-api/comment/get/vo',
            query: {
                'id': id,
            },
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }
    /**
     * listCommentByPage
     * @param commentQueryRequest commentQueryRequest
     * @returns BaseResponse_Page_Comment_ OK
     * @returns any Created
     * @throws ApiError
     */
    public static listCommentByPageUsingPost(
        commentQueryRequest: CommentQueryRequest,
    ): CancelablePromise<BaseResponse_Page_Comment_ | any> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/dev-api/comment/list/page',
            body: commentQueryRequest,
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }
    /**
     * listCommentVOByPage
     * @param commentQueryRequest commentQueryRequest
     * @returns BaseResponse_Page_CommentVO_ OK
     * @returns any Created
     * @throws ApiError
     */
    public static listCommentVoByPageUsingPost(
        commentQueryRequest: CommentQueryRequest,
    ): CancelablePromise<BaseResponse_Page_CommentVO_ | any> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/dev-api/comment/list/page/vo',
            body: commentQueryRequest,
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }
    /**
     * listMyCommentVOByPage
     * @param commentQueryRequest commentQueryRequest
     * @returns BaseResponse_Page_CommentVO_ OK
     * @returns any Created
     * @throws ApiError
     */
    public static listMyCommentVoByPageUsingPost(
        commentQueryRequest: CommentQueryRequest,
    ): CancelablePromise<BaseResponse_Page_CommentVO_ | any> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/dev-api/comment/my/list/page/vo',
            body: commentQueryRequest,
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }
    /**
     * updateComment
     * @param commentUpdateRequest commentUpdateRequest
     * @returns BaseResponse_boolean_ OK
     * @returns any Created
     * @throws ApiError
     */
    public static updateCommentUsingPost(
        commentUpdateRequest: CommentUpdateRequest,
    ): CancelablePromise<BaseResponse_boolean_ | any> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/dev-api/comment/update',
            body: commentUpdateRequest,
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }
}
