package com.sparrow.chat.contact.protocol.vo;

import com.sparrow.protocol.VO;

import java.util.List;

public class ContactGroupVO implements VO {

    public ContactGroupVO(List<QunVO> quns, List<ContactVO> users) {
        this.quns = quns;
        this.contacts = users;
    }

    private List<QunVO> quns;
    private List<ContactVO> contacts;

    public List<QunVO> getQuns() {
        return quns;
    }

    public void setQuns(List<QunVO> quns) {
        this.quns = quns;
    }

    public List<ContactVO> getContacts() {
        return contacts;
    }

    public void setContacts(List<ContactVO> contacts) {
        this.contacts = contacts;
    }
}
