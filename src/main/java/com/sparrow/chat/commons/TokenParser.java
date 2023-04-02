package com.sparrow.chat.commons;

import com.sparrow.core.spi.JsonFactory;
import com.sparrow.enums.HttpMethod;
import com.sparrow.json.Json;
import com.sparrow.protocol.BusinessException;
import com.sparrow.protocol.constant.SparrowError;
import com.sparrow.utility.HttpClient;
import java.util.HashMap;
import java.util.Map;

public class TokenParser {
    public static Integer parseUserId(String token) throws BusinessException {
        Map<String, String> header = new HashMap<>();
        header.put("X-Sugar-Token", token);
        String result = HttpClient.request(HttpMethod.GET, "http://studyapi.zhilongsoft.com/app/authMember/info"
            , "", null, header, false);
        Json json = JsonFactory.getProvider();
        Map<String, Object> map = json.parse(result);
        Integer code = (Integer) map.get("code");
        if (code.equals(200)) {
            Map<String, Object> userMap = (Map<String, Object>) map.get("data");
            Map<String, Object> userProperty = (Map<String, Object>) userMap.get("member");
            return (Integer) userProperty.get("id");
        }
        throw new BusinessException(SparrowError.USER_NOT_LOGIN);
    }
}
