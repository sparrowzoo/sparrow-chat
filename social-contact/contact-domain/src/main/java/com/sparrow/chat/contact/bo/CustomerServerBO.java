package com.sparrow.chat.contact.bo;

import com.sparrow.protocol.BO;
import com.sparrow.protocol.enums.StatusRecord;
import lombok.Data;

@Data
public class CustomerServerBO implements BO {
    private Long id;
    private String tenantId;
    private Integer category;
    private Long serverId;
    private String serverName;
    private StatusRecord status;
    private String createUserName;
    private Long createUserId;
    private Long modifiedUserId;
    private String modifiedUserName;
    private Long gmtCreate;
    private Long gmtModified;
}
