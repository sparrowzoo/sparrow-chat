package com.sparrow.chat.contact.bo;

import com.sparrow.passport.protocol.dto.UserProfileDTO;
import lombok.Data;

@Data
public class UserProfileBO {

    public UserProfileBO(UserProfileDTO userDto, String secretIdentify) {
        this.userDto = userDto;
        this.secretIdentify = secretIdentify;
    }

    private UserProfileDTO userDto;
    private String secretIdentify;
}
