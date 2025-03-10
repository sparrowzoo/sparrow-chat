package com.sparrow.chat.protocol.query;

public class ChatUserQuery {
    public ChatUserQuery(String id, Integer category) {
        this.id = id;
        this.category = category;
    }

    private String id;
    private Integer category;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getCategory() {
        return category;
    }

    public void setCategory(Integer category) {
        this.category = category;
    }
}
