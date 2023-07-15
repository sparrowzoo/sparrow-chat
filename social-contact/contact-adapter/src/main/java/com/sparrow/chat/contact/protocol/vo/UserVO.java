package com.sparrow.chat.contact.protocol.vo;

import com.sparrow.protocol.VO;

public class UserVO implements VO {
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
     * 头象
     */
    private String avatar;

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getFlagUrl() {
        return flagUrl;
    }

    public void setFlagUrl(String flagUrl) {
        this.flagUrl = flagUrl;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
