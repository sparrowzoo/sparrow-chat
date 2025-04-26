package com.sparrow.chat.contact.assembler;

import com.sparrow.chat.contact.protocol.enums.Nationality;
import com.sparrow.chat.contact.protocol.vo.ContactVO;
import com.sparrow.passport.protocol.dto.UserProfileDTO;
import com.sparrow.utility.BeanUtility;
import org.springframework.stereotype.Component;

@Component
public class UserAssembler {
    public ContactVO userDto2ContactVo(UserProfileDTO userProfile){
        ContactVO userVO = new ContactVO();
        if(userProfile == null) return userVO;

        BeanUtility.copyProperties(userProfile, userVO);
        userVO.setFlagUrl(Nationality.CHINA.getFlag());
        userVO.setSignature(userProfile.getPersonalSignature());
        return userVO;
    }
}
