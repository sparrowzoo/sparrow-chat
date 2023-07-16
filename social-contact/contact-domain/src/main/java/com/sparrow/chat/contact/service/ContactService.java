package com.sparrow.chat.contact.service;

import com.sparrow.chat.contact.bo.ContactsWrapBO;
import com.sparrow.chat.contact.bo.QunBO;
import com.sparrow.chat.contact.bo.UserProfileBO;
import com.sparrow.chat.contact.repository.ContactRepository;
import com.sparrow.chat.contact.repository.QunRepository;
import com.sparrow.exception.Asserts;
import com.sparrow.passport.api.UserProfileAppService;
import com.sparrow.passport.protocol.dto.UserProfileDTO;
import com.sparrow.protocol.BusinessException;
import com.sparrow.protocol.ThreadContext;
import com.sparrow.protocol.constant.SparrowError;
import com.sparrow.utility.StringUtility;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;
import java.util.Map;

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
        Asserts.isTrue(StringUtility.isNullOrEmpty(userIdentify), SparrowError.USER_DISABLE);
        UserProfileDTO userDto = this.userProfileAppService.getByIdentify(userIdentify);
        String secretIdentify = secretService.encryptUserIdentify(userDto);
        return new UserProfileBO(userDto, secretIdentify);
    }

    public ContactsWrapBO getContacts() throws BusinessException {
        List<Long> contactUserIds = this.contactRepository.getContacts();
        //通讯录加自己f
        contactUserIds.add(ThreadContext.getLoginToken().getUserId());
        Map<Long, UserProfileDTO> userProfileMap = this.userProfileAppService.getUserMap(contactUserIds);
        List<QunBO> myQuns = this.qunRepository.getMyQunList();
        return new ContactsWrapBO(userProfileMap.values(), myQuns);
    }

    public Map<Long, UserProfileDTO> getUserMap(List<Long> userIds) throws BusinessException {
        return this.userProfileAppService.getUserMap(userIds);
    }
}
