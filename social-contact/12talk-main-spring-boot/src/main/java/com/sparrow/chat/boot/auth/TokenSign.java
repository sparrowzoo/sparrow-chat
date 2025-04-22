package com.sparrow.chat.boot.auth;

import com.alibaba.fastjson.JSON;
import com.sparrow.cryptogram.Base64;
import com.sparrow.cryptogram.Hmac;
import com.sparrow.protocol.LoginUser;
import com.sparrow.utility.JSUtility;

import java.nio.charset.StandardCharsets;

public class TokenSign {
    public static String sign(LoginUser loginUser, String encryptKey) {
        String userInfo = JSON.toJSONString(loginUser);
        String signature = Hmac.getInstance().getSHA1Base64(userInfo, encryptKey);
        String token = Base64.encodeBytes(userInfo.getBytes(StandardCharsets.UTF_8)) + "." + signature;
        return JSUtility.encodeURIComponent(token);
    }
}
