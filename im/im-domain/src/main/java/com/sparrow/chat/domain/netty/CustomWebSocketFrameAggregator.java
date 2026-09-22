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

package com.sparrow.chat.domain.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrameAggregator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomWebSocketFrameAggregator extends WebSocketFrameAggregator {
    private static Logger logger = LoggerFactory.getLogger(CustomWebSocketFrameAggregator.class);
    private static final int DEFAULT_MAX_FRAME_SIZE = 1024 * 1024 * 3;

    public CustomWebSocketFrameAggregator() {
        super(DEFAULT_MAX_FRAME_SIZE);
    }

    public CustomWebSocketFrameAggregator(int maxFrameSize) {
        super(maxFrameSize);
    }

    @Override
    protected void handleOversizedMessage(ChannelHandlerContext ctx, WebSocketFrame oversized) throws Exception {
        logger.error("upload file over sized :{}", oversized);
    }

}
