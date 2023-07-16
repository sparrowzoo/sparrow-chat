package com.sparrow.chat.contact.protocol.event;

import com.sparrow.mq.MQEvent;

public class QunMemberEvent implements MQEvent {
    public QunMemberEvent(Long qunId, Long memberId) {
        this.qunId = qunId;
        this.memberId = memberId;
    }

    private Long qunId;

    private Long memberId;

    public Long getQunId() {
        return qunId;
    }

    public void setQunId(Long qunId) {
        this.qunId = qunId;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }
}
