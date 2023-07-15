package com.sparrow.chat.contact.protocol.enums;

import com.sparrow.protocol.ErrorSupport;
import com.sparrow.protocol.ModuleSupport;

public enum ContactError implements ErrorSupport {

    USER_IDENTIFY_INFO_EMPTY(false, ContactModule.IM_CONTACT, "00", "user information is empty"),
    USER_IDENTIFY_INFO_ID_IS_EMPTY(false, ContactModule.IM_CONTACT, "01", "user id is empty"),
    USER_SECRET_IDENTIFY_IS_EMPTY(false, ContactModule.IM_CONTACT, "02", "user secret identify is empty"),

    USER_SECRET_IDENTIFY_IS_ERROR(false, ContactModule.IM_CONTACT, "03", "user secret identify is error"),
    USER_SECRET_IDENTIFY_APPLY_USER_NOT_MATCH(false, ContactModule.IM_CONTACT, "05", "apply user id not match"),
    AUDIT_BUSINESS_TYPE_NOT_MATCH(false, ContactModule.IM_CONTACT, "06", "audit business type is not match"),
    AUDIT_USER_IS_NOT_MATCH(false, ContactModule.IM_CONTACT, "07", "audit user id not match"),

    CANNOT_APPLY_SELF_FRIEND(false, ContactModule.IM_CONTACT, "08", "user can't apply friend with self "),

    APPLY_FRIEND_CANNOT_BE_NULL(false, ContactModule.IM_CONTACT, "08", "user can't apply friend with self "),

    QUN_ID_IS_EMPTY(false, ContactModule.IM_QUN, "00", "qun id can't be empty"),

    QUN_NAME_IS_EMPTY(false, ContactModule.IM_QUN, "01", "qun name can't be empty"),
    CATEGORY_OF_QUN_EMPTY(false, ContactModule.IM_QUN, "02", "qun category can't be empty"),

    QUN_NOT_FOUND(false, ContactModule.IM_QUN, "03", "qun can't be found!"),
    QUN_STATUS_INVALID(false, ContactModule.IM_QUN, "04", "status of qun is invalid!"),

    NATIONALITY_OF_QUN_EMPTY(false, ContactModule.IM_QUN, "05", "qun nationality can't be empty"),
    QUN_OWNER_IS_NOT_MATCH(false, ContactModule.IM_QUN, "06", "qun owner can't match"),

    USER_IS_NOT_MEMBER(false, ContactModule.IM_QUN, "07", "user is not member of qun"),

    USER_IS_MEMBER(false, ContactModule.IM_QUN, "08", "user is member of qun"),


    ;
    /**
     * 是否系统级别的错误
     */
    private boolean system;
    /**
     * 表示业务的模块
     */
    private ModuleSupport module;
    /**
     * 表示具体的错误码
     */
    private String code;
    /**
     * 异常的错误信息
     */
    private String message;

    ContactError(boolean system, ModuleSupport module, String code, String message) {
        this.system = system;
        this.message = message;
        this.module = module;
        this.code = (system ? 0 : 1) + module.code() + code;
    }

    @Override
    public boolean system() {
        return this.system;
    }

    @Override
    public ModuleSupport module() {
        return this.module;
    }

    @Override
    public String getCode() {
        return this.code;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
