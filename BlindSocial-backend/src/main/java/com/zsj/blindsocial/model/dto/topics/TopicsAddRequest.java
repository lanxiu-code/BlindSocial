package com.zsj.blindsocial.model.dto.topics;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 创建话题请求
 *
 * @author zsj

 */
@Data
public class TopicsAddRequest implements Serializable {

    /**
     * 话题名
     */
    private String name;

    private static final long serialVersionUID = 1L;
}