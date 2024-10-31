package com.zsj.blindsocial.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zsj.blindsocial.common.ErrorCode;
import com.zsj.blindsocial.constant.CommonConstant;
import com.zsj.blindsocial.exception.ThrowUtils;
import com.zsj.blindsocial.model.dto.comment.CommentQueryRequest;
import com.zsj.blindsocial.model.entity.Comment;
import com.zsj.blindsocial.model.entity.User;
import com.zsj.blindsocial.model.vo.CommentVO;
import com.zsj.blindsocial.model.vo.UserVO;
import com.zsj.blindsocial.service.CommentService;
import com.zsj.blindsocial.mapper.CommentMapper;
import com.zsj.blindsocial.service.UserService;
import com.zsj.blindsocial.utils.SqlUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
* @author 蓝朽
* @description 针对表【comment(评论)】的数据库操作Service实现
* @createDate 2024-10-30 13:50:23
*/
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment>
    implements CommentService{
    @Resource
    private UserService userService;
    @Override
    public void validComment(Comment comment, boolean add) {
        ThrowUtils.throwIf(comment == null, ErrorCode.PARAMS_ERROR);
        String content = comment.getContent();
        Long postId = comment.getPostId();
        // 创建数据时，参数不能为空
        if (add) {
            ThrowUtils.throwIf(StringUtils.isBlank(content), ErrorCode.PARAMS_ERROR);
            ThrowUtils.throwIf(ObjectUtil.isNull(postId), ErrorCode.PARAMS_ERROR);
        }
        // 修改数据时，有参数则校验
        if (StringUtils.isNotBlank(content)) {
            ThrowUtils.throwIf(content.length() > 100, ErrorCode.PARAMS_ERROR, "评论过长");
        }
    }

    @Override
    public QueryWrapper<Comment> getQueryWrapper(CommentQueryRequest commentQueryRequest) {
        QueryWrapper<Comment> queryWrapper = new QueryWrapper<>();
        if (commentQueryRequest == null) {
            return queryWrapper;
        }
        Long id = commentQueryRequest.getId();
        Long postId = commentQueryRequest.getPostId();
        Long parentId = commentQueryRequest.getParentId();
        Long replyId = commentQueryRequest.getReplyId();
        Long userId = commentQueryRequest.getUserId();
        String sortField = commentQueryRequest.getSortField();
        String sortOrder = commentQueryRequest.getSortOrder();
        // 精确查询
        queryWrapper.eq(ObjectUtil.isNotEmpty(postId), "postId", postId);
        queryWrapper.eq(ObjectUtil.isNotEmpty(id), "id", id);
        queryWrapper.eq(ObjectUtil.isNotEmpty(userId), "userId", userId);
        queryWrapper.eq(ObjectUtil.isNotEmpty(parentId), "parentId", parentId);
        queryWrapper.eq(ObjectUtil.isNotEmpty(replyId), "replyId", replyId);
        // 排序规则
        queryWrapper.orderBy(SqlUtils.validSortField(sortField),
                sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }

    @Override
    public CommentVO getCommentVO(Comment comment, HttpServletRequest request) {
        // 对象转封装类
        CommentVO commentVO = CommentVO.objToVo(comment);
        Long userId = comment.getUserId();
        Long replyId = comment.getReplyId();
        Map<Long, User> userMap = userService
                .list(Wrappers.lambdaQuery(User.class)
                        .in(User::getId, CollUtil.toList(userId, replyId)))
                .stream()
                .collect(Collectors.toMap(User::getId, Function.identity()));
        commentVO.setUserVO(userService.getUserVO(userMap.get(userId)));
        if(replyId != null){
            commentVO.setReply(userService.getUserVO(userMap.get(replyId)));
        }
        return commentVO;
    }

    private void getSubComment(Long postId, Long parentId,List<CommentVO> result,HttpServletRequest request) {
        List<Comment> comments = list(Wrappers
                .lambdaQuery(Comment.class)
                .eq(Comment::getPostId, postId)
                .eq(Comment::getParentId, parentId)
                .orderByDesc(Comment::getCreateTime));
        if (CollUtil.isEmpty(comments)) {
            return;
        }
        List<CommentVO> temp = comments.stream()
                .map(item -> getCommentVO(item, request))
                .collect(Collectors.toList());
        result.addAll(temp);
        for (CommentVO commentVO : temp) {
            getSubComment(postId, commentVO.getId(),result,request);
        }
    }
    @Override
    public Page<CommentVO> getCommentVOPage(Page<Comment> commentPage, HttpServletRequest request) {
        List<Comment> commentList = commentPage.getRecords();
        Page<CommentVO> commentVOPage = new Page<>(commentPage.getCurrent(), commentPage.getSize(), commentPage.getTotal());
        if (CollUtil.isEmpty(commentList)) {
            return commentVOPage;
        }
        // 获取子评论
        List<CommentVO> commentVOList = commentList.stream().map(comment -> {
            CommentVO commentVO = getCommentVO(comment,request);
            ArrayList<CommentVO> result = new ArrayList<>();
            getSubComment(comment.getPostId(), comment.getId(),result,request);
            commentVO.setChildren(result);
            return commentVO;
        }).collect(Collectors.toList());
        // endregion
        commentVOPage.setRecords(commentVOList);
        return commentVOPage;
    }

    @Override
    public Page<CommentVO> getMyCommentVOPage(Page<Comment> commentPage, HttpServletRequest request) {
        List<Comment> commentList = commentPage.getRecords();
        Page<CommentVO> commentVOPage = new Page<>(commentPage.getCurrent(), commentPage.getSize(), commentPage.getTotal());
        if (CollUtil.isEmpty(commentList)) {
            return commentVOPage;
        }
        List<CommentVO> myCommentVo = new ArrayList<>();
        // 获取子评论
        commentList.forEach(comment -> {
            CommentVO commentVO = getCommentVO(comment,request);
            myCommentVo.add(commentVO);
            ArrayList<CommentVO> result = new ArrayList<>();
            getSubComment(comment.getPostId(), comment.getId(),result,request);
            myCommentVo.addAll(result);
        });
        // endregion
        commentVOPage.setRecords(myCommentVo);
        return commentVOPage;
    }
}




