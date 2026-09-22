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

public class RedisKey {
    /**
     * 会话内的消息 zset hash
     */
    public static final String SESSION_MESSAGE_KEY = "msg:{sessionKey}";

    /**
     * 用户的会话 string 只保存最后一条消息的时间戮
     */
    public static final String USER_SESSION_KEY = "session:{userKey}:{sessionKey}";

    /**
     * 群内用户 zset
     */
    public static final String MEMBER_OF_QUN = "qun:member:{qunId}";

    /**
     * 通讯录zset
     */
    public static final String USER_CONTACTS = "contacts:{chatType}:{userId}";
    /**
     * 群信息 string
     */
    public static final String QUN = "qun:{qunId}";

    public static final String VISITOR_ID = "visitor.id.seed";

}
