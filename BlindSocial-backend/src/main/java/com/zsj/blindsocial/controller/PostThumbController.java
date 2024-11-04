package com.zsj.blindsocial.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zsj.blindsocial.common.BaseResponse;
import com.zsj.blindsocial.common.ErrorCode;
import com.zsj.blindsocial.common.ResultUtils;
import com.zsj.blindsocial.exception.BusinessException;
import com.zsj.blindsocial.exception.ThrowUtils;
import com.zsj.blindsocial.model.dto.post.PostQueryRequest;
import com.zsj.blindsocial.model.dto.postthumb.PostThumbAddRequest;
import com.zsj.blindsocial.model.entity.Post;
import com.zsj.blindsocial.model.entity.PostFavour;
import com.zsj.blindsocial.model.entity.PostThumb;
import com.zsj.blindsocial.model.entity.User;
import com.zsj.blindsocial.model.vo.PostFavourVO;
import com.zsj.blindsocial.model.vo.PostThumbVO;
import com.zsj.blindsocial.service.PostService;
import com.zsj.blindsocial.service.PostThumbService;
import com.zsj.blindsocial.service.UserService;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 帖子点赞接口
 *
 * @author zsj

 */
@RestController
@RequestMapping("/post_thumb")
@Slf4j
public class PostThumbController {

    @Resource
    private PostThumbService postThumbService;
    @Resource
    private PostService postService;
    @Resource
    private UserService userService;
    /*
     * 获取收藏列表
     * */
    @PostMapping("/my/thumb/list/page")
    public BaseResponse<Page<PostThumbVO>> listMyThumbVOByPage(@RequestBody PostQueryRequest postQueryRequest,
                                                               HttpServletRequest request) {
        if (postQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        long current = postQueryRequest.getCurrent();
        long size = postQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        // 查询我发布的帖子列表
        LambdaQueryWrapper<Post> postWrapper = Wrappers.lambdaQuery(Post.class)
                .eq(Post::getUserId, loginUser.getId());
        List<Long> myPostIds = postService.list(postWrapper)
                .stream().map(Post::getId).collect(Collectors.toList());
        if (myPostIds.isEmpty()){
            return ResultUtils.success(new Page<>());
        }
        // 查询点赞我的
        LambdaQueryWrapper<PostThumb> favourWrapper = Wrappers.lambdaQuery(PostThumb.class)
                .ne(PostThumb::getUserId, loginUser.getId())
                .in(PostThumb::getPostId, myPostIds);
        Page<PostThumb> thumbPage = postThumbService.page(new Page<>(current, size), favourWrapper);
        return ResultUtils.success(postThumbService.getPostThumbVOPage(thumbPage, request));
    }
    /**
     * 点赞 / 取消点赞
     *
     * @param postThumbAddRequest
     * @param request
     * @return resultNum 本次点赞变化数
     */
    @PostMapping("/")
    public BaseResponse<Integer> doThumb(@RequestBody PostThumbAddRequest postThumbAddRequest,
            HttpServletRequest request) {
        if (postThumbAddRequest == null || postThumbAddRequest.getPostId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 登录才能点赞
        final User loginUser = userService.getLoginUser(request);
        long postId = postThumbAddRequest.getPostId();
        int result = postThumbService.doPostThumb(postId, loginUser);
        return ResultUtils.success(result);
    }

}
