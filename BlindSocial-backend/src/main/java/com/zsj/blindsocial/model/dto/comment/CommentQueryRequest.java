package com.zsj.blindsocial.model.dto.comment;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.zsj.blindsocial.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 查询评论请求
 *
 * @author zsj

 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CommentQueryRequest extends PageRequest implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 帖子 id
     */
    private Long postId;

    /**
     * 父评论 id(-1-根评论)
     */
    private Long parentId;

    /*
    * 回复用户id
    * */
    private Long replyId;
    /**
     * 创建用户 id
     */
    private Long userId;


    private static final long serialVersionUID = 1L;
}