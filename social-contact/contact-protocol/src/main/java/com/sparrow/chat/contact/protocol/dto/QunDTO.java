package com.sparrow.chat.contact.protocol.dto;

import com.sparrow.protocol.DTO;
import lombok.Data;

@Data
public class QunDTO implements DTO {
    private Long id;
    private String name;
    private String avatar;
    private String announcement;
    private Integer nationalityId;
    private String nationality;
    private Long organizationId;
    private Long ownerId;
    private Long createUserId;
    private Long createTime;
    private Integer categoryId;
    private String remark;
}
