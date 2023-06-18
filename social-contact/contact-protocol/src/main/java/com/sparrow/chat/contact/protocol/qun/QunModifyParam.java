package com.sparrow.chat.contact.protocol.qun;

import com.sparrow.protocol.Param;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("修改群")
public class QunModifyParam implements Param {
    @ApiModelProperty("群id")
    private Long qunId;
    @ApiModelProperty("群名称")
    private String name;
    @ApiModelProperty("群公告")
    private String announcement;
    @ApiModelProperty("备注")
    private String remark;

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

    public String getAnnouncement() {
        return announcement;
    }

    public void setAnnouncement(String announcement) {
        this.announcement = announcement;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
