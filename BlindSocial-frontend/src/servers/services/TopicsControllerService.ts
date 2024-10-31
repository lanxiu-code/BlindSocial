/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { BaseResponse_boolean_ } from '../models/BaseResponse_boolean_';
import type { BaseResponse_long_ } from '../models/BaseResponse_long_';
import type { BaseResponse_Page_Topics_ } from '../models/BaseResponse_Page_Topics_';
import type { BaseResponse_Page_TopicsVO_ } from '../models/BaseResponse_Page_TopicsVO_';
import type { BaseResponse_TopicsVO_ } from '../models/BaseResponse_TopicsVO_';
import type { DeleteRequest } from '../models/DeleteRequest';
import type { TopicsAddRequest } from '../models/TopicsAddRequest';
import type { TopicsEditRequest } from '../models/TopicsEditRequest';
import type { TopicsQueryRequest } from '../models/TopicsQueryRequest';
import type { TopicsUpdateRequest } from '../models/TopicsUpdateRequest';
import type { CancelablePromise } from '../core/CancelablePromise';
import { OpenAPI } from '../core/OpenAPI';
import { request as __request } from '../core/request';
export class TopicsControllerService {
    /**
     * addTopics
     * @param topicsAddRequest topicsAddRequest
     * @returns BaseResponse_long_ OK
     * @returns any Created
     * @throws ApiError
     */
    public static addTopicsUsingPost(
        topicsAddRequest: TopicsAddRequest,
    ): CancelablePromise<BaseResponse_long_ | any> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/dev-api/topics/add',
            body: topicsAddRequest,
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }
    /**
     * deleteTopics
     * @param deleteRequest deleteRequest
     * @returns BaseResponse_boolean_ OK
     * @returns any Created
     * @throws ApiError
     */
    public static deleteTopicsUsingPost(
        deleteRequest: DeleteRequest,
    ): CancelablePromise<BaseResponse_boolean_ | any> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/dev-api/topics/delete',
            body: deleteRequest,
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }
    /**
     * editTopics
     * @param topicsEditRequest topicsEditRequest
     * @returns BaseResponse_boolean_ OK
     * @returns any Created
     * @throws ApiError
     */
    public static editTopicsUsingPost(
        topicsEditRequest: TopicsEditRequest,
    ): CancelablePromise<BaseResponse_boolean_ | any> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/dev-api/topics/edit',
            body: topicsEditRequest,
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }
    /**
     * getTopicsVOById
     * @param id id
     * @returns BaseResponse_TopicsVO_ OK
     * @throws ApiError
     */
    public static getTopicsVoByIdUsingGet(
        id?: number,
    ): CancelablePromise<BaseResponse_TopicsVO_> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/dev-api/topics/get/vo',
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
     * listTopicsByPage
     * @param topicsQueryRequest topicsQueryRequest
     * @returns BaseResponse_Page_Topics_ OK
     * @returns any Created
     * @throws ApiError
     */
    public static listTopicsByPageUsingPost(
        topicsQueryRequest: TopicsQueryRequest,
    ): CancelablePromise<BaseResponse_Page_Topics_ | any> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/dev-api/topics/list/page',
            body: topicsQueryRequest,
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }
    /**
     * listTopicsVOByPage
     * @param topicsQueryRequest topicsQueryRequest
     * @returns BaseResponse_Page_TopicsVO_ OK
     * @returns any Created
     * @throws ApiError
     */
    public static listTopicsVoByPageUsingPost(
        topicsQueryRequest: TopicsQueryRequest,
    ): CancelablePromise<BaseResponse_Page_TopicsVO_ | any> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/dev-api/topics/list/page/vo',
            body: topicsQueryRequest,
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }
    /**
     * listMyTopicsVOByPage
     * @param topicsQueryRequest topicsQueryRequest
     * @returns BaseResponse_Page_TopicsVO_ OK
     * @returns any Created
     * @throws ApiError
     */
    public static listMyTopicsVoByPageUsingPost(
        topicsQueryRequest: TopicsQueryRequest,
    ): CancelablePromise<BaseResponse_Page_TopicsVO_ | any> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/dev-api/topics/my/list/page/vo',
            body: topicsQueryRequest,
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }
    /**
     * updateTopics
     * @param topicsUpdateRequest topicsUpdateRequest
     * @returns BaseResponse_boolean_ OK
     * @returns any Created
     * @throws ApiError
     */
    public static updateTopicsUsingPost(
        topicsUpdateRequest: TopicsUpdateRequest,
    ): CancelablePromise<BaseResponse_boolean_ | any> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/dev-api/topics/update',
            body: topicsUpdateRequest,
            errors: {
                401: `Unauthorized`,
                403: `Forbidden`,
                404: `Not Found`,
            },
        });
    }
}
