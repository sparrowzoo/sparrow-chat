package com.sparrow.chat.contact.rpc;

import com.sparrow.chat.contact.ContactServiceApi;
import com.sparrow.chat.contact.repository.ContactRepository;
import com.sparrow.protocol.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ContactRpcService implements ContactServiceApi {
    @Autowired
    private ContactRepository contactRepository;
    @Override
    public List<Long> getFriends(Long userId) throws BusinessException {
        return this.contactRepository.getContacts(userId);
    }
}
