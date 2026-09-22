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

package com.sparrow.chat.domain.service;

import com.sparrow.authenticator.AuthenticationInfo;
import com.sparrow.authenticator.Authenticator;
import com.sparrow.authenticator.DefaultLoginUser;
import com.sparrow.protocol.BusinessException;
import com.sparrow.protocol.LoginUser;
import com.sparrow.servlet.ServletContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserLoginService {

    @Autowired
    private ServletContainer springServletContainer;

    @Autowired
    private Authenticator authenticator;


    public String login(Long userId) throws BusinessException {
        DefaultLoginUser loginUser = new DefaultLoginUser();
        loginUser.setUserId(userId);
        loginUser.setUserName("张" + userId);
        loginUser.setCategory(LoginUser.CATEGORY_REGISTER);
        loginUser.setNickName(loginUser.getUserName());
        loginUser.setAvatar("");
        loginUser.setDays(1D);
        loginUser.setHost(springServletContainer.getClientIp());
        loginUser.setExpireAt(System.currentTimeMillis() + 3600 * 1000 * 24);
        return this.authenticator.login(new AuthenticationInfo() {
            @Override
            public LoginUser getUser() {
                return loginUser;
            }

            @Override
            public String getCredential() {
                return "";
            }
        });
    }
}
