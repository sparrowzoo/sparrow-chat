package com.sparrow.chat.contact.service;

import com.sparrow.chat.contact.protocol.UserDTO;

public interface SecretService {
    String encryptUserIdentify(UserDTO userDto);

    UserDTO parseUserSecretIdentify(String secretIdentify);
}
