package com.sparrow.chat.contact.bo;

import com.sparrow.chat.contact.protocol.enums.Category;
import com.sparrow.passport.protocol.dto.UserProfileDTO;
import com.sparrow.protocol.BO;

import java.util.List;
import java.util.Map;

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


    public Map<Integer, Category> getCategoryDicts() {
        return categoryDicts;
    }

    public void setCategoryDicts(Map<Integer, Category> categoryDicts) {
        this.categoryDicts = categoryDicts;
    }

    public Map<Long, UserProfileDTO> getUserDicts() {
        return userDicts;
    }

    public void setUserDicts(Map<Long, UserProfileDTO> userDicts) {
        this.userDicts = userDicts;
    }

    public List<QunBO>getQunList() {
        return qunList;
    }

    public void setQunList(List<QunBO> qunList) {
        this.qunList = qunList;
    }
}
