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

import com.sparrow.authenticator.DefaultLoginUser;
import com.sparrow.chat.domain.repository.QunRepository;
import com.sparrow.chat.domain.service.subscriber.IMSubscriber;
import com.sparrow.context.SessionContext;
import com.sparrow.messaging.domain.service.ClientConnection;
import com.sparrow.messaging.domain.service.MultiDeviceConnectionRegistry;
import com.sparrow.messaging.protocol.ChatSession;
import com.sparrow.messaging.protocol.ChatUser;
import com.sparrow.messaging.protocol.Protocol;
import com.sparrow.messaging.protocol.enums.ChatType;
import com.sparrow.messaging.protocol.enums.DefaultProtocolType;
import com.sparrow.protocol.LoginUser;
import com.sparrow.protocol.enums.DeviceType;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Proxy;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.junit.Assert.*;

public class IMSubscriberTest {
    private final IMSubscriber subscriber = new IMSubscriber();
    private final MultiDeviceConnectionRegistry registry = new MultiDeviceConnectionRegistry();
    private final List<Protocol> saved = new ArrayList<>();
    private final List<Long> members = List.of(1L, 2L, 3L, 4L);
    private boolean failSave;

    @Before
    public void setUp() throws Exception {
        inject("connectionRegistry", registry);
        // The real persistence requires Redis and a database; capture its boundary here.
        inject("chatService", new ChatService() {
            @Override
            public void saveMessage(Protocol protocol) {
                if (failSave) {
                    throw new IllegalStateException("storage unavailable");
                }
                saved.add(protocol.clone());
            }
        });
        inject("qunRepository", Proxy.newProxyInstance(QunRepository.class.getClassLoader(),
                new Class<?>[]{QunRepository.class}, (proxy, method, args) -> {
                    if (method.getName().equals("getUserIdList")) {
                        assertEquals("99", args[0]);
                        return members;
                    }
                    throw new UnsupportedOperationException(method.getName());
                }));
    }

    @Test
    public void forwardsToAllReceiverDevicesAfterSaving() throws Throwable {
        List<Protocol> sender = connect(1, DeviceType.PC, false);
        List<Protocol> desktop = connect(2, DeviceType.PC, false);
        List<Protocol> mobile = connect(2, DeviceType.MOBILE, false);
        Protocol request = message(2);
        subscriber.subscribe(request);
        assertEquals(1, saved.size());
        assertTrue(sender.isEmpty());
        for (List<Protocol> frames : List.of(desktop, mobile)) {
            assertEquals(1, frames.size());
            Protocol frame = frames.getFirst();
            assertEquals("hello", frame.getPayload());
            assertEquals("1", frame.getSender().getId());
            assertEquals("2", frame.getReceiver().getId());
            assertEquals(DefaultProtocolType.IM, frame.getProtocolType());
            assertEquals(Long.valueOf(123), frame.getClientSendTime());
            assertNotNull(frame.getServerTime());
            assertEquals(saved.getFirst().getServerTime(), frame.getServerTime());
        }
    }

    @Test
    public void savesSelfMessageWithoutForwarding() throws Throwable {
        List<Protocol> sender = connect(1, DeviceType.PC, false);
        subscriber.subscribe(message(1));
        assertEquals(1, saved.size());
        assertTrue(sender.isEmpty());
    }

    @Test
    public void notifiesSenderWhenReceiverIsOffline() throws Throwable {
        List<Protocol> sender = connect(1, DeviceType.PC, false);
        subscriber.subscribe(message(2));
        assertEquals(1, saved.size());
        assertEquals(1, sender.size());
        assertEquals(DefaultProtocolType.OFFLINE, sender.getFirst().getProtocolType());
    }

    @Test
    public void forwardsGroupMessageExcludingSenderAndOfflineMembers() throws Throwable {
        List<Protocol> sender = connect(1, DeviceType.PC, false);
        List<Protocol> second = connect(2, DeviceType.PC, false);
        List<Protocol> third = connect(3, DeviceType.MOBILE, false);
        Protocol request = message(2);
        request.setChatType(ChatType.GROUP);
        request.setReceiver(null);
        request.setChatSession(ChatSession.parse("199"));
        subscriber.subscribe(request);
        assertEquals(1, saved.size());
        assertTrue(sender.isEmpty());
        assertEquals(1, second.size());
        assertEquals(1, third.size());
        assertEquals("199", third.getFirst().getChatSession().key());
    }

    @Test
    public void failedGroupConnectionDoesNotStopOtherRecipients() throws Throwable {
        connect(1, DeviceType.PC, false);
        connect(2, DeviceType.PC, true);
        List<Protocol> third = connect(3, DeviceType.PC, false);
        Protocol request = message(2);
        request.setChatType(ChatType.GROUP);
        request.setChatSession(ChatSession.parse("199"));
        subscriber.subscribe(request);
        assertEquals(1, third.size());
    }

    @Test
    public void preservesThreadUserAndDoesNotForwardWhenSavingFails() throws Throwable {
        connect(1, DeviceType.PC, false);
        List<Protocol> receiver = connect(2, DeviceType.PC, false);
        DefaultLoginUser previous = new DefaultLoginUser();
        previous.setUserId(100L);
        SessionContext.bindLoginUser(previous);
        failSave = true;
        try {
            assertThrows(IllegalStateException.class, () -> {
                try {
                    subscriber.subscribe(message(2));
                } catch (Exception e) {
                    throw e;
                } catch (Throwable t) {
                    throw new AssertionError(t);
                }
            });
            assertSame(previous, SessionContext.getLoginUser());
            assertTrue(receiver.isEmpty());
        } finally {
            SessionContext.clearToken();
        }
    }

    private List<Protocol> connect(long id, DeviceType device, boolean fail) {
        DefaultLoginUser user = new DefaultLoginUser();
        user.setUserId(id);
        user.setCategory(LoginUser.CATEGORY_REGISTER);
        user.setDeviceType(device);
        List<Protocol> frames = new ArrayList<>();
        ClientConnection connection = (ClientConnection) Proxy.newProxyInstance(
                ClientConnection.class.getClassLoader(), new Class<?>[]{ClientConnection.class},
                (proxy, method, args) -> {
                    if (method.getName().equals("loginUser")) {
                        return user;
                    }
                    if (method.getName().equals("refreshAccessTime")) {
                        return null;
                    }
                    if (method.getName().equals("sendMessage")) {
                        assertFalse("message must be saved before forwarding", saved.isEmpty());
                        if (fail) {
                            return CompletableFuture.failedFuture(new IllegalStateException("closed"));
                        }
                        frames.add(new Protocol(((ByteBuffer) args[0]).duplicate()));
                        return CompletableFuture.completedFuture(null);
                    }
                    throw new UnsupportedOperationException(method.getName());
                });
        registry.register(connection);
        return frames;
    }

    private Protocol message(long receiver) {
        Protocol protocol = new Protocol();
        protocol.setProtocolType(DefaultProtocolType.IM);
        protocol.setChatType(ChatType.ONE_2_ONE);
        protocol.setDevice(DeviceType.PC);
        protocol.setSender(ChatUser.longUserId(1L, LoginUser.CATEGORY_REGISTER));
        protocol.setReceiver(ChatUser.longUserId(receiver, LoginUser.CATEGORY_REGISTER));
        protocol.setPayload("hello");
        protocol.setClientSendTime(123L);
        return protocol;
    }

    private void inject(String name, Object value) throws Exception {
        Field field = IMSubscriber.class.getDeclaredField(name);
        field.setAccessible(true);
        field.set(subscriber, value);
    }
}
