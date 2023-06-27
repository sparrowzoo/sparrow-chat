package com.sparrow.chat.contact.service;

import com.sparrow.chat.contact.protocol.qun.InviteFriendParam;
import com.sparrow.protocol.BusinessException;

public interface InviteFriendSecurity {
    String encryptInviteFriend(InviteFriendParam inviteFriendParam) throws BusinessException;
    
    InviteFriendParam parseUserSecretIdentify(String inviteFriendToken) throws BusinessException;
}
