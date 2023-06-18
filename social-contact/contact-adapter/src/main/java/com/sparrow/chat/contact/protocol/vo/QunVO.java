package com.sparrow.chat.contact.protocol.vo;

import com.sparrow.protocol.VO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("群详情")
public class QunVO implements VO {
    @ApiModelProperty("群ID")
    private Long qunId;
    @ApiModelProperty("群名称")
    private String name;
    @ApiModelProperty("群公告")
    private String announcement;
    @ApiModelProperty("所属国籍")
    private String nationality;
    @ApiModelProperty("备注")
    private String remark;
    @ApiModelProperty("群主ID")
    private Long ownerId;
    @ApiModelProperty("群主名")
    private String ownerName;

    @ApiModelProperty("所属类型id")
    private Long categoryId;
    @ApiModelProperty("所属类型名")
    private String categoryName;

    @ApiModelProperty("图标")
    private String icon;

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

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }


    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
