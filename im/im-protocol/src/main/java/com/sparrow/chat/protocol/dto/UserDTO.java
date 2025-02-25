package com.sparrow.chat.protocol.dto;

import com.sparrow.chat.protocol.ChatUser;
import com.sparrow.protocol.POJO;

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
    private ChatUser chatUser;
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

    public Long getAddTime() {
        return addTime;
    }

    public void setAddTime(Long addTime) {
        this.addTime = addTime;
    }

    @Override
    public int compareTo(UserDTO o) {
        return this.userName.compareTo(o.getUserName());
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public ChatUser getChatUser() {
        return chatUser;
    }

    public void setChatUser(ChatUser chatUser) {
        this.chatUser = chatUser;
    }
}
