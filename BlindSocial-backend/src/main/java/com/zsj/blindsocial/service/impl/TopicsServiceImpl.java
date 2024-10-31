package com.zsj.blindsocial.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zsj.blindsocial.common.ErrorCode;
import com.zsj.blindsocial.constant.CommonConstant;
import com.zsj.blindsocial.exception.ThrowUtils;
import com.zsj.blindsocial.model.dto.topics.TopicsQueryRequest;
import com.zsj.blindsocial.model.entity.Topics;
import com.zsj.blindsocial.model.vo.TopicsVO;
import com.zsj.blindsocial.service.TopicsService;
import com.zsj.blindsocial.mapper.TopicsMapper;
import com.zsj.blindsocial.utils.SqlUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
* @author zsj
* @description 针对表【topics(话题)】的数据库操作Service实现
* @createDate 2024-10-30 09:41:57
*/
@Service
public class TopicsServiceImpl extends ServiceImpl<TopicsMapper, Topics>
    implements TopicsService{

    @Override
    public void validTopics(Topics topics, boolean add) {
        ThrowUtils.throwIf(topics == null, ErrorCode.PARAMS_ERROR);
        String name = topics.getName();
        // 创建数据时，参数不能为空
        if (add) {
            ThrowUtils.throwIf(StringUtils.isBlank(name), ErrorCode.PARAMS_ERROR);
        }
        // 修改数据时，有参数则校验
        if (StringUtils.isNotBlank(name)) {
            ThrowUtils.throwIf(name.length() > 80, ErrorCode.PARAMS_ERROR, "标题过长");
        }
    }

    @Override
    public QueryWrapper<Topics> getQueryWrapper(TopicsQueryRequest topicsQueryRequest) {
        QueryWrapper<Topics> queryWrapper = new QueryWrapper<>();
        if (topicsQueryRequest == null) {
            return queryWrapper;
        }
        Long id = topicsQueryRequest.getId();
        String name = topicsQueryRequest.getName();
        String sortField = topicsQueryRequest.getSortField();
        String sortOrder = topicsQueryRequest.getSortOrder();
        // 模糊查询
        queryWrapper.like(StringUtils.isNotBlank(name), "name", name);
        // 精确查询
        queryWrapper.eq(ObjectUtils.isNotEmpty(id), "id", id);
        // 排序规则
        queryWrapper.orderBy(SqlUtils.validSortField(sortField),
                sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }

    @Override
    public TopicsVO getTopicsVO(Topics topics, HttpServletRequest request) {
        return TopicsVO.objToVo(topics);
    }

    @Override
    public Page<TopicsVO> getTopicsVOPage(Page<Topics> topicsPage, HttpServletRequest request) {
        List<Topics> topicsList = topicsPage.getRecords();
        Page<TopicsVO> topicsVOPage = new Page<>(topicsPage.getCurrent(), topicsPage.getSize(), topicsPage.getTotal());
        if (CollUtil.isEmpty(topicsList)) {
            return topicsVOPage;
        }
        // 对象列表 => 封装对象列表
        List<TopicsVO> topicsVOList = topicsList.stream().map(TopicsVO::objToVo).collect(Collectors.toList());
        topicsVOPage.setRecords(topicsVOList);
        return topicsVOPage;
    }
}




