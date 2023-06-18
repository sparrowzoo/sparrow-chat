package com.sparrow.chat.contact.protocol.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("好友申请密文标识vo")
public class UserFriendApplyVO {
    @ApiModelProperty("用户密文标识")
    private String userSecretIdentify;
    @ApiModelProperty("用户头象")
    private String avatar;
    @ApiModelProperty("用户昵称")
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
