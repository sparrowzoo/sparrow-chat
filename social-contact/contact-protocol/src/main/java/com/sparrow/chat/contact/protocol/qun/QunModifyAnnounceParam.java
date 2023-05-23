package com.sparrow.chat.contact.protocol.qun;

public class QunModifyAnnounceParam {
    private Long qunId;
    private String announce;

    public Long getQunId() {
        return qunId;
    }

    public void setQunId(Long qunId) {
        this.qunId = qunId;
    }

    public String getAnnounce() {
        return announce;
    }

    public void setAnnounce(String announce) {
        this.announce = announce;
    }
}
