package com.sparrow.chat.core;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.timeout.IdleStateEvent;
import java.util.List;

public class WebSocketServerProtocolSupportHandshake extends WebSocketServerProtocolHandler {

    // js 不支持ping pong 帧
    // https://stackoverflow.com/questions/10585355/sending-websocket-ping-pong-frame-from-browser
    /**
     * FIN: 1 bit，表示该帧是否为消息的最后一帧。1-是，0-否。
     * <p>
     * <p>
     * RSV1,RSV2,RSV3: 1 bit each，预留(3位)，扩展的预留标志。一般情况为0，除非协商的扩展定义为非零值。如果接收到非零值且不为协商扩展定义，接收端必须使连接失败。
     * <p>
     * <p>
     * Opcode: 4 bits，定义消息帧的操作类型，如果接收到一个未知Opcode，接收端必须使连接失败。
     * <p>
     * （0x0-延续帧，0x1-文本帧，0x2-二进制帧，0x8-关闭帧，0x9-PING帧，0xA-PONG帧
     * <p>
     * （在接收到PING帧时，终端必须发送一个PONG帧响应，除非它已经接收到关闭帧）
     * <p>
     * 0x3-0x7保留给未来的非控制帧，0xB-F保留给未来的控制帧）
     * <p>
     * <p>
     * Mask: 1 bit，表示该帧是否为隐藏的，即被加密保护的。1-是，0-否。Mask=1时，必须传一个Masking-key，用于解除隐藏（客户端发送消息给服务器端，Mask必须为1）。
     * <p>
     * Payload length: 7 bits, 7+16 bits, or 7+64 bits，有效载荷数据的长度（扩展数据长度+应用数据长度，扩展数据长度可以为0）。
     * <p>
     * <p>
     * Masking-key: 0 or 4 bytes，用于解除帧隐藏（加密）的key，Mask=1时不为空，Mask=0时不用传。
     * <p>
     * <p>
     * Payload data: (x+y) bytes，有效载荷数据包括扩展数据（x bytes）和应用数据（y bytes）。有效载荷数据是用户真正要传输的数据。
     *
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
