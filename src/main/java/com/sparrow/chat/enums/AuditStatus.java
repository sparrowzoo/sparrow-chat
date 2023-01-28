package com.sparrow.chat.enums;

public enum AuditStatus {
    AUDITING(0, "审核中"),
    PASS(1, "审核通过"),
    REJECT(2, "拒绝");

    private final int status;
    private final String description;

    AuditStatus(int status, String description) {
        this.status = status;
        this.description = description;
    }

    public int getStatus() {
        return status;
    }

    public String getDescription() {
        return description;
    }

    public static AuditStatus getStatus(Integer status) {
        AuditStatus[] statuses = values();
        for (AuditStatus auditStatus : statuses) {
            if (auditStatus.status == status) {
                return auditStatus;
            }
        }
        return AUDITING;
    }
}
