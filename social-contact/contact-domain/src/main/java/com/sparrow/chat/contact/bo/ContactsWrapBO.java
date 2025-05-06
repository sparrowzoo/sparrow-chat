package com.sparrow.chat.contact.bo;

import com.sparrow.chat.contact.protocol.dto.FriendDetailDTO;
import com.sparrow.passport.protocol.dto.UserProfileDTO;
import com.sparrow.utility.CollectionsUtility;
import lombok.Data;

import java.util.*;

@Data
public class ContactsWrapBO {

    public ContactsWrapBO(List<QunBO> quns) {
        this.quns = quns;
    }

    private Map<Long,UserProfileDTO> userMap;
    private List<QunBO> quns;
    private List<FriendDetailDTO> friends;
    private Set<Long> getQunOwnerIds() {
        Set<Long> ids = new HashSet<>();
        for (QunBO qun : quns) {
            ids.add(qun.getOwnerId());
        }
        return ids;
    }

    private Set<Long> getFriendIds() {
        Set<Long> ids = new HashSet<>();
        for (FriendDetailDTO friend : friends) {
            ids.add(friend.getFriendId());
        }
        return ids;
    }

    public Set<Long> getUserIds(Long currentUserId) {
        Set<Long> contactUserIds = new HashSet<>();
        contactUserIds.add(currentUserId);
        Collection<Long> contactIds=this.getFriendIds();
        if (!CollectionsUtility.isNullOrEmpty(contactIds)) {
            contactUserIds.addAll(contactIds);
        }
        contactUserIds.addAll(this.getQunOwnerIds());
        return contactUserIds;
    }
}
