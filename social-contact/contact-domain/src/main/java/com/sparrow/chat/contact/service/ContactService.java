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

package com.sparrow.chat.contact.service;

import com.sparrow.authenticator.enums.AuthenticatorError;
import com.sparrow.chat.contact.bo.ContactsWrapBO;
import com.sparrow.chat.contact.bo.UserProfileBO;
import com.sparrow.chat.contact.protocol.dto.FriendDetailDTO;
import com.sparrow.chat.contact.protocol.dto.QunDTO;
import com.sparrow.chat.contact.repository.ContactRepository;
import com.sparrow.chat.contact.repository.QunRepository;
import com.sparrow.context.SessionContext;
import com.sparrow.exception.Asserts;
import com.sparrow.passport.api.UserProfileAppService;
import com.sparrow.passport.protocol.dto.UserProfileDTO;
import com.sparrow.protocol.BusinessException;
import com.sparrow.utility.StringUtility;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Named
public class ContactService {

    @Inject
    private SecretService secretService;

    @Inject
    private UserProfileAppService userProfileAppService;

    @Inject
    private QunRepository qunRepository;
    @Inject
    private ContactRepository contactRepository;

    public UserProfileBO findFriend(String userIdentify) throws BusinessException {
        Asserts.isTrue(StringUtility.isNullOrEmpty(userIdentify), AuthenticatorError.USER_DISABLE);
        UserProfileDTO userDto = this.userProfileAppService.getByIdentify(userIdentify);
        String secretIdentify = secretService.encryptUserIdentify(userDto);
        return new UserProfileBO(userDto, secretIdentify);
    }

    public ContactsWrapBO getContacts() throws BusinessException {
        List<QunDTO> myQuns = this.qunRepository.getMyQunList();
        ContactsWrapBO contactsWrapBO = new ContactsWrapBO(myQuns);

        //通讯录加自己
        /**
         * java.lang.UnsupportedOperationException: null
         *     at java.util.AbstractList.add(AbstractList.java:148) ~[na:1.8.0_281]
         *     at java.util.AbstractList.add(AbstractList.java:108) ~[na:1.8.0_281]
         *     at com.sparrow.chat.contact.service.ContactService.getContacts
         */
        Long userId = SessionContext.getLoginUser().getUserId();
        List<FriendDetailDTO> otherContacts = this.contactRepository.getContacts(userId);
        contactsWrapBO.setFriends(otherContacts);
        Set<Long> contactUserIds = contactsWrapBO.getUserIds(userId);
        Map<Long, UserProfileDTO> userProfileMap = this.userProfileAppService.getUserMap(contactUserIds);
        contactsWrapBO.setUserMap(userProfileMap);
        return contactsWrapBO;
    }

    public Map<Long, UserProfileDTO> getUserMap(List<Long> userIds) throws BusinessException {
        return this.userProfileAppService.getUserMap(userIds);
    }
}
