package com.zsj.blindsocial.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zsj.blindsocial.model.entity.PostThumb;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zsj.blindsocial.model.entity.User;
import com.zsj.blindsocial.model.vo.PostFavourVO;
import com.zsj.blindsocial.model.vo.PostThumbVO;

import javax.servlet.http.HttpServletRequest;

/**
 * 帖子点赞服务
 *
 * @author zsj

 */
public interface PostThumbService extends IService<PostThumb> {

    /**
     * 点赞
     *
     * @param postId
     * @param loginUser
     * @return
     */
    int doPostThumb(long postId, User loginUser);

    /**
     * 帖子点赞（内部服务）
     *
     * @param userId
     * @param postId
     * @return
     */
    int doPostThumbInner(long userId, long postId);

    Page<PostThumbVO> getPostThumbVOPage(Page<PostThumb> thumbPage, HttpServletRequest request);
}
