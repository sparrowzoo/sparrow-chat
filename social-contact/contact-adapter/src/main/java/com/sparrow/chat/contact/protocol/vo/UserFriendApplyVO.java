package com.sparrow.chat.contact.protocol.vo;

public class UserFriendApplyVO {
    private String userSecretIdentify;
    private String avatar;
    private String nickName;

    public String getUserSecretIdentify() {
        return userSecretIdentify;
    }

    public void setUserSecretIdentify(String userSecretIdentify) {
        this.userSecretIdentify = userSecretIdentify;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
}
