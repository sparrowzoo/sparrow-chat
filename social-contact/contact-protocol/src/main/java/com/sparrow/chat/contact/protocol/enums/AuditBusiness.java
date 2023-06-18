package com.sparrow.chat.contact.protocol.enums;

public enum AuditBusiness {
    FRIEND(1, "申请好友"),
    GROUP(2, "申请加入社群");

    private Integer business;
    private String description;

    AuditBusiness(Integer business, String description) {
        this.business = business;
        this.description = description;
    }

    public Integer getBusiness() {
        return business;
    }

    public String getDescription() {
        return description;
    }

    public static AuditBusiness getInstance(Integer business) {
        for (AuditBusiness auditBusiness : AuditBusiness.values()) {
            if (auditBusiness.business.equals(business)) {
                return auditBusiness;
            }
        }
        return null;
    }
}


