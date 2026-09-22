/*
Licensed to the Apache Software Foundation (ASF) under one or more
contributor license agreements.  See the NOTICE file distributed with
this work for additional information regarding copyright ownership.
The ASF licenses this file to You under the Apache License, Version 2.0
(the "License"); you may not use this file except in compliance with
the License.  You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package com.sparrow.chat.infrastructure.service;

import com.sparrow.chat.contact.protocol.qun.InviteFriendParam;
import com.sparrow.chat.contact.service.InviteFriendSecurity;
import com.sparrow.context.SessionContext;
import com.sparrow.core.spi.JsonFactory;
import com.sparrow.cryptogram.ThreeDES;
import com.sparrow.json.Json;
import com.sparrow.protocol.BusinessException;
import com.sparrow.protocol.LoginUser;
import jakarta.inject.Named;

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
        LoginUser loginUser = SessionContext.getLoginUser();
        String inviteFriendInfo = ThreeDES.getInstance().decryptHex(loginUser.getUserId() + "", inviteFriendToken);
        return (InviteFriendParam) this.json.parse(inviteFriendInfo, InviteFriendSecurity.class);
    }
}
