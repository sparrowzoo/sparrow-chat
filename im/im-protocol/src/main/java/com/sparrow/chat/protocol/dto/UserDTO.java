package com.sparrow.chat.protocol.dto;

import com.sparrow.chat.protocol.query.ChatUserQuery;
import com.sparrow.protocol.POJO;
import lombok.Data;

@Data
public class UserDTO implements POJO, Comparable<UserDTO> {
    /**
     * 国籍
     */
    private String nationality;
    /**
     * 国旗url
     */
    private String flagUrl;
    /**
     * 用户
     */
    private ChatUserQuery chatUser;
    /**
     * 用户名
     */
    private String userName;
    /**
     * 头象
     */
    private String avatar;
    /**
     * 加入时间
     */
    private Long addTime;

    /**
     * 个性签名
     */
    private String signature;

    @Override
    public int compareTo(UserDTO o) {
        return this.userName.compareTo(o.getUserName());
    }
}
