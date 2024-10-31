package com.zsj.blindsocial.model.dto.comment;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 创建评论请求
 *
 * @author zsj

 */
@Data
public class CommentAddRequest implements Serializable {


    /**
     * 评论内容
     */
    private String content;

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

    private static final long serialVersionUID = 1L;
}