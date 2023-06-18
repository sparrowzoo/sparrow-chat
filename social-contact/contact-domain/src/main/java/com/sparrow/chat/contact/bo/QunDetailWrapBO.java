package com.sparrow.chat.contact.bo;

import com.sparrow.passport.protocol.dto.UserProfileDTO;

public class QunDetailWrapBO {
    private QunBO qun;
    private UserProfileDTO owner;

    public QunDetailWrapBO(QunBO qun, UserProfileDTO owner) {
        this.qun = qun;
        this.owner = owner;
    }

    public QunBO getQun() {
        return qun;
    }

    public UserProfileDTO getOwner() {
        return owner;
    }
}
