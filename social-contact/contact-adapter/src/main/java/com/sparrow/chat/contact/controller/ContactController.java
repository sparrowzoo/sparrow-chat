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

package com.sparrow.chat.contact.controller;

import com.sparrow.authenticator.enums.AuthenticatorError;
import com.sparrow.chat.contact.assembler.ContactAssembler;
import com.sparrow.chat.contact.bo.ContactsWrapBO;
import com.sparrow.chat.contact.bo.CustomerServerBO;
import com.sparrow.chat.contact.bo.UserProfileBO;
import com.sparrow.chat.contact.protocol.FindUserSecretParam;
import com.sparrow.chat.contact.protocol.vo.ContactGroupVO;
import com.sparrow.chat.contact.protocol.vo.ContactVO;
import com.sparrow.chat.contact.protocol.vo.UserFriendApplyVO;
import com.sparrow.chat.contact.service.ContactService;
import com.sparrow.chat.contact.service.CustomerServerService;
import com.sparrow.context.SessionContext;
import com.sparrow.exception.Asserts;
import com.sparrow.passport.protocol.dto.UserProfileDTO;
import com.sparrow.protocol.BusinessException;
import com.sparrow.protocol.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/contact")
public class ContactController {

    @Autowired
    private ContactService contactService;
    @Autowired
    private ContactAssembler contactAssembler;

    @Autowired
    private CustomerServerService customerServerService;

    /**
     * 通过用户标识查找用户密文标识 和 用户基本信息
     *
     * @param findUserSecretParam
     * @return
     */
    @PostMapping("/find-friend.json")
    public UserFriendApplyVO findFriend(@RequestBody FindUserSecretParam findUserSecretParam) throws BusinessException {
        UserProfileBO contactBO = this.contactService.findFriend(findUserSecretParam.getUserIdentify());
        return this.contactAssembler.toUserFriendApplyVO(contactBO);
    }

    @GetMapping("/contacts.json")
    public ContactGroupVO getContacts() throws BusinessException {
        LoginUser loginUser = SessionContext.getLoginUser();
        Asserts.isTrue(loginUser.isVisitor(), AuthenticatorError.USER_NOT_LOGIN);
        ContactsWrapBO contactsWrapBO = this.contactService.getContacts();
        return this.contactAssembler.assembleVO(contactsWrapBO);
    }


    @PostMapping("/get-users-by-ids.json")
    public List<ContactVO> getUsersByIds(@RequestBody List<Long> userIds) throws BusinessException {
        Map<Long, UserProfileDTO> userProfileDTOMap = this.contactService.getUserMap(userIds);
        return this.contactAssembler.assembleUserListVO(userProfileDTOMap.values());
    }

    @PostMapping("/get-customer-servers.json")
    public List<ContactVO> getUsersTenantId() throws BusinessException {
        LoginUser loginUser = SessionContext.getLoginUser();
        List<CustomerServerBO> customerServers = this.customerServerService.getCustomerServerListByTenantId(loginUser.getTenantId());
        List<Long> customerServerIds = customerServers.stream().map(CustomerServerBO::getServerId).collect(java.util.stream.Collectors.toList());
        Map<Long, UserProfileDTO> userProfileDTOMap = this.contactService.getUserMap(customerServerIds);
        return this.contactAssembler.assembleUserListVO(userProfileDTOMap.values());
    }
}
