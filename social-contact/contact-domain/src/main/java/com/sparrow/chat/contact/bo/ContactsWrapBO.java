package com.sparrow.chat.contact.bo;

import com.sparrow.passport.protocol.dto.UserProfileDTO;

import java.util.Collection;
import java.util.List;

public class ContactsWrapBO {

    public ContactsWrapBO(Collection<UserProfileDTO> users, List<QunBO> quns) {
        this.users = users;
        this.quns = quns;
    }

    private Collection<UserProfileDTO> users;
    private List<QunBO> quns;



    public Collection<UserProfileDTO> getUsers() {
        return users;
    }

    public void setUsers(Collection<UserProfileDTO> users) {
        this.users = users;
    }

    public List<QunBO> getQuns() {
        return quns;
    }

    public void setQuns(List<QunBO> quns) {
        this.quns = quns;
    }
}
