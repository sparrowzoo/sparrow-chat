package com.sparrow.chat.enums;

public enum BusinessType {
    FRIEND(1, "好友"),
    QUN(2, "群");

    private final int businessType;
    private final String description;

    BusinessType(int businessType, String description) {
        this.businessType = businessType;
        this.description = description;
    }

    public int getBusinessType() {
        return businessType;
    }

    public String getDescription() {
        return description;
    }

    public static BusinessType getBusinessType(Integer businessType) {
        BusinessType[] auditBusinessTypes = values();
        for (BusinessType auditBusinessType : auditBusinessTypes) {
            if (auditBusinessType.businessType == businessType) {
                return auditBusinessType;
            }
        }
        return BusinessType.FRIEND;
    }
}
