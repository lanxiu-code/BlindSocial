package com.zsj.blindsocial.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zsj.blindsocial.annotation.AuthCheck;
import com.zsj.blindsocial.common.BaseResponse;
import com.zsj.blindsocial.common.DeleteRequest;
import com.zsj.blindsocial.common.ErrorCode;
import com.zsj.blindsocial.common.ResultUtils;
import com.zsj.blindsocial.constant.UserConstant;
import com.zsj.blindsocial.exception.BusinessException;
import com.zsj.blindsocial.exception.ThrowUtils;
import com.zsj.blindsocial.model.dto.topics.TopicsAddRequest;
import com.zsj.blindsocial.model.dto.topics.TopicsEditRequest;
import com.zsj.blindsocial.model.dto.topics.TopicsQueryRequest;
import com.zsj.blindsocial.model.dto.topics.TopicsUpdateRequest;
import com.zsj.blindsocial.model.entity.Topics;
import com.zsj.blindsocial.model.entity.User;
import com.zsj.blindsocial.model.vo.TopicsVO;
import com.zsj.blindsocial.service.TopicsService;
import com.zsj.blindsocial.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 话题接口
 *
 * @author zsj

 */
@RestController
@RequestMapping("/topics")
@Slf4j
public class TopicsController {

    @Resource
    private TopicsService topicsService;

    @Resource
    private UserService userService;

    // region 增删改查

    /**
     * 创建话题
     *
     * @param topicsAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    public BaseResponse<Long> addTopics(@RequestBody TopicsAddRequest topicsAddRequest, HttpServletRequest request) {
        ThrowUtils.throwIf(topicsAddRequest == null, ErrorCode.PARAMS_ERROR);
        Topics topics = new Topics();
        BeanUtils.copyProperties(topicsAddRequest, topics);
        // 数据校验
        topicsService.validTopics(topics, true);
        User loginUser = userService.getLoginUser(request);
        topics.setUserId(loginUser.getId());
        // 写入数据库
        boolean result = topicsService.save(topics);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        // 返回新写入的数据 id
        long newTopicsId = topics.getId();
        return ResultUtils.success(newTopicsId);
    }

    /**
     * 删除话题
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteTopics(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getLoginUser(request);
        long id = deleteRequest.getId();
        // 判断是否存在
        Topics oldTopics = topicsService.getById(id);
        ThrowUtils.throwIf(oldTopics == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可删除
        if (!oldTopics.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        // 操作数据库
        boolean result = topicsService.removeById(id);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 更新话题（仅管理员可用）
     *
     * @param topicsUpdateRequest
     * @return
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateTopics(@RequestBody TopicsUpdateRequest topicsUpdateRequest) {
        if (topicsUpdateRequest == null || topicsUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // todo 在此处将实体类和 DTO 进行转换
        Topics topics = new Topics();
        BeanUtils.copyProperties(topicsUpdateRequest, topics);
        // 数据校验
        topicsService.validTopics(topics, false);
        // 判断是否存在
        long id = topicsUpdateRequest.getId();
        Topics oldTopics = topicsService.getById(id);
        ThrowUtils.throwIf(oldTopics == null, ErrorCode.NOT_FOUND_ERROR);
        // 操作数据库
        boolean result = topicsService.updateById(topics);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 根据 id 获取话题（封装类）
     *
     * @param id
     * @return
     */
    @GetMapping("/get/vo")
    public BaseResponse<TopicsVO> getTopicsVOById(long id, HttpServletRequest request) {
        ThrowUtils.throwIf(id <= 0, ErrorCode.PARAMS_ERROR);
        // 查询数据库
        Topics topics = topicsService.getById(id);
        ThrowUtils.throwIf(topics == null, ErrorCode.NOT_FOUND_ERROR);
        // 获取封装类
        return ResultUtils.success(topicsService.getTopicsVO(topics, request));
    }

    /**
     * 分页获取话题列表（仅管理员可用）
     *
     * @param topicsQueryRequest
     * @return
     */
    @PostMapping("/list/page")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<Topics>> listTopicsByPage(@RequestBody TopicsQueryRequest topicsQueryRequest) {
        long current = topicsQueryRequest.getCurrent();
        long size = topicsQueryRequest.getPageSize();
        // 查询数据库
        Page<Topics> topicsPage = topicsService.page(new Page<>(current, size),
                topicsService.getQueryWrapper(topicsQueryRequest));
        return ResultUtils.success(topicsPage);
    }

    /**
     * 分页获取话题列表（封装类）
     *
     * @param topicsQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/list/page/vo")
    public BaseResponse<Page<TopicsVO>> listTopicsVOByPage(@RequestBody TopicsQueryRequest topicsQueryRequest,
                                                               HttpServletRequest request) {
        long current = topicsQueryRequest.getCurrent();
        long size = topicsQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        // 查询数据库
        Page<Topics> topicsPage = topicsService.page(new Page<>(current, size),
                topicsService.getQueryWrapper(topicsQueryRequest));
        // 获取封装类
        return ResultUtils.success(topicsService.getTopicsVOPage(topicsPage, request));
    }

    /**
     * 分页获取当前登录用户创建的话题列表
     *
     * @param topicsQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/my/list/page/vo")
    public BaseResponse<Page<TopicsVO>> listMyTopicsVOByPage(@RequestBody TopicsQueryRequest topicsQueryRequest,
                                                                 HttpServletRequest request) {
        ThrowUtils.throwIf(topicsQueryRequest == null, ErrorCode.PARAMS_ERROR);
        // 补充查询条件，只查询当前登录用户的数据
        User loginUser = userService.getLoginUser(request);
        long current = topicsQueryRequest.getCurrent();
        long size = topicsQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        QueryWrapper<Topics> wrapper = topicsService.getQueryWrapper(topicsQueryRequest).eq("userId", loginUser.getId());

        // 查询数据库
        Page<Topics> topicsPage = topicsService.page(new Page<>(current, size),wrapper);
        // 获取封装类
        return ResultUtils.success(topicsService.getTopicsVOPage(topicsPage, request));
    }

    /**
     * 编辑话题（给用户使用）
     *
     * @param topicsEditRequest
     * @param request
     * @return
     */
    @PostMapping("/edit")
    public BaseResponse<Boolean> editTopics(@RequestBody TopicsEditRequest topicsEditRequest, HttpServletRequest request) {
        if (topicsEditRequest == null || topicsEditRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Topics topics = new Topics();
        BeanUtils.copyProperties(topicsEditRequest, topics);
        // 数据校验
        topicsService.validTopics(topics, false);
        User loginUser = userService.getLoginUser(request);
        // 判断是否存在
        long id = topicsEditRequest.getId();
        Topics oldTopics = topicsService.getById(id);
        ThrowUtils.throwIf(oldTopics == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可编辑
        if (!oldTopics.getUserId().equals(loginUser.getId()) && !userService.isAdmin(loginUser)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        // 操作数据库
        boolean result = topicsService.updateById(topics);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    // endregion
}
