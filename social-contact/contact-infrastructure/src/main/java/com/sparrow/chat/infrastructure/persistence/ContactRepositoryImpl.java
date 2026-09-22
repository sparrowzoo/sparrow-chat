/*
Licensed to the Apache Software Foundation (ASF) under one or more
contributor license agreements.  See the NOTICE file distributed with
this work for additional information regarding copyright ownership.
The ASF licenses this file to You under the Apache License, Version 2.0
(the "License"); you may not use this file except in compliance with
the License.  You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package com.sparrow.chat.infrastructure.persistence;

import com.sparrow.chat.contact.bo.AuditBO;
import com.sparrow.chat.contact.dao.ContactDao;
import com.sparrow.chat.contact.po.Contact;
import com.sparrow.chat.contact.protocol.dto.FriendDetailDTO;
import com.sparrow.chat.contact.repository.ContactRepository;
import com.sparrow.chat.infrastructure.persistence.data.converter.ContactConverter;
import com.sparrow.context.SessionContext;
import com.sparrow.protocol.LoginUser;
import com.sparrow.utility.CollectionsUtility;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.util.ArrayList;
import java.util.List;

@Named
public class ContactRepositoryImpl implements ContactRepository {
    @Inject
    private ContactConverter contactConverter;

    @Inject
    private ContactDao contactDao;

    /**
     * 新增好友
     * <p>
     * 需要新增当前用户的好友 和 对方的好友，就是你！！！
     *
     * @param auditBO
     */
    @Override
    public void addContact(AuditBO auditBO) {
        Contact myContact = this.contactConverter.convert2MyPo(auditBO);
        this.contactDao.insert(myContact);
        Contact friendContact = this.contactConverter.convert2FriendPo(auditBO);
        this.contactDao.insert(friendContact);
    }

    @Override
    public List<FriendDetailDTO> getContacts(Long userId) {
        if (userId == null) {
            LoginUser loginUser = SessionContext.getLoginUser();
            userId = loginUser.getUserId();
        }
        List<Contact> contacts = this.contactDao.getMyContact(userId);
        if (CollectionsUtility.isNullOrEmpty(contacts)) {
            return null;
        }
        List<FriendDetailDTO> contactDtos = new ArrayList<>(contacts.size());
        for (Contact contact : contacts) {
            contactDtos.add(new FriendDetailDTO(contact.getFriendId(), contact.getUserId(), contact.getApplyTime()));
        }
        return contactDtos;
    }
}
