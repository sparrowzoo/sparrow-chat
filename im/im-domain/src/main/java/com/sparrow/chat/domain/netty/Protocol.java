package com.sparrow.chat.domain.netty;

import com.sparrow.chat.protocol.ChatSession;
import com.sparrow.chat.protocol.ChatUser;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

import static com.sparrow.chat.protocol.constant.Chat.CHAT_TYPE_1_2_1;
import static com.sparrow.chat.protocol.constant.Chat.TEXT_MESSAGE;

public class Protocol {
    public Protocol() {
    }

    private byte version;
    private byte messageType;
    private byte chatType;
    private int sessionLength;
    private ChatUser sender;
    private ChatUser receiver;
    private ChatSession chatSession;
    private String content;
    private byte[] contentBytes;
    private Long clientSendTime;
    private Long serverTime = System.currentTimeMillis();

    /**
     * 解析用户信息
     * 协议struct:
     * 用户ID长度(1字节) + 用户ID([用户ID长度个字节]) + 用户类型(1字节)
     *
     * @param content
     * @return
     */
    public ChatUser parseUser(ByteBuf content) {
        int senderIdLength = content.readByte();
        //剩余字节为发送时间字符串的时间戮
        byte[] senderIdBytes = new byte[senderIdLength];
        content.readBytes(senderIdBytes);
        int senderType = content.readByte();
        return ChatUser.stringUserId(new String(senderIdBytes), senderType);
    }

    public Protocol(ByteBuf content) {
        this.version = content.readByte();
        this.chatType = content.readByte();
        this.messageType = content.readByte();
        this.sender = parseUser(content);
        if (this.chatType == CHAT_TYPE_1_2_1) {
           this.receiver=this.parseUser(content);
            this.chatSession = ChatSession.create1To1Session(this.sender, this.receiver);
        } else {
            this.sessionLength = content.readByte();
            byte[] sessionBytes = new byte[sessionLength];
            content.readBytes(sessionBytes);
            String sessionKey = new String(sessionBytes);
            this.chatSession = ChatSession.createQunSession(this.sender, sessionKey);
        }
        int contentLength = content.readInt();
        byte[] contentBytes = new byte[contentLength];
        content.readBytes(contentBytes);
        if (this.messageType == TEXT_MESSAGE) {
            this.content = new String(contentBytes);
        } else {
            //图片等文件内容
            this.contentBytes = contentBytes;
        }
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isOne2One() {
        return this.chatType == CHAT_TYPE_1_2_1;
    }

    public boolean isText() {
        return this.messageType == TEXT_MESSAGE;
    }

    public int getChatType() {
        return chatType;
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

    public void setServerTime(Long serverTime) {
        this.serverTime = serverTime;
    }

    public ChatUser getSender() {
        return sender;
    }

    public void setSender(ChatUser sender) {
        this.sender = sender;
    }

    public ChatUser getReceiver() {
        return receiver;
    }

    public void setReceiver(ChatUser receiver) {
        this.receiver = receiver;
    }

    public byte getVersion() {
        return version;
    }

    public void setVersion(byte version) {
        this.version = version;
    }

    public void setChatType(byte chatType) {
        this.chatType = chatType;
    }

    public byte[] getContentBytes() {
        return contentBytes;
    }

    public void setContentBytes(byte[] contentBytes) {
        this.contentBytes = contentBytes;
    }

    public ByteBuf encode(ByteBufAllocator allocator,int capacity) {
        this.serverTime = System.currentTimeMillis();
        capacity = capacity + this.serverTime.toString().getBytes().length + 1;
        ByteBuf byteBuf = allocator.directBuffer(capacity);
        byteBuf.writeByte(this.version);
        byteBuf.writeByte(this.chatType);
        byteBuf.writeByte(this.messageType);
        byteBuf.writeByte(this.sender.toBytes().length);
        if (this.chatType == CHAT_TYPE_1_2_1) {
            byteBuf.writeBytes(this.receiver.toBytes());
        } else {
            byte[] sessionBytes = this.chatSession.getSessionKey().getBytes();
            byteBuf.writeByte(sessionBytes.length);
            byteBuf.writeBytes(sessionBytes);
        }
        byte[] contentBytes = this.content.getBytes();
        byteBuf.writeInt(contentBytes.length);
        byteBuf.writeBytes(contentBytes);
        byteBuf.writeBytes(String.valueOf(System.currentTimeMillis()).getBytes());
        return byteBuf;
    }
}
