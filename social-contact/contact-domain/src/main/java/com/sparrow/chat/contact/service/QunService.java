package com.sparrow.chat.contact.service;

import com.sparrow.chat.contact.bo.QunBO;
import com.sparrow.chat.contact.bo.QunDetailWrapBO;
import com.sparrow.chat.contact.bo.QunPlazaBO;
import com.sparrow.chat.contact.protocol.enums.Category;
import com.sparrow.chat.contact.protocol.enums.ContactError;
import com.sparrow.chat.contact.protocol.qun.QunCreateParam;
import com.sparrow.chat.contact.protocol.qun.QunModifyParam;
import com.sparrow.chat.contact.repository.QunRepository;
import com.sparrow.exception.Asserts;
import com.sparrow.passport.api.UserProfileAppService;
import com.sparrow.passport.protocol.dto.UserProfileDTO;
import com.sparrow.protocol.BusinessException;
import com.sparrow.utility.StringUtility;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.*;

@Named
public class QunService {
    @Inject
    private QunRepository qunRepository;

    @Inject
    private UserProfileAppService userProfileAppService;

    public Long createQun(QunCreateParam qunCreateParam) throws BusinessException {
        Asserts.isTrue(StringUtility.isNullOrEmpty(qunCreateParam.getName()), ContactError.QUN_NAME_IS_EMPTY);
        Asserts.isTrue(null == qunCreateParam.getCategoryId(), ContactError.CATEGORY_OF_QUN_EMPTY);
        Asserts.isTrue(null == qunCreateParam.getNationalityId(), ContactError.NATIONALITY_OF_QUN_EMPTY);
        return this.qunRepository.createQun(qunCreateParam);
    }

    public void modify(QunModifyParam qunModifyParam) throws BusinessException {
        Asserts.isTrue(null == qunModifyParam.getQunId(), ContactError.QUN_ID_IS_EMPTY);
        Asserts.isTrue(StringUtility.isNullOrEmpty(qunModifyParam.getName()), ContactError.QUN_NAME_IS_EMPTY);
        this.qunRepository.modifyQun(qunModifyParam);
    }

    public QunDetailWrapBO qunDetail(Long qunId) throws BusinessException {
        QunBO qunBo = this.qunRepository.qunDetail(qunId);
        UserProfileDTO owner = this.userProfileAppService.getUser(qunBo.getOwnerId());
        return new QunDetailWrapBO(qunBo, owner);
    }


    private QunPlazaBO wrapQunPlaza(List<QunBO> qunBOS) throws BusinessException {
        QunPlazaBO qunPlaza = new QunPlazaBO();
        Set<Long> userIds = new HashSet<>();
        //Set<Long> categories = new HashSet<>();
        for (QunBO qun : qunBOS) {
            userIds.add(qun.getOwnerId());
            //categories.add(qun.getCategoryId());
        }
        Map<Long, UserProfileDTO> userProfileMap = this.userProfileAppService.getUserMap(userIds);
        qunPlaza.setUserDicts(userProfileMap);
        qunPlaza.setCategoryDicts(Category.getMap());
        qunPlaza.setQunList(qunBOS);
        return qunPlaza;
    }

    public QunPlazaBO qunPlaza(Long categoryId) throws BusinessException {
        List<QunBO> qunBOs = this.qunRepository.queryQunPlaza(categoryId);
        return this.wrapQunPlaza(qunBOs);
    }

    public QunPlazaBO qunPlaza() throws BusinessException {
        List<QunBO> qunBOs = this.qunRepository.queryQunPlaza();
        return this.wrapQunPlaza(qunBOs);
    }
}
