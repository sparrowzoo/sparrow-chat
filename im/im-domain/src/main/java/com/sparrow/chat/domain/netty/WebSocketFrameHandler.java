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

import com.sparrow.chat.domain.service.ChatService;
import com.sparrow.chat.protocol.constant.Chat;
import com.sparrow.core.spi.ApplicationContext;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.ByteBufHolder;
import io.netty.channel.*;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.ContinuationWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Echoes uppercase content of text frames.
 */
public class WebSocketFrameHandler extends SimpleChannelInboundHandler<WebSocketFrame> {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketFrameHandler.class);

    /**
     *  一定要重写channelRead0方法，否则会报错,内存泄漏问题交由netty处理
     * @param ctx           the {@link ChannelHandlerContext} which this {@link SimpleChannelInboundHandler}
     *                      belongs to
     * @param frame           the message to handle
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, WebSocketFrame frame) throws Exception {
        // ping and pong frames already handled js 不支持PingFrame 需要手动处理
        if (frame instanceof TextWebSocketFrame) {
            TextWebSocketFrame text=(TextWebSocketFrame)frame;
            ByteBuf byteBuf = text.content();
            logger.info("ping pong content address hashcode {},capacity {}",byteBuf.hashCode(),byteBuf.capacity());
            // Send the uppercase string back.
            String content = ((TextWebSocketFrame) frame).text();
            if ("ping".equalsIgnoreCase(content)) {
                frame.retain();
                ctx.channel().writeAndFlush(new TextWebSocketFrame(Chat.RESPONSE_TEXT_PONG));
            }
        } else if (frame instanceof BinaryWebSocketFrame) {
            BinaryWebSocketFrame msg = (BinaryWebSocketFrame) frame;
            ByteBuf content = msg.content();
            Protocol protocol = new Protocol(content);
            Integer currentUserId = UserContainer.getContainer().hasUser(ctx.channel());
            if (protocol.getSender() != currentUserId) {
                logger.error("user id is not allow {}", currentUserId);
                return;
            }
            //从发前channel 中获取当前用户id
            protocol.setSender(currentUserId);
            ChatService chatService = ApplicationContext.getContainer().getBean("chatService");
            chatService.saveMessage(protocol);
            if (protocol.getCharType() == Chat.CHAT_TYPE_1_2_1 && protocol.getSender() == protocol.getReceiver()) {
                return;
            }
            List<Channel> channels = UserContainer.getContainer().getChannels(protocol.getChatSession(), currentUserId);
            this.writeAndFlush(ctx, protocol.getCharType(), msg, channels);
        } else if (frame instanceof ContinuationWebSocketFrame) {
            ContinuationWebSocketFrame msg = (ContinuationWebSocketFrame) frame;
        }
    }

    private BinaryWebSocketFrame unsafeDuplicate(
        BinaryWebSocketFrame msg) {
        byte[] serviceTimeBytes = ("_" + System.currentTimeMillis()).getBytes();
        int capacity = msg.content().readableBytes() + serviceTimeBytes.length;

        byte [] bytes=msg.content().array();
        logger.info("msg length {}",bytes.length);
        ByteBuf byteBuf = ByteBufAllocator.DEFAULT.directBuffer(capacity);
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
    }

    @Deprecated
    private BinaryWebSocketFrame unsafeDuplicateDeprecated(BinaryWebSocketFrame msg) {
        ByteBuf byteBuf = ByteBufAllocator.DEFAULT.directBuffer(msg.content().capacity());
        ;
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

    private void writeAndFlush(ChannelHandlerContext ctx, Integer chatType, BinaryWebSocketFrame msg,
        List<Channel> channels) throws InterruptedException {
//分组发送 或者自定义发送 release 会报错
//        ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
//        channelGroup.addAll(channels);
//        channelGroup.writeAndFlush(msg);

        for (Channel channel : channels) {
            if (channel == null || !channel.isOpen() || !channel.isActive()) {
                if (chatType == Chat.CHAT_TYPE_1_2_1) {
//                    ByteBuf offline = Unpooled.directBuffer(1);
//                    offline.writeByte(0);
                    ctx.channel().writeAndFlush(new TextWebSocketFrame(Chat.RESPONSE_TEXT_OFFLINE));
                }
                continue;
            }
            BinaryWebSocketFrame unsafe = this.unsafeDuplicate(msg);
            //对比使用 bad case
            //BinaryWebSocketFrame safe=(BinaryWebSocketFrame) safeDuplicate(msg);
            logger.info("write channel {}", channel);
            ChannelPromise promise = channel.newPromise();
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
