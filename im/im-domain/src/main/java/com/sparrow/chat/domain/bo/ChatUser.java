package com.sparrow.chat.domain.bo;

import com.sparrow.chat.protocol.query.ChatUserQuery;
import com.sparrow.protocol.LoginUser;
import com.sparrow.protocol.constant.magic.Symbol;
import lombok.Data;

@Data
public class ChatUser {

    public static ChatUser convertFromQuery(ChatUserQuery chatUser) {
        return new ChatUser(chatUser.getId(), chatUser.getCategory());
    }

    public static ChatUser parse(String key) {
        String[] parts = key.split(Symbol.UNDERLINE);
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

    public Integer getSessionCategory() {
        if (this.isVisitor()) {
            return LoginUser.CATEGORY_VISITOR;
        }
        return LoginUser.CATEGORY_REGISTER;
    }

    public Boolean isVisitor() {
        return this.category == LoginUser.CATEGORY_VISITOR;
    }

    public String key() {
        return this.id + Symbol.UNDERLINE + this.getSessionCategory();
    }

    public boolean equals(ChatUser chatUser) {
        if (chatUser == null) {
            return false;
        }
        if (chatUser.isVisitor()) {
            return this.id.equals(chatUser.id) && this.category.equals(chatUser.category);
        }
        return this.id.equals(chatUser.id);
    }

    public boolean equals(ChatUserQuery chatUser) {
        if (chatUser == null) {
            return false;
        }
        return this.id.equals(chatUser.getId()) && this.category.equals(chatUser.getCategory());
    }

    public Long getLongUserId() {
        return Long.parseLong(this.id);
    }

    /**
     * 用户ID长度(1字节) + 用户ID([用户ID长度个字节]) + 用户类型(1字节)
     *
     * @return
     */
    public byte[] toBytes() {
        byte[] idBytes = this.id.getBytes();
        int capacity = 1 + idBytes.length + 1;
        byte[] byteBuf = new byte[capacity];
        byteBuf[0] = (byte) idBytes.length;
        System.arraycopy(idBytes, 0, byteBuf, 1, idBytes.length);
        idBytes[idBytes.length - 1] = this.category.byteValue();
        return byteBuf;
    }

    public ChatUserQuery toChatUserQuery() {
        return new ChatUserQuery(this.id, this.category);
    }
}
