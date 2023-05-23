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
