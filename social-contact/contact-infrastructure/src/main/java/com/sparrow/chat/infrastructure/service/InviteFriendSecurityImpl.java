package com.sparrow.chat.infrastructure.service;

import com.sparrow.chat.contact.protocol.qun.InviteFriendParam;
import com.sparrow.chat.contact.service.InviteFriendSecurity;
import com.sparrow.core.spi.JsonFactory;
import com.sparrow.cryptogram.ThreeDES;
import com.sparrow.json.Json;
import com.sparrow.protocol.BusinessException;
import com.sparrow.protocol.LoginUser;
import com.sparrow.protocol.ThreadContext;

import javax.inject.Named;

@Named
public class InviteFriendSecurityImpl implements InviteFriendSecurity {
    private Json json = JsonFactory.getProvider();

    @Override
    public String encryptInviteFriend(InviteFriendParam inviteFriendParam) throws BusinessException {
        String json = this.json.toString(inviteFriendParam);
        return ThreeDES.getInstance().encryptHex(inviteFriendParam.getFriendId() + "", json);
    }

    @Override
    public InviteFriendParam parseUserSecretIdentify(String inviteFriendToken) throws BusinessException {
        LoginUser loginUser = ThreadContext.getLoginToken();
        String inviteFriendInfo = ThreeDES.getInstance().decryptHex(loginUser.getUserId() + "", inviteFriendToken);
        return (InviteFriendParam) this.json.parse(inviteFriendInfo, InviteFriendSecurity.class);
    }
}
