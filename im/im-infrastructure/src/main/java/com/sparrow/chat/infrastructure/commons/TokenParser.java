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

package com.sparrow.chat.infrastructure.commons;

import com.sparrow.authenticator.DefaultLoginUser;
import com.sparrow.authenticator.enums.AuthenticatorError;
import com.sparrow.core.spi.JsonFactory;
import com.sparrow.enums.HttpMethod;
import com.sparrow.json.Json;
import com.sparrow.protocol.BusinessException;
import com.sparrow.protocol.LoginUser;
import com.sparrow.utility.HttpClient;

import java.util.HashMap;
import java.util.Map;

public class TokenParser {
    public static LoginUser parseUserId(String token) throws BusinessException {
        DefaultLoginUser loginUser = new DefaultLoginUser();
        if (token.contains("mock.")) {
            loginUser.setUserId(Long.valueOf(token.substring("mock.".length())));
            return loginUser;
        }
        Map<String, String> header = new HashMap<>();
        header.put("X-Sugar-Token", token);
        String result = HttpClient.request(HttpMethod.GET, "http://studyapi.zhilongsoft.com/app/authMember/info"
                , "", null, header, false);
        Json json = JsonFactory.getProvider();
        Map<String, Object> map = json.parse(result);
        Integer code = (Integer) map.get("code");
        if (code.equals(200)) {
            Map<String, Object> userMap = (Map<String, Object>) map.get("data");
            Map<String, Object> userProperty = (Map<String, Object>) userMap.get("member");
            loginUser.setUserId(Long.valueOf(userProperty.get("id").toString()));
            loginUser.setUserName(userProperty.get("name").toString());
            return loginUser;
        }
        throw new BusinessException(AuthenticatorError.USER_NOT_LOGIN);
    }
}
