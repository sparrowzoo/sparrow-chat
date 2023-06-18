package com.sparrow.chat.contact.repository;

import com.sparrow.chat.contact.bo.AuditBO;

public interface ContactRepository {
    Long addContact(AuditBO auditBO);
}
