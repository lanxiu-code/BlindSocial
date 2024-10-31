package com.zsj.blindsocial.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zsj.blindsocial.model.dto.comment.CommentQueryRequest;
import com.zsj.blindsocial.model.entity.Comment;
import com.zsj.blindsocial.model.vo.CommentVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import javax.servlet.http.HttpServletRequest;

/**
* @author zsj
* @description 针对表【comment(评论)】的数据库操作Service
* @createDate 2024-10-30 13:50:23
*/
public interface CommentService extends IService<Comment> {

    /**
     * 校验数据
     *
     * @param comment
     * @param add 对创建的数据进行校验
     */
    void validComment(Comment comment, boolean add);

    /**
     * 获取查询条件
     *
     * @param commentQueryRequest
     * @return
     */
    QueryWrapper<Comment> getQueryWrapper(CommentQueryRequest commentQueryRequest);

    /**
     * 获取评论封装
     *
     * @param comment
     * @param request
     * @return
     */
    CommentVO getCommentVO(Comment comment, HttpServletRequest request);

    /**
     * 分页获取评论封装
     *
     * @param commentPage
     * @param request
     * @return
     */
    Page<CommentVO> getCommentVOPage(Page<Comment> commentPage, HttpServletRequest request);
    /**
     * 分页获取我的评论封装
     *
     * @param commentPage
     * @param request
     * @return
     */
    Page<CommentVO> getMyCommentVOPage(Page<Comment> commentPage, HttpServletRequest request);
}
