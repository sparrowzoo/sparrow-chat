package com.sparrow.chat.contact.bo;

import com.sparrow.chat.contact.protocol.enums.Category;
import com.sparrow.passport.protocol.dto.UserProfileDTO;
import com.sparrow.protocol.BO;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class QunPlazaBO implements BO {
    private List<QunBO> qunList;
    /**
     * key:categoryId
     * value:类别
     */
    private Map<Integer, Category> categoryDicts;
    /**
     * key:userId
     * value:用户实体
     */
    private Map<Long, UserProfileDTO> userDicts;
}
