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
 * Copyright 2014 The Netty Project
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
package com.sparrow.chat.domain.demo;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.handler.codec.http.websocketx.extensions.compression.WebSocketClientCompressionHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import io.netty.util.concurrent.DefaultThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;

/**
 * This is an example of a WebSocket client.
 * <p>
 * In order to run this example you need a compatible WebSocket server.
 * Therefore you can either start the WebSocket server from the examples
 * by running {@link io.netty.example.http.websocketx.server.WebSocketServer}
 * or connect to an existing WebSocket server such as
 * <a href="http://www.websocket.org/echo.html">ws://echo.websocket.org</a>.
 * <p>
 * The client will attempt to connect to the URI passed to it as the first argument.
 * You don't have to specify any arguments if you want to connect to the example WebSocket server,
 * as this is the default.
 */
public final class WebSocketClient {

    private static final Logger log = LoggerFactory.getLogger(WebSocketClient.class);

    static final String URL = System.getProperty("url", "ws://127.0.0.1:8080/websocket");

    static PooledByteBufAllocator allocator = new PooledByteBufAllocator(true);

    public static void main(String[] args) throws Exception {
        System.setProperty("io.netty.eventLoopThreads", 64 + "");
        URI uri = new URI(URL);
        String scheme = uri.getScheme() == null ? "ws" : uri.getScheme();
        final String host = uri.getHost() == null ? "127.0.0.1" : uri.getHost();
        final int port;
        if (uri.getPort() == -1) {
            if ("ws".equalsIgnoreCase(scheme)) {
                port = 80;
            } else if ("wss".equalsIgnoreCase(scheme)) {
                port = 443;
            } else {
                port = -1;
            }
        } else {
            port = uri.getPort();
        }

        if (!"ws".equalsIgnoreCase(scheme) && !"wss".equalsIgnoreCase(scheme)) {
            System.err.println("Only WS(S) is supported.");
            return;
        }

        final boolean ssl = "wss".equalsIgnoreCase(scheme);
        final SslContext sslCtx;
        if (ssl) {
            sslCtx = SslContextBuilder.forClient()
                    .trustManager(InsecureTrustManagerFactory.INSTANCE).build();
        } else {
            sslCtx = null;
        }
        EventLoopGroup workder = new NioEventLoopGroup(2, new DefaultThreadFactory("websocket-workder", true));
        try {
            // Connect with V13 (RFC 6455 aka HyBi-17). You can change it to V08 or V00.
            // If you change it to V00, ping is not supported and remember to change
            // HttpResponseDecoder to WebSocketHttpResponseDecoder in the pipeline.

            //https://tools.ietf.org/html/rfc6455
            //https://tools.ietf.org/html/rfc6455#section-11.6  各版本说明


            HttpHeaders httpHeaders = new DefaultHttpHeaders();
            httpHeaders.add("user-name", "zhangsan");
            final WebSocketClientHandler handler =
                    new WebSocketClientHandler(
                            WebSocketClientHandshakerFactory.newHandshaker(
                                    uri, WebSocketVersion.V13, null, true, httpHeaders));

            Bootstrap b = new Bootstrap();
            b.option(ChannelOption.ALLOCATOR, allocator);
            b.group(workder)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() { //处理当前channel的handler
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            ChannelPipeline p = ch.pipeline();
                            if (sslCtx != null) {
                                p.addLast(sslCtx.newHandler(ch.alloc(), host, port));
                            }
                            p.addLast(
                                    new HttpClientCodec(),
                                    new HttpObjectAggregator(8192),
                                    WebSocketClientCompressionHandler.INSTANCE,
                                    handler);
                        }
                    });

            //NioEventLoop.select(boolean oldWakenUp)
            Channel ch = b.connect(uri.getHost(), port).sync().channel();

            handler.handshakeFuture().sync();

            BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
            while (true) {
                String msg = console.readLine();
                if (msg.equalsIgnoreCase("\n")) {
                    continue;
                }
                if ("bye".equals(msg.toLowerCase())) {
                    ch.writeAndFlush(new CloseWebSocketFrame());
                    ch.closeFuture().sync();
                    break;
                } else if ("ping".equals(msg.toLowerCase())) {
                    WebSocketFrame frame = new PingWebSocketFrame(Unpooled.wrappedBuffer(new byte[]{8, 1, 8, 1}));
                    ch.writeAndFlush(frame);
                } else if ("while".equals(msg.toLowerCase())) {
                    while (true) {
                        //Thread.sleep(10);//如果不sleep 会怎样？OOM?
                        ByteBuf byteBuf = allocator.directBuffer(1024);
                        byteBuf.writeBytes("这里是北京这里是北京这里是北京这里是北京这里是北京这里是北京这里是北京这里是北京这里是北京这里是北京这里是北京这里是北京这里是北京这里是北京这里是北京这里是北京这里是北京这里是北京这里是北京这里是北京这里是北京这里是北京这里是北京这里是北京这里是北京这里是北京这里是北京这里是北京这里是北京这里是北京这里是北京这里是北京这里是北京这里是北京这里是北京这里是北京这里是北京这里是北京这里是北京这里是北京这里是北京这里是北京这里是北京这里是北京这里是北京这里是北京这里是北京这里是北京这里是北京这里是北京".getBytes());
                        BinaryWebSocketFrame binaryWebSocketFrame = new BinaryWebSocketFrame(byteBuf);
                        ch.writeAndFlush(binaryWebSocketFrame);
                                //.sync();
                        log.info("{}send ok!{}", Thread.currentThread().getName(), System.currentTimeMillis());
//                                .addListener(future -> {
//                            try {
//                                if (!future.isSuccess()) {
//                                    if (binaryWebSocketFrame.refCnt() > 0) { // 检查引用计数
//                                        binaryWebSocketFrame.release();
//                                    }
//                                }
//                            } finally {
//                                //ReferenceCountUtil.safeRelease(binaryWebSocketFrame); // 最终安全释放
//                                //ReferenceCountUtil.release(binaryWebSocketFrame); // 会抛异常，这里不需要手动释放HeadHandler在成功写入时会自动释放
//                            }
//                        });
                    }
                } else {
                    ByteBuf byteBuf = allocator.directBuffer(1024);
                    byteBuf.writeBytes(msg.getBytes());
                    BinaryWebSocketFrame binaryWebSocketFrame = new BinaryWebSocketFrame(byteBuf);
                    ch.writeAndFlush(binaryWebSocketFrame).sync();
                }
            }
        } finally {
            workder.shutdownGracefully();
        }
    }
}
