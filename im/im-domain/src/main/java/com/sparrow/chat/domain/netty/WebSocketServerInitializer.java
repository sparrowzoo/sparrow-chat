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

import com.sparrow.chat.domain.demo.OutOfMemoryHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.extensions.compression.WebSocketServerCompressionHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.timeout.IdleStateHandler;

public class WebSocketServerInitializer extends ChannelInitializer<SocketChannel> {

    private static final String WEBSOCKET_PATH = "/websocket";

    private final SslContext sslCtx;

    public WebSocketServerInitializer(SslContext sslCtx) {
        this.sslCtx = sslCtx;
    }

    @Override
    public void initChannel(SocketChannel ch) {
        ChannelPipeline pipeline = ch.pipeline();
        if (sslCtx != null) {
            pipeline.addLast(sslCtx.newHandler(ch.alloc()));
        }
        pipeline.addLast(new HttpServerCodec());
        //OR
        //pipeline.addLast("decoder", new HttpRequestDecoder())
        //pipeline.addLast("encoder", new HttpResponseEncoder())

        // ChunkedWriteHandler：向客户端发送大文件 如 html5文件
        //pipeline.addLast("http-chunked", new ChunkedWriteHandler());
        pipeline.addLast(new WebSocketServerCompressionHandler());
        pipeline.addLast(new HttpObjectAggregator(1024));
        pipeline.addLast(new CustomWebSocketFrameAggregator());

        // 升级http到websocket握手 处理ping、pong、close
        //handlerAdded
        //ctx.pipeline().addBefore(ctx.name(), WebSocketServerProtocolHandshakeHandler.class.getName(), new WebSocketServerProtocolHandshakeHandler(this.websocketPath, this.subprotocols, this.allowExtensions, this.maxFramePayloadLength, this.allowMaskMismatch, this.checkStartsWith));

        pipeline.addLast(new WebSocketServerProtocolSupportHandshake(WEBSOCKET_PATH, 65536 * 10));
        pipeline.addLast(new IdleStateHandler(10, 10, 10));
        //握手有先后顺序
        pipeline.addLast(new WebSocketIndexPageHandler(WEBSOCKET_PATH));

        pipeline.addLast(new OutOfMemoryHandler());
        //pipeline.addLast(new WebSocketFrameHandler());
    }
}
