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

package com.sparrow.chat.protocol.dto;

import com.sparrow.chat.protocol.query.ChatUserQuery;
import com.sparrow.protocol.POJO;
import lombok.Data;

@Data
public class MessageDTO implements POJO {
    private Long messageId;
    private String sessionKey;
    private Integer chatType;
    /**
     * 业务消息类型 ID，参见 com.sparrow.chat.protocol.enums.MessageType。
     */
    private int messageType;
    /**
     * 发送者
     */
    private ChatUserQuery sender;
    /**
     * 接收者
     */
    private ChatUserQuery receiver;
    /**
     * 消息正文，可承载文本、HTML 或 JSON，由业务消息类型约定结构。
     */
    private String content;
    /**
     * 服务器时间
     */
    private Long serverTime;
    /**
     * 客户端发送时间
     */
    private Long clientSendTime;
}
