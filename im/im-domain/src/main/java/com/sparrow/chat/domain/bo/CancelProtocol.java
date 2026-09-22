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

package com.sparrow.chat.domain.bo;

import com.sparrow.chat.protocol.constant.Chat;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import lombok.Data;

import java.nio.charset.StandardCharsets;

@Data
public class CancelProtocol {
    public CancelProtocol(String sessionKey, Long clientSendTime) {
        this.sessionKey = sessionKey;
        this.clientSendTime = clientSendTime;
    }

    private String sessionKey;
    private Long clientSendTime;

    public ByteBuf toBytes() {
        ByteBuf byteBuf = ByteBufAllocator.DEFAULT.directBuffer(32, 256);
        byte[] clientTimeBytes = clientSendTime.toString().getBytes(StandardCharsets.UTF_8);
        byte[] sessionKeyBytes = this.sessionKey.getBytes(StandardCharsets.UTF_8);
        byteBuf.writeByte(Chat.CHAT_TYPE_CANCEL);//取消
        byteBuf.writeInt(sessionKeyBytes.length);
        byteBuf.writeBytes(sessionKeyBytes);
        byteBuf.writeInt(clientTimeBytes.length);
        byteBuf.writeBytes(clientTimeBytes);
        return byteBuf;
    }
}
