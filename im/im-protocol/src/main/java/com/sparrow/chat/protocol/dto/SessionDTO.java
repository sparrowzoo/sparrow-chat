package com.sparrow.chat.protocol.dto;

import com.sparrow.protocol.VO;
import lombok.Data;

@Data
public class SessionDTO implements VO {

    public static SessionDTO parse(String sessionKey,Long lastReadTime) {
        String chatType = sessionKey.substring(0, 1);
        String id = sessionKey.substring(1);
        return new SessionDTO(Integer.parseInt(chatType), id,lastReadTime);
    }

    public SessionDTO(int chatType, String id) {
        this(chatType, id, 0L);
    }

    public SessionDTO(int chatType, String id, Long lastReadTime) {
        this.chatType = chatType;
        this.id = id;
        this.lastReadTime = lastReadTime;
    }

    private int chatType;
    private String id;
    private Long lastReadTime = 0L;
}
