package com.sparrow.chat.contact.po;

import com.sparrow.protocol.MethodOrder;
import com.sparrow.protocol.dao.PO;

import javax.persistence.*;

@Table(name = "qun")
public class Qun extends PO {
    private Long id;
    private String name;
    private String avatar;
    private String announcement;
    private Integer nationalityId;
    private Long organizationId;
    private Long ownerId;
    private Long categoryId;
    private String remark;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "int(11) UNSIGNED ")
    @MethodOrder(order = 1)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @MethodOrder(order = 2)
    @Column(name = "name", columnDefinition = "varchar(32)  DEFAULT '' COMMENT '群名称'", nullable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @MethodOrder(order = 2.1F)
    @Column(name = "avatar", columnDefinition = "varchar(256)  DEFAULT '' COMMENT '群名称'", nullable = false)
    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    @MethodOrder(order = 3)
    @Column(name = "announcement", columnDefinition = "varchar(255)  DEFAULT '' COMMENT '群公告'", nullable = false)
    public String getAnnouncement() {
        return announcement;
    }

    public void setAnnouncement(String announcement) {
        this.announcement = announcement;
    }

    @MethodOrder(order = 4)
    @Column(name = "nationality_id", columnDefinition = "int(11)  DEFAULT 0 COMMENT '国籍id'", nullable = false, updatable = false)
    public Integer getNationalityId() {
        return nationalityId;
    }

    public void setNationalityId(Integer nationalityId) {
        this.nationalityId = nationalityId;
    }

    @MethodOrder(order = 5)
    @Column(name = "organization_id", columnDefinition = "int(11) UNSIGNED  DEFAULT 0 COMMENT '机构ID'", nullable = false, updatable = false)
    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    @MethodOrder(order = 6)
    @Column(name = "owner_id", columnDefinition = "int(11) UNSIGNED  DEFAULT 0 COMMENT '群主'", nullable = false, updatable = false)
    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    @MethodOrder(order = 7)
    @Column(name = "category_id", columnDefinition = "int(11) UNSIGNED  DEFAULT 0 COMMENT '所属类别'", nullable = false, updatable = false)
    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    @MethodOrder(order = 8)
    @Column(name = "remark", columnDefinition = "varchar(255)  DEFAULT '' COMMENT '备注'", nullable = false)
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
