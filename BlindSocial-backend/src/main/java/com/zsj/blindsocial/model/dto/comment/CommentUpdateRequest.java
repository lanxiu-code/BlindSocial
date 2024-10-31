package com.zsj.blindsocial.model.dto.comment;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 更新评论请求
 *
 * @author zsj

 */
@Data
public class CommentUpdateRequest implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 评论内容
     */
    private String content;



    private static final long serialVersionUID = 1L;
}