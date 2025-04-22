package com.sparrow.chat.contact.protocol.vo;

import com.sparrow.protocol.VO;
import lombok.Data;

@Data
public class ContactVO implements VO {
    /**
     * 国籍
     */
    private String nationality;
    /**
     * 国旗url
     */
    private String flagUrl;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 用户名
     */
    private String userName;

    /**
     * 昵称
     */
    private String nickName;
    /**
     * 头象
     */
    private String avatar;

    private String signature;
    private String englishName;
    private Integer category;
}
