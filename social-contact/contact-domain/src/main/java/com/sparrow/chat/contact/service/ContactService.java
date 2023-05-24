package com.sparrow.chat.contact.service;

import com.sparrow.chat.contact.bo.ContactBO;
import com.sparrow.chat.contact.protocol.UserDTO;
import com.sparrow.exception.Asserts;
import com.sparrow.protocol.BusinessException;
import com.sparrow.protocol.constant.SparrowError;
import com.sparrow.utility.StringUtility;

public class ContactService {

    private SecretService secretService;

    //private UserService userService;

    public ContactBO findFriend(String userIdentify) throws BusinessException {
        Asserts.isTrue(StringUtility.isNullOrEmpty(userIdentify), SparrowError.USER_DISABLE);
        UserDTO userDto = null;//todo this.userService.getUserByIdentify(userIdentify);
        String secretIdentify = secretService.encryptUserIdentify(userDto);
        return new ContactBO(userDto, secretIdentify);
    }
}
