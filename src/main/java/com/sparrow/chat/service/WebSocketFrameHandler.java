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
package com.sparrow.chat.service;

import com.sparrow.chat.commons.Chat;
import com.sparrow.chat.core.UserContainer;
import com.sparrow.chat.protocol.Protocol;
import com.sparrow.spring.starter.SpringContext;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.ContinuationWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import java.util.List;
import java.util.Locale;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Echoes uppercase content of text frames.
 */
public class WebSocketFrameHandler extends SimpleChannelInboundHandler<WebSocketFrame> {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketFrameHandler.class);

    private static ChatService chatService = SpringContext.getContext().getBean("chatService", ChatService.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, WebSocketFrame frame) throws Exception {
        // ping and pong frames already handled
        if (frame instanceof TextWebSocketFrame) {
            // Send the uppercase string back.
            String request = ((TextWebSocketFrame) frame).text();
            System.out.println(request.length());
            System.out.println("received " + request);
            logger.info("{} received {}", ctx.channel(), request);
            ctx.channel().writeAndFlush(new TextWebSocketFrame(request.toUpperCase(Locale.US)));
        } else if (frame instanceof BinaryWebSocketFrame) {
            BinaryWebSocketFrame msg = (BinaryWebSocketFrame) frame;
            ByteBuf content = msg.content();
            Protocol protocol = new Protocol(content);
            chatService.saveMessage(protocol);
            List<Channel> channels = UserContainer.getContainer().getChannels(protocol);
            for (Channel channel : channels) {
                if (channel == null) {
                    if (protocol.getCharType() == Chat.CHAT_TYPE_1_2_1) {
                        ByteBuf offline = Unpooled.directBuffer(1);
                        offline.writeByte(0);
                        ctx.channel().writeAndFlush(new BinaryWebSocketFrame(offline));
                    }
                    continue;
                }
                ByteBuf byteBuf = ByteBufAllocator.DEFAULT.directBuffer(msg.content().capacity());
                byteBuf.writeBytes(msg.content());
                channel.writeAndFlush(new BinaryWebSocketFrame(byteBuf));
            }
        } else if (frame instanceof ContinuationWebSocketFrame) {
            ContinuationWebSocketFrame msg = (ContinuationWebSocketFrame) frame;
            System.out.println(
                "IsFinal" + msg.isFinalFragment() + " length" + msg.content()
                    .capacity());
        }
    }
}
