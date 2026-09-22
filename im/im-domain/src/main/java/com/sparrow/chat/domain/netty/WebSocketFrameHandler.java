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

/*
 * Copyright 2012 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package com.sparrow.chat.domain.netty;

import com.alibaba.fastjson.JSON;
import com.sparrow.authenticator.DefaultLoginUser;
import com.sparrow.chat.domain.bo.ChatUser;
import com.sparrow.chat.domain.bo.Protocol;
import com.sparrow.chat.domain.service.ChatService;
import com.sparrow.chat.protocol.constant.Chat;
import com.sparrow.chat.protocol.dto.ContactStatusDTO;
import com.sparrow.chat.protocol.query.ChatUserQuery;
import com.sparrow.context.SessionContext;
import com.sparrow.core.spi.ApplicationContext;
import com.sparrow.protocol.Result;
import com.sparrow.protocol.constant.SparrowError;
import com.sparrow.support.IpSupport;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.ByteBufHolder;
import io.netty.channel.*;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.ContinuationWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.List;

@Slf4j
public class WebSocketFrameHandler extends SimpleChannelInboundHandler<WebSocketFrame> {

    /**
     * 一定要重写channelRead0方法，否则会报错,内存泄漏问题交由netty处理
     *
     * @param ctx   the {@link ChannelHandlerContext} which this {@link SimpleChannelInboundHandler}
     *              belongs to
     * @param frame the message to handle
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, WebSocketFrame frame) throws Exception {

        ChatService chatService = ApplicationContext.getContainer().getBean("chatService");

        // ping and pong frames already handled js 不支持PingFrame 需要手动处理
        if (frame instanceof TextWebSocketFrame) {
            TextWebSocketFrame text = (TextWebSocketFrame) frame;
            ByteBuf byteBuf = text.content();
            log.info("ping pong content address hashcode {},capacity {}", byteBuf.hashCode(), byteBuf.capacity());
            // Send the uppercase string back.
            String content = ((TextWebSocketFrame) frame).text();
            if (Instruction.PING.equalsIgnoreCase(content)) {
                ctx.channel().writeAndFlush(new TextWebSocketFrame(Instruction.PONG));
                return;
            }
            if (content.startsWith(Instruction.USER_STATUS)) {
                long lastMonitorTime = UserContainer.getContainer().getLastMonitorStatusTime(ctx.channel());
                if (System.currentTimeMillis() - lastMonitorTime < 10000) {
                    Result result = Result.fail(SparrowError.GLOBAL_REQUEST_REPEAT);
                    result.setInstruction(Instruction.USER_STATUS);
                    ctx.channel().writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(result)));
                    return;
                }
                String contacts = content.substring(Instruction.USER_STATUS.length());
                List<ChatUserQuery> userQueries = JSON.parseArray(contacts, ChatUserQuery.class);
                Result<List<ContactStatusDTO>> result;
                try {
                    ChatUser currentUser = UserContainer.getContainer().hasUser(ctx.channel());
                    List<ContactStatusDTO> contactStatus = chatService.getContactsStatus(currentUser.getLongUserId(), userQueries);
                    result = Result.success(contactStatus, Instruction.USER_STATUS);
                } catch (Exception e) {
                    result = Result.fail(SparrowError.SYSTEM_SERVER_ERROR);
                    result.setInstruction(Instruction.USER_STATUS);
                }
                ctx.channel().writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(result))).addListener(new ChannelFutureListener() {
                    @Override
                    public void operationComplete(ChannelFuture future) {
                        UserContainer.getContainer().refreshLastMonitorStatusTime(ctx.channel());
                    }
                });
            }
            return;
        }

        if (frame instanceof BinaryWebSocketFrame) {

            UserContainer.getContainer().refreshLastActiveTime(ctx.channel());
            BinaryWebSocketFrame msg = (BinaryWebSocketFrame) frame;
            ByteBuf content = msg.content();
            Protocol protocol = new Protocol(content);
            ChatUser currentUser = UserContainer.getContainer().hasUser(ctx.channel());
            DefaultLoginUser loginUser = new DefaultLoginUser();
            loginUser.setUserId(currentUser.getLongUserId());
            loginUser.setCategory(currentUser.getCategory());
            SessionContext.bindLoginUser(loginUser);
            //从当前channel 中获取当前用户id
            protocol.setSender(currentUser);
            InetSocketAddress remoteAddress = (InetSocketAddress) ctx.channel().remoteAddress();
            String clientIP = remoteAddress.getAddress().getHostAddress();
            IpSupport ipSupport = ApplicationContext.getContainer().getBean(IpSupport.class);
            chatService.saveMessage(protocol, ipSupport.toLong(clientIP));
            if (protocol.getChatType() == Chat.CHAT_TYPE_1_2_1 && protocol.getSender().equals(protocol.getReceiver())) {
                return;
            }
            List<Channel> channels = UserContainer.getContainer().getChannels(protocol.getChatSession(), currentUser);
            this.writeAndFlush(ctx, protocol.getChatType(), msg, channels);
            return;
        }
        if (frame instanceof ContinuationWebSocketFrame) {
            ContinuationWebSocketFrame msg = (ContinuationWebSocketFrame) frame;
        }
    }

    private BinaryWebSocketFrame newFrame(
            BinaryWebSocketFrame msg) {
        ByteBuf byteBuf = null;
        try {
            byte[] serviceTimeBytes = ("_" + System.currentTimeMillis()).getBytes();
            int capacity = msg.content().readableBytes() + serviceTimeBytes.length;

            /**
             * .array()可能会报空指针异常
             */
            //byte [] bytes=msg.content().array();
            //log.info("msg length {}",bytes.length);
            byteBuf = ByteBufAllocator.DEFAULT.directBuffer(capacity);
            //byteBuf.writeBytes(bytes);
            byteBuf.writeBytes(msg.content());
            //服务器时间戮
            byteBuf.writeBytes(serviceTimeBytes);

            /**
             *  public ByteBuf writeBytes(ByteBuf src, int length) {
             *         if (checkBounds) {
             *             checkReadableBounds(src, length);
             *         }
             *         writeBytes(src, src.readerIndex(), length);
             *         src.readerIndex(src.readerIndex() + length);
             *         return this;
             *     }
             */
            msg.content().resetReaderIndex();
            return new BinaryWebSocketFrame(byteBuf);
        } catch (Exception e) {
            if (byteBuf != null) {
                ReferenceCountUtil.release(byteBuf);
            }
            throw e;
        }
    }

    private static Object retainedDuplicate(Object message) {
        if (message instanceof ByteBuf) {
            return ((ByteBuf) message).retainedDuplicate();
        } else {
            return message instanceof ByteBufHolder ? ((ByteBufHolder) message).retainedDuplicate() : ReferenceCountUtil.retain(message);
        }
    }

    private void writeAndFlush(ChannelHandlerContext ctx, Integer chatType, BinaryWebSocketFrame msg,
                               List<Channel> channels) throws InterruptedException {
//分组发送 或者自定义发送 release 会报错
//        ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
//        channelGroup.addAll(channels);
//        channelGroup.writeAndFlush(msg);

        for (Channel channel : channels) {
            if (channel == null || !channel.isOpen() || !channel.isActive()) {
                if (chatType == Chat.CHAT_TYPE_1_2_1) {
                    ctx.channel().writeAndFlush(new TextWebSocketFrame(Instruction.OFFLINE));
                }
                continue;
            }

            BinaryWebSocketFrame webSocketFrame = null;//this.newFrame(msg);
            log.info("init binary frame refCnt 这里会内存泄露" + webSocketFrame.refCnt());

            //对比使用 bad case
            webSocketFrame = (BinaryWebSocketFrame) retainedDuplicate(msg);
            log.info("init safe binary frame refCnt {}", webSocketFrame.refCnt());
            log.info("write before refCnt {}", msg.refCnt());
            log.info("write channel {}", channel);
            ChannelPromise promise = channel.newPromise();
            promise.addListener(new LoggingListener());
            channel.writeAndFlush(webSocketFrame, promise);
            log.info("after write flush refCnt {}", webSocketFrame.refCnt());
        }
        log.info("write before refCnt {}", msg.refCnt());

        //谁创建谁释放，谁最后使用谁释放
        //ReferenceCountUtil.release(unsafe);
        //log.info("after release refCnt {}", unsafe.refCnt());
        //不需要手动release
        //ReferenceCountUtil.release(msg);
    }

    //todo ACK 机制
    private static class LoggingListener implements ChannelFutureListener {
        public void operationComplete(ChannelFuture future) {
            log.info("Write operation complete {}", future.channel());
        }
    }
}
