package com.sparrow.chat.contact.assembler;

import com.sparrow.chat.contact.protocol.enums.Nationality;
import com.sparrow.chat.contact.protocol.vo.ContactVO;
import com.sparrow.passport.protocol.dto.UserProfileDTO;
import com.sparrow.protocol.BeanCopier;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

@Component
public class UserAssembler {
    @Inject
    private BeanCopier beanCopier;

    public ContactVO userDto2ContactVo(UserProfileDTO userProfile) {
        ContactVO userVO = new ContactVO();
        if (userProfile == null) return userVO;

        this.beanCopier.copyProperties(userProfile, userVO);
        userVO.setFlagUrl(Nationality.CHINA.getFlag());
        userVO.setSignature(userProfile.getPersonalSignature());
        return userVO;
    }
}
