package com.sparrow.chat.contact.bo;

import com.sparrow.protocol.BO;
import lombok.Data;

@Data
public class QunBO implements BO {
    private Long id;
    private String name;

    private String avatar;
    private String announcement;
    private Integer nationalityId;
    private String nationality;
    private Long organizationId;
    private Long ownerId;
    private Long categoryId;
    private String category;
    private String remark;

}
