package com.sparrow.chat.contact.repository;

import com.sparrow.chat.contact.bo.AuditBO;

import java.util.List;

public interface ContactRepository {
    void addContact(AuditBO auditBO);

    List<Long> getContacts(Long userId);
}
