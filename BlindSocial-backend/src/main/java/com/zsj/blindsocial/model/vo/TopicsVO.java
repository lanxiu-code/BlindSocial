package com.zsj.blindsocial.model.vo;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.zsj.blindsocial.model.entity.Topics;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 话题视图
 *
 * @author zsj

 */
@Data
public class TopicsVO implements Serializable {
    /**
     * id
     */

    private Long id;

    /**
     * 话题名
     */
    private String name;

    /**
     * 创建用户 id
     */
    private Long userId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;


    /**
     * 封装类转对象
     *
     * @param topicsVO
     * @return
     */
    public static Topics voToObj(TopicsVO topicsVO) {
        if (topicsVO == null) {
            return null;
        }
        Topics topics = new Topics();
        BeanUtils.copyProperties(topicsVO, topics);
        return topics;
    }

    /**
     * 对象转封装类
     *
     * @param topics
     * @return
     */
    public static TopicsVO objToVo(Topics topics) {
        if (topics == null) {
            return null;
        }
        TopicsVO topicsVO = new TopicsVO();
        BeanUtils.copyProperties(topics, topicsVO);
        return topicsVO;
    }
}
