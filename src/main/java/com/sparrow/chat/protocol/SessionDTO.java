package com.sparrow.chat.protocol;

import java.util.List;

public class SessionDTO {
    private ChatSession chatSession;
    private List<MessageDTO> messages;
    private Long lastReadTime;


    public SessionDTO(ChatSession chatSession, List<MessageDTO> messages) {
        this.chatSession = chatSession;
        this.messages = messages;
        this.lastReadTime = lastReadTime;
    }

    public ChatSession getChatSession() {
        return chatSession;
    }

    public void setChatSession(ChatSession chatSession) {
        this.chatSession = chatSession;
    }

    public List<MessageDTO> getMessages() {
        return messages;
    }

    public void setMessages(List<MessageDTO> messages) {
        this.messages = messages;
    }

    public Long getLastReadTime() {
        return lastReadTime;
    }

    public void setLastReadTime(Long lastReadTime) {
        this.lastReadTime = lastReadTime;
    }
}
