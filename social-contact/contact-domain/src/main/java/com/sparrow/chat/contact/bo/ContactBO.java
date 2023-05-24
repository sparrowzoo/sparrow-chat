package com.sparrow.chat.contact.bo;

import com.sparrow.chat.contact.protocol.UserDTO;

public class ContactBO {

    public ContactBO(UserDTO userDto, String secretIdentify) {
        this.userDto = userDto;
        this.secretIdentify = secretIdentify;
    }

    private UserDTO userDto;
    private String secretIdentify;

    public UserDTO getUserDto() {
        return userDto;
    }

    public void setUserDto(UserDTO userDto) {
        this.userDto = userDto;
    }

    public String getSecretIdentify() {
        return secretIdentify;
    }

    public void setSecretIdentify(String secretIdentify) {
        this.secretIdentify = secretIdentify;
    }
}
