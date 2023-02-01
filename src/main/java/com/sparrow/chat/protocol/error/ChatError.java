package com.sparrow.chat.protocol.error;

import com.sparrow.protocol.ErrorSupport;
import com.sparrow.protocol.ModuleSupport;

public enum ChatError implements ErrorSupport {

    APPLY_FRIEND_IS_NULL(false, CharModule.AUDIT, "01", "apply friend id is null"),
    AUDIT_NOT_EXIST(false, CharModule.AUDIT, "02", "audit not exist"),
    AUDIT_BUSINESS_TYPE_NOT_MATCH(false, CharModule.AUDIT, "03", "audit business type not match"),
    AUDIT_USER_ID_NOT_MATCH_FRIEND_ID(false, CharModule.AUDIT, "04", "audit user id not match friend id"),
    AUDIT_STATUS_NOT_ALLOW_OPERATE(false, CharModule.AUDIT, "05", "audit status is not allow operate"),


    APPLY_QUN_ID_IS_NULL(false, CharModule.AUDIT, "06", "apply qun id is null"),
    AUDIT_USER_ID_NOT_MATCH_QUN_OWNER(false, CharModule.AUDIT, "07", "audit user id not match qun owner");


    private boolean system;
    private ModuleSupport module;
    private String code;
    private String message;

    ChatError(boolean system, ModuleSupport module, String code, String message) {
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
