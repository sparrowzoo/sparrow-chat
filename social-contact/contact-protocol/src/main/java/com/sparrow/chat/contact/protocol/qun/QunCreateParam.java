package com.sparrow.chat.contact.protocol.qun;

import com.sparrow.protocol.Param;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("创建群参数")
public class QunCreateParam implements Param {
    @ApiModelProperty("群名称")
    private String name;
    @ApiModelProperty("群公告")
    private String announcement;
    @ApiModelProperty("所在国籍")
    private Integer nationalityId;

    @ApiModelProperty("所在组织")
    @Deprecated
    private Long organizationId;
    @ApiModelProperty("备注")
    private String remark;
    @ApiModelProperty("群所属类型")
    private Long categoryId;

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

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getNationalityId() {
        return nationalityId;
    }

    public void setNationalityId(Integer nationalityId) {
        this.nationalityId = nationalityId;
    }
}
