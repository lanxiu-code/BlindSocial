package com.zsj.blindsocial.model.dto.topics;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.zsj.blindsocial.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 查询话题请求
 *
 * @author zsj

 */
@EqualsAndHashCode(callSuper = true)
@Data
public class TopicsQueryRequest extends PageRequest implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 话题名
     */
    private String name;


    private static final long serialVersionUID = 1L;
}