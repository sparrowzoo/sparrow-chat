package com.sparrow.chat.protocol.qun;

public class QunModifyNameParam {
    private Long qunId;
    private String name;

    public Long getQunId() {
        return qunId;
    }

    public void setQunId(Long qunId) {
        this.qunId = qunId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
