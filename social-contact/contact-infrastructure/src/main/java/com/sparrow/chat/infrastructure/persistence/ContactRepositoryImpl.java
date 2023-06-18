package com.sparrow.chat.infrastructure.persistence;

import com.sparrow.chat.contact.bo.AuditBO;
import com.sparrow.chat.contact.dao.ContactDao;
import com.sparrow.chat.contact.po.Contact;
import com.sparrow.chat.contact.repository.ContactRepository;
import com.sparrow.chat.infrastructure.persistence.data.converter.ContactConverter;

import javax.inject.Inject;
import javax.inject.Named;

@Named
public class ContactRepositoryImpl implements ContactRepository {
    @Inject
    private ContactConverter contactConverter;

    @Inject
    private ContactDao contactDao;

    @Override
    public Long addContact(AuditBO auditBO) {
        Contact contact = this.contactConverter.convert2po(auditBO);
        this.contactDao.insert(contact);
        return contact.getId();
    }
}
