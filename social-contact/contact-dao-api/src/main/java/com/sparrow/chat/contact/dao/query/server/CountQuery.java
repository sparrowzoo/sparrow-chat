package com.sparrow.chat.contact.dao.query.server;

import com.sparrow.protocol.enums.StatusRecord;
import lombok.Data;

@Data
public class CountQuery {
    private String customerServiceName;
    private Long customerServiceId;
    private Integer tenantId;
    private StatusRecord status;
}
