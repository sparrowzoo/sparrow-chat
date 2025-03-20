package com.sparrow.chat.domain.bo;

import com.sparrow.chat.protocol.constant.Chat;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import lombok.Data;

import java.nio.charset.StandardCharsets;

@Data
public class CancelProtocol {
    public CancelProtocol(String sessionKey, Long clientSendTime) {
        this.sessionKey = sessionKey;
        this.clientSendTime = clientSendTime;
    }

    private String sessionKey;
    private Long clientSendTime;

    public ByteBuf toBytes() {
        ByteBuf byteBuf = ByteBufAllocator.DEFAULT.directBuffer(32, 256);
        byte[] clientTimeBytes = clientSendTime.toString().getBytes(StandardCharsets.UTF_8);
        byte[] sessionKeyBytes = this.sessionKey.getBytes(StandardCharsets.UTF_8);
        byteBuf.writeByte(Chat.CHAT_TYPE_CANCEL);//取消
        byteBuf.writeInt(sessionKeyBytes.length);
        byteBuf.writeBytes(sessionKeyBytes);
        byteBuf.writeInt(clientTimeBytes.length);
        byteBuf.writeBytes(clientTimeBytes);
        return byteBuf;
    }
}
