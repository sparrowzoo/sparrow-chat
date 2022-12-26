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
import com.sparrow.core.spi.ApplicationContext;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.ByteBufHolder;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.ContinuationWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.concurrent.GlobalEventExecutor;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Echoes uppercase content of text frames.
 */
public class WebSocketFrameHandler extends SimpleChannelInboundHandler<WebSocketFrame> {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketFrameHandler.class);

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
            ChatService chatService = ApplicationContext.getContainer().getBean("chatService");
            chatService.saveMessage(protocol);
            List<Channel> channels = UserContainer.getContainer().getChannels(protocol);
            for (Channel channel : channels) {
                if (channel == null || !channel.isOpen() || !channel.isActive()) {
                    if (protocol.getCharType() == Chat.CHAT_TYPE_1_2_1) {
                        ByteBuf offline = Unpooled.directBuffer(1);
                        offline.writeByte(0);
                        ctx.channel().writeAndFlush(new BinaryWebSocketFrame(offline));
                    }
                    continue;
                }
            }
            this.writeAndFlush(msg, channels);
        } else if (frame instanceof ContinuationWebSocketFrame) {
            ContinuationWebSocketFrame msg = (ContinuationWebSocketFrame) frame;
            System.out.println(
                "IsFinal" + msg.isFinalFragment() + " length" + msg.content()
                    .capacity());
        }
    }

    private BinaryWebSocketFrame unsafeDuplicate(BinaryWebSocketFrame msg) {
        ByteBuf byteBuf = ByteBufAllocator.DEFAULT.directBuffer(msg.content().capacity());
        byteBuf.writeBytes(msg.content());
        //必须重置
        msg.content().resetReaderIndex();
        return new BinaryWebSocketFrame(byteBuf);
    }

    private static Object safeDuplicate(Object message) {
        if (message instanceof ByteBuf) {
            return ((ByteBuf) message).retainedDuplicate();
        } else {
            return message instanceof ByteBufHolder ? ((ByteBufHolder) message).retainedDuplicate() : ReferenceCountUtil.retain(message);
        }
    }

    private void writeAndFlush(BinaryWebSocketFrame msg, List<Channel> channels) throws InterruptedException {
//分组发送 或者自定义发送 release 会报错
//        ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
//        channelGroup.addAll(channels);
//        channelGroup.writeAndFlush(msg);

        for (Channel channel : channels) {
            BinaryWebSocketFrame unsafe = this.unsafeDuplicate(msg);
            //对比使用 bad case
            //BinaryWebSocketFrame safe=(BinaryWebSocketFrame) safeDuplicate(msg);
            logger.info("write channel {}", channel);
            ChannelPromise promise = channel.voidPromise();
            promise.addListener(new LoggingListener());
            channel.writeAndFlush(unsafe, promise);
        }
        //不需要手动release
        //ReferenceCountUtil.release(msg);
    }

    private static class LoggingListener implements ChannelFutureListener {
        public void operationComplete(ChannelFuture future) {
            logger.info("Write operation complete {}", future.channel());
        }
    }
}
