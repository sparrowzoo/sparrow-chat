package com.sparrow.chat.protocol;

import com.sparrow.cryptogram.Base64;
import io.netty.buffer.ByteBuf;

import static com.sparrow.chat.commons.Chat.CHAT_TYPE_1_2_1;
import static com.sparrow.chat.commons.Chat.TEXT_MESSAGE;

public class Protocol {
    public Protocol() {
    }
    private byte messageType;
    private byte charType;
    private int sessionLength;
    private int fromUserId;
    private int targetUserId;
    private ChatSession chatSession;
    private int contentLength;
    private String content;
    private Long clientSendTime;
    private Long serverTime =System.currentTimeMillis();


    public Protocol(ByteBuf content) {
        this.charType = content.readByte();
        this.messageType = content.readByte();
        this.fromUserId = content.readInt();
        if (this.charType == CHAT_TYPE_1_2_1) {
            this.targetUserId = content.readInt();
            this.chatSession = ChatSession.create1To1Session(this.fromUserId, this.targetUserId);
        } else {
            this.sessionLength = content.readInt();
            byte[] sessionBytes = new byte[sessionLength];
            content.readBytes(sessionBytes);
            String sessionKey=new String(sessionBytes);
            this.chatSession = ChatSession.createQunSession(this.fromUserId,sessionKey);
        }
        this.contentLength = content.readInt();
        byte[] contentBytes = new byte[contentLength];
        content.readBytes(contentBytes);
        this.content = Base64.encodeBytes(contentBytes);
        //剩余字节为发送时间字符串的时间戮
        byte[] sendTimeBytes = new byte[content.readableBytes()];
        content.readBytes(sendTimeBytes);
        this.clientSendTime = Long.parseLong(new String(sendTimeBytes));
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

    public ChatSession getChatSession() {
        return chatSession;
    }

    public void setChatSession(ChatSession chatSession) {
        this.chatSession = chatSession;
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

    public Long getServerTime() {
        return serverTime;
    }

    public Long getClientSendTime() {
        return clientSendTime;
    }

    public void setClientSendTime(Long clientSendTime) {
        this.clientSendTime = clientSendTime;
    }

    public void setFromUserId(int fromUserId) {
        this.fromUserId = fromUserId;
    }

    public void setTargetUserId(int targetUserId) {
        this.targetUserId = targetUserId;
    }

    public void setServerTime(Long serverTime) {
        this.serverTime = serverTime;
    }
}
