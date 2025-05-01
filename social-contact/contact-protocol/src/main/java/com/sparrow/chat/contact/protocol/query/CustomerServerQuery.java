package com.sparrow.chat.contact.protocol.query;

import com.sparrow.protocol.enums.StatusRecord;
import com.sparrow.protocol.pager.SimplePager;

public class CustomerServerQuery extends SimplePager {
    private String customerServiceName;
    private Long customerServiceId;
    private Integer tenantId;
    private StatusRecord status;
}
