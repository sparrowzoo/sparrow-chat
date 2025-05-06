package com.sparrow.chat.contact.repository;

import com.sparrow.chat.contact.bo.AuditBO;
import com.sparrow.chat.contact.protocol.dto.FriendDetailDTO;

import java.util.List;

public interface ContactRepository {
    void addContact(AuditBO auditBO);

    List<FriendDetailDTO> getContacts(Long userId);
}
