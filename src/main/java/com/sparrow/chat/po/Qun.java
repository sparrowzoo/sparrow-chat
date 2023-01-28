package com.sparrow.chat.po;

import com.sparrow.protocol.MethodOrder;
import com.sparrow.protocol.POJO;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "qun")
public class Qun implements POJO {

    private Long id;

    private String name;

    private Long lastUserId;

    private String announcement;

    private String nationality;

    private Long institutionId;

    private Long createUserId;

    private Long updateUserId;

    private Long createTime;

    private Long updateTime;

    private String remark;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "int(11) UNSIGNED AUTO_INCREMENT")
    @MethodOrder(order = 1)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @MethodOrder(order = 2)
    @Column(name = "name", columnDefinition = "varchar(64)  DEFAULT '' COMMENT '群名称'", nullable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @MethodOrder(
        order = 3
    )
    @Column(
        name = "last_user_id",
        columnDefinition = "int(11) unsigned  DEFAULT 0 COMMENT '最后加入user id'",
        nullable = false,
        updatable = false
    )
    public Long getLastUserId() {
        return lastUserId;
    }

    public void setLastUserId(Long lastUserId) {
        this.lastUserId = lastUserId;
    }

    @MethodOrder(order = 4)
    @Column(name = "announcement", columnDefinition = "varchar(255)  DEFAULT '' COMMENT '群公告'", nullable = false, updatable = false)
    public String getAnnouncement() {
        return announcement;
    }

    public void setAnnouncement(String announcement) {
        this.announcement = announcement;
    }

    @MethodOrder(order = 5)
    @Column(name = "nationality", columnDefinition = "varchar(64)  DEFAULT '' COMMENT '国籍'", nullable = false, updatable = false)
    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    @MethodOrder(
        order = 6
    )
    @Column(
        name = "institution_id",
        columnDefinition = "bigint(11)  DEFAULT 0 COMMENT '机构ID'",
        nullable = false,
        updatable = false
    )
    public Long getInstitutionId() {
        return institutionId;
    }

    public void setInstitutionId(Long institutionId) {
        this.institutionId = institutionId;
    }

    @MethodOrder(
        order = 7
    )
    @Column(
        name = "create_user_id",
        columnDefinition = "bigint(11)  DEFAULT 0 COMMENT '创建人ID'",
        nullable = false,
        updatable = false
    )
    public Long getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(Long createUserId) {
        this.createUserId = createUserId;
    }

    @MethodOrder(
        order = 8
    )
    @Column(
        name = "update_user_id",
        columnDefinition = "bigint(11)  DEFAULT 0 COMMENT '更新人ID'",
        nullable = false
    )
    public Long getUpdateUserId() {
        return updateUserId;
    }

    public void setUpdateUserId(Long updateUserId) {
        this.updateUserId = updateUserId;
    }

    @MethodOrder(
        order = 9
    )
    @Column(
        name = "create_time",
        columnDefinition = "bigint(11)  DEFAULT 0 COMMENT '创建时间'",
        nullable = false,
        updatable = false
    )
    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    @MethodOrder(
        order = 10
    )
    @Column(
        name = "update_time",
        columnDefinition = "bigint(11)  DEFAULT 0 COMMENT '更新时间'",
        nullable = false
    )
    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    @MethodOrder(
        order = 11
    )
    @Column(
        name = "remark",
        columnDefinition = "varchar(255)  DEFAULT 0 COMMENT '备注'",
        nullable = false
    )
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
