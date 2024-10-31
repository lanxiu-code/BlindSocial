package com.zsj.blindsocial.model.vo;

import lombok.Data;

import java.util.Date;

@Data
public class PostFavourVO {
    private Long id;
    private String type;
    private UserVO userVO;
    private PostVO postVO;
    /**
     * 创建时间
     */
    private Date createTime;
}
