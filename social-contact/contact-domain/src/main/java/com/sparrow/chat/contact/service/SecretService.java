package com.sparrow.chat.contact.service;


import com.sparrow.passport.protocol.dto.UserProfileDTO;

public interface SecretService {
    String encryptUserIdentify(UserProfileDTO userDto);

    UserProfileDTO parseUserSecretIdentify(String secretIdentify);
}
