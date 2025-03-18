package com.sparrow.chat.infrastructure.service;

import com.sparrow.chat.contact.protocol.enums.ContactError;
import com.sparrow.chat.contact.service.SecretService;
import com.sparrow.core.spi.JsonFactory;
import com.sparrow.cryptogram.ThreeDES;
import com.sparrow.exception.Asserts;
import com.sparrow.json.Json;
import com.sparrow.passport.protocol.dto.UserProfileDTO;
import com.sparrow.protocol.BusinessException;
import com.sparrow.protocol.LoginUser;
import com.sparrow.protocol.ThreadContext;
import com.sparrow.protocol.constant.magic.Symbol;
import com.sparrow.spring.starter.config.SparrowConfig;
import com.sparrow.utility.StringUtility;

import javax.inject.Inject;
import javax.inject.Named;
import java.time.Duration;


@Named
public class SecretServiceImpl implements SecretService {
    /**
     * 密钥的key
     */
    private static final String USER_IDENTIFY_SECRET_KEY = "contact_identify_secret_key";
    /**
     * 当前生产环境标识
     */
    private static final String SPARROW_PROD_PROFILE = "prod";

    @Inject
    private SparrowConfig sparrowConfig;

    private static final long TIMEOUT = Duration.ofHours(24).toMillis();

    /**
     * json 序列化接口
     * <p>
     * 可以在后序对json的序列化接口进行替换
     */
    private Json json = JsonFactory.getProvider();

    @Override
    public String encryptUserIdentify(UserProfileDTO userDto) throws BusinessException {
        Asserts.isTrue(userDto == null, ContactError.USER_IDENTIFY_INFO_EMPTY);
        Asserts.isTrue(StringUtility.isNullOrEmpty(userDto.getUserId()), ContactError.USER_IDENTIFY_INFO_ID_IS_EMPTY);
        LoginUser loginUser = ThreadContext.getLoginToken();
        String userInfo = userDto.getUserId() + Symbol.UNDERLINE + loginUser.getUserId() +
                Symbol.UNDERLINE;
        return ThreeDES.getInstance().encryptHex(this.getSecretKey(), userInfo);
    }

    @Override
    public Long parseUserSecretIdentify(String secretIdentify) throws BusinessException {
        Asserts.isTrue(StringUtility.isNullOrEmpty(secretIdentify), ContactError.USER_SECRET_IDENTIFY_IS_EMPTY);
        String userIdTime = ThreeDES.getInstance().decryptHex(this.getSecretKey(), secretIdentify);
        Asserts.isTrue(!userIdTime.contains(Symbol.UNDERLINE), ContactError.USER_SECRET_IDENTIFY_IS_ERROR);
        String[] userIdTimeArray = userIdTime.split(Symbol.UNDERLINE);
        Asserts.isTrue(userIdTimeArray.length != 2, ContactError.USER_SECRET_IDENTIFY_IS_ERROR);
        Long userId = Long.parseLong(userIdTimeArray[0]);
        Long applyUserId = Long.parseLong(userIdTimeArray[1]);
        LoginUser loginUser = ThreadContext.getLoginToken();
        Asserts.isTrue(!applyUserId.equals(loginUser.getUserId()), ContactError.USER_SECRET_IDENTIFY_APPLY_USER_NOT_MATCH);
        return userId;
    }


    private String getSecretKey() {
        //如果当前是生产环境，我在生产环境的服务器上通过环境变量来获取，保证安全性
        if (this.sparrowConfig.getProfile().equalsIgnoreCase(SPARROW_PROD_PROFILE)) {
            return System.getenv(USER_IDENTIFY_SECRET_KEY);
        }
        return this.sparrowConfig.getAuthenticator().getEncryptKey();
    }
}
