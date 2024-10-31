package com.zsj.blindsocial.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zsj.blindsocial.common.ErrorCode;
import com.zsj.blindsocial.exception.BusinessException;
import com.zsj.blindsocial.mapper.PostFavourMapper;
import com.zsj.blindsocial.model.entity.Post;
import com.zsj.blindsocial.model.entity.PostFavour;
import com.zsj.blindsocial.model.entity.User;
import com.zsj.blindsocial.model.vo.CommentVO;
import com.zsj.blindsocial.model.vo.PostFavourVO;
import com.zsj.blindsocial.model.vo.PostThumbVO;
import com.zsj.blindsocial.model.vo.PostVO;
import com.zsj.blindsocial.service.PostFavourService;
import com.zsj.blindsocial.service.PostService;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.zsj.blindsocial.service.UserService;
import org.springframework.aop.framework.AopContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 帖子收藏服务实现
 *
 * @author zsj

 */
@Service
public class PostFavourServiceImpl extends ServiceImpl<PostFavourMapper, PostFavour>
        implements PostFavourService {

    @Resource
    private PostService postService;
    @Resource
    private UserService userService;
    /**
     * 帖子收藏
     *
     * @param postId
     * @param loginUser
     * @return
     */
    @Override
    public int doPostFavour(long postId, User loginUser) {
        // 判断是否存在
        Post post = postService.getById(postId);
        if (post == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        // 是否已帖子收藏
        long userId = loginUser.getId();
        // 每个用户串行帖子收藏
        // 锁必须要包裹住事务方法
        PostFavourService postFavourService = (PostFavourService) AopContext.currentProxy();
        synchronized (String.valueOf(userId).intern()) {
            return postFavourService.doPostFavourInner(userId, postId);
        }
    }

    @Override
    public Page<Post> listFavourPostByPage(IPage<Post> page, Wrapper<Post> queryWrapper, long favourUserId) {
        if (favourUserId <= 0) {
            return new Page<>();
        }
        return baseMapper.listFavourPostByPage(page, queryWrapper, favourUserId);
    }

    /**
     * 封装了事务的方法
     *
     * @param userId
     * @param postId
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int doPostFavourInner(long userId, long postId) {
        PostFavour postFavour = new PostFavour();
        postFavour.setUserId(userId);
        postFavour.setPostId(postId);
        QueryWrapper<PostFavour> postFavourQueryWrapper = new QueryWrapper<>(postFavour);
        PostFavour oldPostFavour = this.getOne(postFavourQueryWrapper);
        boolean result;
        // 已收藏
        if (oldPostFavour != null) {
            result = this.remove(postFavourQueryWrapper);
            if (result) {
                // 帖子收藏数 - 1
                result = postService.update()
                        .eq("id", postId)
                        .gt("favourNum", 0)
                        .setSql("favourNum = favourNum - 1")
                        .update();
                return result ? -1 : 0;
            } else {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR);
            }
        } else {
            // 未帖子收藏
            result = this.save(postFavour);
            if (result) {
                // 帖子收藏数 + 1
                result = postService.update()
                        .eq("id", postId)
                        .setSql("favourNum = favourNum + 1")
                        .update();
                return result ? 1 : 0;
            } else {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR);
            }
        }
    }

    @Override
    public Page<PostFavourVO> getPostFavourVOPage(Page<PostFavour> favourPage, HttpServletRequest request) {
        List<PostFavour> favours = favourPage.getRecords();
        if (CollUtil.isEmpty(favours)){
            return null;
        }
        Page<PostFavourVO> favourVOPage = new Page<>(favourPage.getCurrent(), favourPage.getSize(), favourPage.getTotal());
        List<PostFavourVO> postThumbVOS = favours.stream().map(item -> {
            PostFavourVO favourVO = new PostFavourVO();
            BeanUtil.copyProperties(item, favourVO);
            Long postId = item.getPostId();
            Post post = postService.getById(postId);
            favourVO.setPostVO(PostVO.objToVo(post));
            Long userId = item.getUserId();
            User user = userService.getById(userId);
            favourVO.setUserVO(userService.getUserVO(user));
            favourVO.setType("收藏");
            return favourVO;
        }).collect(Collectors.toList());
        favourVOPage.setRecords(postThumbVOS);
        return favourVOPage;
    }

}




