package com.sparrow.chat.contact.service;

import com.sparrow.chat.contact.bo.ContactBO;
import com.sparrow.exception.Asserts;
import com.sparrow.passport.api.UserProfileAppService;
import com.sparrow.passport.protocol.dto.UserProfileDTO;
import com.sparrow.protocol.BusinessException;
import com.sparrow.protocol.constant.SparrowError;
import com.sparrow.utility.StringUtility;

import javax.inject.Inject;

public class ContactService {

    private SecretService secretService;

    @Inject
    private UserProfileAppService userProfileAppService;

    public ContactBO findFriend(String userIdentify) throws BusinessException {
        Asserts.isTrue(StringUtility.isNullOrEmpty(userIdentify), SparrowError.USER_DISABLE);
        UserProfileDTO userDto =this.userProfileAppService.getByIdentify(userIdentify);
        String secretIdentify = secretService.encryptUserIdentify(userDto);
        return new ContactBO(userDto, secretIdentify);
    }
}
