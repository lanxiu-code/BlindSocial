package com.zsj.blindsocial.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zsj.blindsocial.annotation.AuthCheck;
import com.zsj.blindsocial.common.BaseResponse;
import com.zsj.blindsocial.common.DeleteRequest;
import com.zsj.blindsocial.common.ErrorCode;
import com.zsj.blindsocial.common.ResultUtils;
import com.zsj.blindsocial.constant.UserConstant;
import com.zsj.blindsocial.exception.BusinessException;
import com.zsj.blindsocial.exception.ThrowUtils;
import com.zsj.blindsocial.model.dto.comment.CommentAddRequest;
import com.zsj.blindsocial.model.dto.comment.CommentEditRequest;
import com.zsj.blindsocial.model.dto.comment.CommentQueryRequest;
import com.zsj.blindsocial.model.dto.comment.CommentUpdateRequest;
import com.zsj.blindsocial.model.entity.Comment;
import com.zsj.blindsocial.model.entity.Post;
import com.zsj.blindsocial.model.entity.User;
import com.zsj.blindsocial.model.vo.CommentVO;
import com.zsj.blindsocial.service.CommentService;
import com.zsj.blindsocial.service.PostService;
import com.zsj.blindsocial.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 评论接口
 *
 * @author zsj

 */
@RestController
@RequestMapping("/comment")
@Slf4j
public class CommentController {

    @Resource
    private CommentService commentService;

    @Resource
    private UserService userService;

    @Resource
    private PostService postService;

    // region 增删改查

    /**
     * 创建评论
     *
     * @param commentAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    public BaseResponse<Long> addComment(@RequestBody CommentAddRequest commentAddRequest, HttpServletRequest request) {
        ThrowUtils.throwIf(commentAddRequest == null, ErrorCode.PARAMS_ERROR);
        // todo 在此处将实体类和 DTO 进行转换
        Comment comment = new Comment();
        BeanUtils.copyProperties(commentAddRequest, comment);
        // 数据校验
        commentService.validComment(comment, true);
        // todo 填充默认值
        User loginUser = userService.getLoginUser(request);
        comment.setUserId(loginUser.getId());
        // 写入数据库
        boolean result = commentService.save(comment);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        // 返回新写入的数据 id
        long newCommentId = comment.getId();
        return ResultUtils.success(newCommentId);
    }

    /**
     * 删除评论
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteComment(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getLoginUser(request);
        long id = deleteRequest.getId();
        // 判断是否存在
        Comment oldComment = commentService.getById(id);
        ThrowUtils.throwIf(oldComment == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可删除
        if (!oldComment.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        // 操作数据库
        boolean result = commentService.removeById(id);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 更新评论（仅管理员可用）
     *
     * @param commentUpdateRequest
     * @return
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateComment(@RequestBody CommentUpdateRequest commentUpdateRequest) {
        if (commentUpdateRequest == null || commentUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // todo 在此处将实体类和 DTO 进行转换
        Comment comment = new Comment();
        BeanUtils.copyProperties(commentUpdateRequest, comment);
        // 数据校验
        commentService.validComment(comment, false);
        // 判断是否存在
        long id = commentUpdateRequest.getId();
        Comment oldComment = commentService.getById(id);
        ThrowUtils.throwIf(oldComment == null, ErrorCode.NOT_FOUND_ERROR);
        // 操作数据库
        boolean result = commentService.updateById(comment);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 根据 id 获取评论（封装类）
     *
     * @param id
     * @return
     */
    @GetMapping("/get/vo")
    public BaseResponse<CommentVO> getCommentVOById(long id, HttpServletRequest request) {
        ThrowUtils.throwIf(id <= 0, ErrorCode.PARAMS_ERROR);
        // 查询数据库
        Comment comment = commentService.getById(id);
        ThrowUtils.throwIf(comment == null, ErrorCode.NOT_FOUND_ERROR);
        // 获取封装类
        return ResultUtils.success(commentService.getCommentVO(comment, request));
    }

    /**
     * 分页获取评论列表（仅管理员可用）
     *
     * @param commentQueryRequest
     * @return
     */
    @PostMapping("/list/page")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<Comment>> listCommentByPage(@RequestBody CommentQueryRequest commentQueryRequest) {
        long current = commentQueryRequest.getCurrent();
        long size = commentQueryRequest.getPageSize();
        // 查询数据库
        Page<Comment> commentPage = commentService.page(new Page<>(current, size),
                commentService.getQueryWrapper(commentQueryRequest));
        return ResultUtils.success(commentPage);
    }

    /**
     * 分页获取评论列表（封装类）
     *
     * @param commentQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/list/page/vo")
    public BaseResponse<Page<CommentVO>> listCommentVOByPage(@RequestBody CommentQueryRequest commentQueryRequest,
                                                               HttpServletRequest request) {
        long current = commentQueryRequest.getCurrent();
        long size = commentQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        QueryWrapper<Comment> wrapper = commentService
                .getQueryWrapper(commentQueryRequest)
                .orderByDesc("createTime")
                .eq("parentId",-1);
        // 查询数据库
        Page<Comment> commentPage = commentService.page(new Page<>(current, size),wrapper);
        // 获取封装类
        return ResultUtils.success(commentService.getCommentVOPage(commentPage, request));
    }

    /**
     * 分页获取当前登录用户创建的评论列表
     *
     * @param commentQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/my/list/page/vo")
    public BaseResponse<Page<CommentVO>> listMyCommentVOByPage(@RequestBody CommentQueryRequest commentQueryRequest,
                                                                 HttpServletRequest request) {
        ThrowUtils.throwIf(commentQueryRequest == null, ErrorCode.PARAMS_ERROR);
        // 补充查询条件，只查询当前登录用户的数据
        User loginUser = userService.getLoginUser(request);
        long current = commentQueryRequest.getCurrent();
        long size = commentQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        // 查询我发布的帖子
        List<Long> myPostIds = postService.list(Wrappers
                .lambdaQuery(Post.class)
                .eq(Post::getUserId, loginUser.getId())
        ).stream().map(Post::getId).collect(Collectors.toList());
        QueryWrapper<Comment> wrapper = commentService
                .getQueryWrapper(commentQueryRequest)
                .ne("userId", loginUser.getId())
                .in("postId", myPostIds);
        // 查询数据库
        Page<Comment> commentPage = commentService.page(new Page<>(current, size),wrapper);
        // 获取封装类
        return ResultUtils.success(commentService.getMyCommentVOPage(commentPage, request));
    }

    /**
     * 编辑评论（给用户使用）
     *
     * @param commentEditRequest
     * @param request
     * @return
     */
    @PostMapping("/edit")
    public BaseResponse<Boolean> editComment(@RequestBody CommentEditRequest commentEditRequest, HttpServletRequest request) {
        if (commentEditRequest == null || commentEditRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // todo 在此处将实体类和 DTO 进行转换
        Comment comment = new Comment();
        BeanUtils.copyProperties(commentEditRequest, comment);
        // 数据校验
        commentService.validComment(comment, false);
        User loginUser = userService.getLoginUser(request);
        // 判断是否存在
        long id = commentEditRequest.getId();
        Comment oldComment = commentService.getById(id);
        ThrowUtils.throwIf(oldComment == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可编辑
        if (!oldComment.getUserId().equals(loginUser.getId()) && !userService.isAdmin(loginUser)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        // 操作数据库
        boolean result = commentService.updateById(comment);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    // endregion
}
