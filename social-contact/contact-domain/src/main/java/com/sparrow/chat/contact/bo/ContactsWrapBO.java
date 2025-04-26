package com.sparrow.chat.contact.bo;

import com.sparrow.passport.protocol.dto.UserProfileDTO;
import lombok.Data;

import java.util.*;

@Data
public class ContactsWrapBO {

    public ContactsWrapBO( List<QunBO> quns) {
        this.quns = quns;
    }

    private Map<Long,UserProfileDTO> userMap;
    private List<QunBO> quns;
    private List<Long> contactIds;

    public Set<Long> getQunOwnerIds() {
        Set<Long> ids = new HashSet<>();
        for (QunBO qun : quns) {
            ids.add(qun.getOwnerId());
        }
        return ids;
    }
}
