package com.sparrow.chat.boot;

import com.sparrow.protocol.BusinessException;
import com.sparrow.protocol.LoginUser;
import com.sparrow.protocol.LoginUserStatus;
import com.sparrow.support.AbstractAuthenticatorService;

public class SparrowAuthenticator extends AbstractAuthenticatorService {
    @Override protected String getEncryptKey() {
        return null;
    }

    @Override protected String getDecryptKey() {
        return null;
    }

    @Override protected String sign(LoginUser loginUser, String secretKey) {
        return null;
    }

    @Override protected LoginUser verify(String token, String secretKey) throws BusinessException {
        return null;
    }

    @Override protected void setUserStatus(LoginUser loginUser, LoginUserStatus loginUserStatus) {

    }

    @Override protected LoginUserStatus getUserStatus(Long userId) {
        return null;
    }

    @Override protected LoginUserStatus getUserStatusFromDB(Long userId) {
        return null;
    }

    @Override protected void renewal(LoginUser loginUser, LoginUserStatus loginUserStatus) {

    }
}
