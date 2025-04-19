package com.sparrow.chat.protocol.dto;

import com.sparrow.protocol.DTO;
import lombok.Data;

@Data
public class QunDTO implements DTO {
    /**
     * 群id
     */
    private String qunId;
    /**
     * 群名称
     */
    private String qunName;
    /**
     * 国籍
     */
    private String nationality;
    /**
     * 国旗url
     */
    private String flagUrl;
    /**
     * 单位图标
     */
    private String icon;
    /**
     * 群公告
     */
    private String announcement;
    /**
     * 创建人 群主
     */
    private Integer createUserId;
}
