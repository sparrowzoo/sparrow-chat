package com.sparrow.chat.contact.protocol.qun;

import java.util.List;

public class QunCreateParam {
    private String name;
    private List<Long> userIds;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Long> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<Long> userIds) {
        this.userIds = userIds;
    }
}
