package com.sparrow.chat.protocol;

import com.sparrow.cryptogram.Base64;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;

import static com.sparrow.chat.commons.Chat.CHAT_TYPE_1_2_1;
import static com.sparrow.chat.commons.Chat.TEXT_MESSAGE;

public class Protocol {
    private byte messageType;
    private byte charType;
    private int sessionLength;
    private int fromUserId;
    private int targetUserId;
    private String session;
    private int contentLength;
    private String content;
    private Long sendTime = System.currentTimeMillis();


    public Protocol(ByteBuf content) {
        this.charType = content.readByte();
        this.messageType = content.readByte();
        this.fromUserId = content.readInt();
        if (this.charType == CHAT_TYPE_1_2_1) {
            this.targetUserId = content.readInt();
            this.session = ChatSession.create1To1Session(this.fromUserId, this.targetUserId).getSessionKey();
        } else {
            this.sessionLength = content.readInt();
            byte[] sessionBytes = new byte[sessionLength];
            content.readBytes(sessionBytes);
            this.session = new String(sessionBytes);
        }
        this.contentLength = content.readInt();
        byte[] contentBytes = new byte[contentLength];
        content.readBytes(contentBytes);
        this.content = Base64.encodeBytes(contentBytes);
        content.resetReaderIndex();
    }

    public int getMessageType() {
        return messageType;
    }

    public void setMessageType(byte messageType) {
        this.messageType = messageType;
    }

    public int getSessionLength() {
        return sessionLength;
    }

    public void setSessionLength(int sessionLength) {
        this.sessionLength = sessionLength;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public int getContentLength() {
        return contentLength;
    }

    public void setContentLength(int contentLength) {
        this.contentLength = contentLength;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isOne2One() {
        return this.charType == CHAT_TYPE_1_2_1;
    }

    public boolean isText() {
        return this.messageType == TEXT_MESSAGE;
    }

    public int getTargetUserId() {
        return this.targetUserId;
    }

    public int getCharType() {
        return charType;
    }

    public int getFromUserId() {
        return fromUserId;
    }

    public Long getSendTime() {
        return sendTime;
    }
}
