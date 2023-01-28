package com.sparrow.chat.dao;

import com.sparrow.chat.po.Contact;
import com.sparrow.orm.template.impl.ORMStrategy;
import javax.inject.Named;

@Named
public class ContactDaoImpl extends ORMStrategy<Contact, Long> implements ContactDAO {
}
