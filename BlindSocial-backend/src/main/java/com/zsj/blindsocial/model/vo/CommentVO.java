package com.zsj.blindsocial.model.vo;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.zsj.blindsocial.model.entity.Comment;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 评论视图
 *
 * @author zsj

 */
@Data
public class CommentVO implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

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

    /**
     * 点赞数
     */
    private Integer thumbNum;

    /**
     * 收藏数
     */
    private Integer favourNum;

    /*
    * 子评论
    * */
    private List<CommentVO> children;

    /**
     * 创建用户 id
     */
    private Long userId;
    /*
    * 用户信息
    * */
    private UserVO userVO;
    /*
    * 回复用户信息
    * */
    private UserVO reply;

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
     * @param commentVO
     * @return
     */
    public static Comment voToObj(CommentVO commentVO) {
        if (commentVO == null) {
            return null;
        }
        Comment comment = new Comment();
        BeanUtils.copyProperties(commentVO, comment);
        return comment;
    }

    /**
     * 对象转封装类
     *
     * @param comment
     * @return
     */
    public static CommentVO objToVo(Comment comment) {
        if (comment == null) {
            return null;
        }
        CommentVO commentVO = new CommentVO();
        BeanUtils.copyProperties(comment, commentVO);
        return commentVO;
    }
}
