package com.sparrow.chat.contact.service;


import com.sparrow.passport.protocol.dto.UserProfileDTO;
import com.sparrow.protocol.BusinessException;

/**
 * 加密服务接口
 */
public interface SecretService {
    /**
     * 将用户信息进行加密
     *
     * @param userDto
     * @return
     */
    String encryptUserIdentify(UserProfileDTO userDto) throws BusinessException;

    /**
     * 通过加密的用户信息解析出用户明文信息
     *
     * @param secretIdentify
     * @return
     */
    Long parseUserSecretIdentify(String secretIdentify) throws BusinessException;
}
