package com.sparrow.chat.contact.po;

import com.sparrow.protocol.dao.PO;
import com.sparrow.protocol.enums.StatusRecord;

import javax.persistence.*;

@Table(name = "t_qun")
public class Qun extends PO {
    private Long id;
    private String name;
    private String avatar;
    private String announcement;
    private Integer nationalityId;
    private Long organizationId;
    private Long ownerId;
    private Integer categoryId;
    private String remark;

    private StatusRecord status;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "int(11) UNSIGNED ")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "name", columnDefinition = "varchar(32)  DEFAULT '' COMMENT '群名称'", nullable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "avatar", columnDefinition = "varchar(256)  DEFAULT '' COMMENT '群头象'", nullable = false)
    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    @Column(name = "announcement", columnDefinition = "varchar(255)  DEFAULT '' COMMENT '群公告'", nullable = false)
    public String getAnnouncement() {
        return announcement;
    }

    public void setAnnouncement(String announcement) {
        this.announcement = announcement;
    }

    @Column(name = "nationality_id", columnDefinition = "int(11)  DEFAULT 0 COMMENT '国籍id'", nullable = false, updatable = false)
    public Integer getNationalityId() {
        return nationalityId;
    }

    public void setNationalityId(Integer nationalityId) {
        this.nationalityId = nationalityId;
    }

    @Column(name = "organization_id", columnDefinition = "int(11) UNSIGNED  DEFAULT 0 COMMENT '机构ID'", nullable = false, updatable = false)
    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    @Column(name = "owner_id", columnDefinition = "int(11) UNSIGNED  DEFAULT 0 COMMENT '群主'", nullable = false, updatable = false)
    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    @Column(name = "category_id", columnDefinition = "int(11) UNSIGNED  DEFAULT 0 COMMENT '所属类别'", nullable = false, updatable = false)
    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    @Column(name = "remark", columnDefinition = "varchar(255)  DEFAULT '' COMMENT '备注'", nullable = false)
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }


    @Column(name = "status", columnDefinition = "tinyint(2)  DEFAULT 1 COMMENT '状态'", nullable = false)
    public StatusRecord getStatus() {
        return status;
    }

    public void setStatus(StatusRecord status) {
        this.status = status;
    }
}
