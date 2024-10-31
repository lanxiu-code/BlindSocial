package com.zsj.blindsocial.model.dto.topics;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 更新话题请求
 *
 * @author zsj

 */
@Data
public class TopicsUpdateRequest implements Serializable {

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