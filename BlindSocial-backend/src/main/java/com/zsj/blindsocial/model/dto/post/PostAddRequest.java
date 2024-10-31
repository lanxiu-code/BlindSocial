package com.zsj.blindsocial.model.dto.post;

import java.io.Serializable;
import java.util.List;
import lombok.Data;

/**
 * 创建请求
 *
 * @author zsj

 */
@Data
public class PostAddRequest implements Serializable {

    /**
     * 标题
     */
    private String title;
    /*
     * 描述
     * */
    private String description;
    /*
     * 封面图片
     * */
    private String image;
    /*
     * 话题
     * */
    private String topic;
    /**
     * 内容
     */
    private String content;

    /**
     * 标签列表
     */
    private List<String> tags;

    private static final long serialVersionUID = 1L;
}