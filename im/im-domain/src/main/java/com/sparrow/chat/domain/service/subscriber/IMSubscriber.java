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

package com.sparrow.chat.domain.service.subscriber;

import com.sparrow.chat.domain.repository.QunRepository;
import com.sparrow.chat.domain.service.ChatService;
import com.sparrow.chat.protocol.enums.IMMessageType;
import com.sparrow.messaging.domain.service.ClientConnection;
import com.sparrow.messaging.domain.service.ConnectionRegistry;
import com.sparrow.messaging.infrastructure.service.spring.AbstractSpringTypedSubscriber;
import com.sparrow.messaging.protocol.ChatUser;
import com.sparrow.messaging.protocol.MessageType;
import com.sparrow.messaging.protocol.Protocol;
import com.sparrow.messaging.protocol.ProtocolType;
import com.sparrow.messaging.protocol.enums.DefaultProtocolType;
import com.sparrow.protocol.LoginUser;
import com.sparrow.protocol.enums.DeviceType;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;

import java.nio.ByteBuffer;
import java.util.Map;

@Slf4j
@Named
public class IMSubscriber extends AbstractSpringTypedSubscriber<Protocol> {

    @Inject
    private ConnectionRegistry connectionRegistry;

    @Inject
    private ChatService chatService;

    @Inject
    private QunRepository qunRepository;

    @Override
    public void subscribe(Protocol protocol) throws Throwable {
        if (protocol == null || protocol.getSender() == null || protocol.getDevice() == null
                || protocol.getChatType() == null || protocol.getChatSession() == null
                || (protocol.isOne2One() && protocol.getReceiver() == null)) {
            throw new IllegalArgumentException("IM requires sender, device and a valid chat session");
        }
        ClientConnection senderConnection = connectionRegistry.findConnection(protocol.getSender(), protocol.getDevice());
        if (senderConnection == null) {
            log.debug("Skip IM Subscriber: sender connection is no longer registered");
            return;
        }
        senderConnection.refreshAccessTime();
        protocol.setServerTime(System.currentTimeMillis());
        try {
            chatService.saveMessage(protocol);
        } finally {
            //todo received msg
        }

        if (protocol.isOne2One() && protocol.getSender().equals(protocol.getReceiver())) {
            log.warn("self 2 self ignore,not push");
            return;
        }
        ByteBuffer encoded = protocol.encode().asReadOnlyBuffer();
        if (protocol.isOne2One()) {
            Map<DeviceType, ClientConnection> receivers = connectionRegistry.findConnections(protocol.getReceiver());
            if (receivers.isEmpty()) {
                sendMessage(senderConnection,protocol.offline().encode(), protocol.getSender(), protocol.getDevice());
                return;
            }
            sendMessage(protocol.getReceiver(), encoded, receivers);
            return;
        }
        for (Long userId : qunRepository.getUserIdList(protocol.getChatSession().getId())) {
            if (userId.equals(protocol.getSender().getLongUserId())) {
                continue;
            }
            ChatUser receiver = ChatUser.longUserId(userId, LoginUser.CATEGORY_REGISTER);
            sendMessage(receiver, encoded, connectionRegistry.findConnections(receiver));
        }
    }

    private void sendMessage(ChatUser receiver, ByteBuffer encoded,
                             Map<DeviceType, ClientConnection> connections) {
        connections.forEach((device, connection) -> sendMessage(connection, encoded, receiver, device));
    }

    private void sendMessage(ClientConnection connection, ByteBuffer encoded, ChatUser receiver, DeviceType device) {
        try {
            connection.sendMessage(encoded.asReadOnlyBuffer()).whenComplete((ignored, failure) -> {
                if (failure != null) {
                    log.warn("Failed to send IM message to {} on {}", receiver.key(), device, failure);
                }
            });
        } catch (Exception failure) {
            log.warn("Failed to send IM message to {} on {}", receiver.key(), device, failure);
        }
    }

    @Override
    public ProtocolType getProtocolType() {
        return DefaultProtocolType.IM;
    }

    @Override
    public MessageType getMessageType() {
        return IMMessageType.HTML;
    }

    @Override
    public Boolean localImmediately() {
        return false;
    }
}
