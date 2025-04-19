package com.sparrow.chat.protocol.dto;

import com.sparrow.chat.protocol.query.ChatUserQuery;
import com.sparrow.protocol.POJO;
import lombok.Data;

@Data
public class MessageDTO implements POJO {
    /**
     * 消息类型
     */
    private int messageType;
    /**
     * 发送者
     */
    private ChatUserQuery sender;
    /**
     * 接收者
     */
    private ChatUserQuery receiver;
    /**
     * 消息内容
     */
    private String content;
    /**
     * 服务器时间
     */
    private Long serverTime;
    /**
     * 客户端发送时间
     */
    private Long clientSendTime;
}
