package com.sparrow.chat.core;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.timeout.IdleStateEvent;
import java.util.List;

public class WebSocketServerProtocolSupportHandshake extends WebSocketServerProtocolHandler {

    /**
     * @param websocketPath
     * @param maxLength
     */
    public WebSocketServerProtocolSupportHandshake(String websocketPath, Integer maxLength) {
        super(websocketPath, "*", true, maxLength);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof HandshakeComplete) {
            HandshakeComplete serverHandshakeComplete = (HandshakeComplete) evt;
            String userId = serverHandshakeComplete.requestHeaders().get("sec-websocket-protocol");
            System.out.println(serverHandshakeComplete.requestHeaders().get("sec-websocket-protocol"));
            UserContainer.getContainer().online(ctx.channel(), userId);
        } else {
            if (evt instanceof IdleStateEvent) {
                UserContainer.getContainer().offline(ctx.channel());
            }
            super.userEventTriggered(ctx, evt);
        }
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, WebSocketFrame frame, List<Object> out)
        throws Exception {
        if (frame instanceof CloseWebSocketFrame) {
            UserContainer.getContainer().offline(ctx.channel());
        }
        super.decode(ctx, frame, out);
    }
}
