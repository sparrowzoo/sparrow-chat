package com.sparrow.chat.dao.sparrow;

import com.sparrow.chat.contact.dao.ContactDao;
import com.sparrow.chat.contact.po.Contact;
import com.sparrow.orm.template.impl.ORMStrategy;

import javax.inject.Named;

@Named
public class ContactDaoImpl extends ORMStrategy<Contact, Long> implements ContactDao {
}
