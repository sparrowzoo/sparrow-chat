package com.sparrow.chat.contact.bo;

import com.sparrow.protocol.BO;
import lombok.Data;

@Data
public class QunMemberBO implements BO {
    private Long id;
    private Long memberId;
    private Long applyTime;
    private Long auditTime;
}
