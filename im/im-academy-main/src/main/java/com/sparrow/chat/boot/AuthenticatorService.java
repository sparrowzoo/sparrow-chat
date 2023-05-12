/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.sparrow.chat.boot;

import com.sparrow.chat.protocol.constant.Chat;
import com.sparrow.core.spi.JsonFactory;
import com.sparrow.enums.HttpMethod;
import com.sparrow.json.Json;
import com.sparrow.protocol.BusinessException;
import com.sparrow.protocol.LoginUser;
import com.sparrow.protocol.LoginUserStatus;
import com.sparrow.protocol.constant.SparrowError;
import com.sparrow.support.AbstractAuthenticatorService;
import com.sparrow.utility.HttpClient;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class AuthenticatorService extends AbstractAuthenticatorService {

    @Override protected String getEncryptKey() {
        return null;
    }

    @Override protected String getDecryptKey() {
        return null;
    }

    @Override
    protected String sign(LoginUser loginUser, String secretKey) {
        return null;
    }

    @Override public LoginUser authenticate(String token, String deviceId) throws BusinessException {
        return this.verify(token, this.getDecryptKey());
    }

    @Override protected LoginUser verify(String token, String secretKey) throws BusinessException {
        LoginUser loginUser = new LoginUser();
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
            boolean isPlatform="1".equals(userProperty.getOrDefault("isCustomer","0"));
            loginUser.getExtensions().put(Chat.PLATFORM,isPlatform);
            return loginUser;
        }
        throw new BusinessException(SparrowError.USER_NOT_LOGIN);
    }

    @Override protected void setUserStatus(LoginUser loginUser, LoginUserStatus loginUserStatus) {

    }

    @Override
    protected LoginUserStatus getUserStatus(Long userId) {
        //todo 从缓存中获取
        return null;
    }

    @Override
    protected LoginUserStatus getUserStatusFromDB(Long userId) {
        return null;
    }

    @Override
    protected void renewal(LoginUser loginUser, LoginUserStatus loginUserStatus) {
        //如果过期时间小于30分钟，延长1小时
        if (loginUserStatus.getExpireAt() - System.currentTimeMillis() < Duration.ofMinutes(30).toMillis())
            loginUserStatus.setExpireAt(System.currentTimeMillis() + Duration.ofHours(1).toMillis());
    }
}
