package com.sparrow.chat.protocol;

public class ChatUser {
    public static ChatUser parse(String key) {
        String[] parts = key.split("-");
        if (parts.length != 2) {
            return null;
        }
        String id = parts[0];
        Integer role = Integer.parseInt(parts[1]);
        return new ChatUser(id, role);
    }

    public static ChatUser longUserId(Long id, Integer role) {
        return new ChatUser(id.toString(), role);
    }

    public static ChatUser stringUserId(String id, Integer role) {
        return new ChatUser(id, role);
    }

    private ChatUser(String id, Integer role) {
        this.id = id;
        this.category = role;
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

    public String key() {
        return this.id + "-" + this.category;
    }

    public boolean equals(ChatUser chatUser) {
        if (chatUser == null) {
            return false;
        }
        return this.id.equals(chatUser.getId()) && this.category.equals(chatUser.getCategory());
    }
}
