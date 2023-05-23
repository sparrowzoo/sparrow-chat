package com.sparrow.chat.contact.protocol;

public class FindUserSecretParam {
    /**
     * 用户明文标识【邮箱或者手机号】
     */
    private String userIdentify;

    public String getUserIdentify() {
        return userIdentify;
    }

    public void setUserIdentify(String userIdentify) {
        this.userIdentify = userIdentify;
    }
}
