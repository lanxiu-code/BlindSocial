package com.zsj.blindsocial.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zsj.blindsocial.model.dto.topics.TopicsQueryRequest;
import com.zsj.blindsocial.model.entity.Topics;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zsj.blindsocial.model.vo.TopicsVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import javax.servlet.http.HttpServletRequest;

/**
* @author zsj
* @description 针对表【topics(话题)】的数据库操作Service
* @createDate 2024-10-30 09:41:57
*/
public interface TopicsService extends IService<Topics> {

    /**
     * 校验数据
     *
     * @param topics
     * @param add 对创建的数据进行校验
     */
    void validTopics(Topics topics, boolean add);

    /**
     * 获取查询条件
     *
     * @param topicsQueryRequest
     * @return
     */
    QueryWrapper<Topics> getQueryWrapper(TopicsQueryRequest topicsQueryRequest);

    /**
     * 获取话题封装
     *
     * @param topics
     * @param request
     * @return
     */
    TopicsVO getTopicsVO(Topics topics, HttpServletRequest request);

    /**
     * 分页获取话题封装
     *
     * @param topicsPage
     * @param request
     * @return
     */
    Page<TopicsVO> getTopicsVOPage(Page<Topics> topicsPage, HttpServletRequest request);

}
