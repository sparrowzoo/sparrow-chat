package com.sparrow.chat.contact.dao;

import com.sparrow.chat.contact.po.Contact;
import com.sparrow.protocol.dao.DaoSupport;

import java.util.List;

public interface ContactDao extends DaoSupport<Contact, Long> {
    List<Contact> getMyContact(Long userId);
}
