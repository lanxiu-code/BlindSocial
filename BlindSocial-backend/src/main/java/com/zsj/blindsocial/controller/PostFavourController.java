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
import com.zsj.blindsocial.model.dto.postfavour.PostFavourAddRequest;
import com.zsj.blindsocial.model.dto.postfavour.PostFavourQueryRequest;
import com.zsj.blindsocial.model.entity.Post;
import com.zsj.blindsocial.model.entity.PostFavour;
import com.zsj.blindsocial.model.entity.PostThumb;
import com.zsj.blindsocial.model.entity.User;
import com.zsj.blindsocial.model.vo.PostFavourVO;
import com.zsj.blindsocial.model.vo.PostVO;
import com.zsj.blindsocial.service.PostFavourService;
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
 * 帖子收藏接口
 *
 * @author zsj

 */
@RestController
@RequestMapping("/post_favour")
@Slf4j
public class PostFavourController {

    @Resource
    private PostFavourService postFavourService;
    @Resource
    private PostService postService;

    @Resource
    private UserService userService;

    /*
    * 获取收藏列表
    * */
    @PostMapping("/my/favour/list/page")
    public BaseResponse<Page<PostFavourVO>> listMyFavourVOByPage(@RequestBody PostQueryRequest postQueryRequest,
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
        // 查询收藏我的
        LambdaQueryWrapper<PostFavour> favourWrapper = Wrappers.lambdaQuery(PostFavour.class)
                .ne(PostFavour::getUserId, loginUser.getId())
                .in(PostFavour::getPostId, myPostIds);
        Page<PostFavour> favourPage = postFavourService.page(new Page<>(current, size), favourWrapper);
        return ResultUtils.success(postFavourService.getPostFavourVOPage(favourPage, request));
    }
    /**
     * 收藏 / 取消收藏
     *
     * @param postFavourAddRequest
     * @param request
     * @return resultNum 收藏变化数
     */
    @PostMapping("/")
    public BaseResponse<Integer> doPostFavour(@RequestBody PostFavourAddRequest postFavourAddRequest,
            HttpServletRequest request) {
        if (postFavourAddRequest == null || postFavourAddRequest.getPostId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 登录才能操作
        final User loginUser = userService.getLoginUser(request);
        long postId = postFavourAddRequest.getPostId();
        int result = postFavourService.doPostFavour(postId, loginUser);
        return ResultUtils.success(result);
    }

    /**
     * 获取我收藏的帖子列表
     *
     * @param postQueryRequest
     * @param request
     */
    @PostMapping("/my/list/page")
    public BaseResponse<Page<PostVO>> listMyFavourPostByPage(@RequestBody PostQueryRequest postQueryRequest,
            HttpServletRequest request) {
        if (postQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        long current = postQueryRequest.getCurrent();
        long size = postQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        Page<Post> postPage = postFavourService.listFavourPostByPage(new Page<>(current, size),
                postService.getQueryWrapper(postQueryRequest), loginUser.getId());
        return ResultUtils.success(postService.getPostVOPage(postPage, request));
    }

    /**
     * 获取用户收藏的帖子列表
     *
     * @param postFavourQueryRequest
     * @param request
     */
    @PostMapping("/list/page")
    public BaseResponse<Page<PostVO>> listFavourPostByPage(@RequestBody PostFavourQueryRequest postFavourQueryRequest,
            HttpServletRequest request) {
        if (postFavourQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long current = postFavourQueryRequest.getCurrent();
        long size = postFavourQueryRequest.getPageSize();
        Long userId = postFavourQueryRequest.getUserId();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20 || userId == null, ErrorCode.PARAMS_ERROR);
        Page<Post> postPage = postFavourService.listFavourPostByPage(new Page<>(current, size),
                postService.getQueryWrapper(postFavourQueryRequest.getPostQueryRequest()), userId);
        return ResultUtils.success(postService.getPostVOPage(postPage, request));
    }
}
