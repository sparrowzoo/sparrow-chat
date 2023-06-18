package com.sparrow.chat.contact.protocol;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("查找用户密文标识")
public class FindUserSecretParam {
    /**
     * 用户明文标识【邮箱或者手机号】
     */
    @ApiModelProperty("用户名文标识")
    private String userIdentify;

    public String getUserIdentify() {
        return userIdentify;
    }

    public void setUserIdentify(String userIdentify) {
        this.userIdentify = userIdentify;
    }
}
