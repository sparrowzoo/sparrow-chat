/*
Licensed to the Apache Software Foundation (ASF) under one or more
contributor license agreements.  See the NOTICE file distributed with
this work for additional information regarding copyright ownership.
The ASF licenses this file to You under the Apache License, Version 2.0
(the "License"); you may not use this file except in compliance with
the License.  You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package com.sparrow.chat.contact.assembler;

import com.sparrow.chat.contact.bo.QunDetailWrapBO;
import com.sparrow.chat.contact.bo.QunPlazaBO;
import com.sparrow.chat.contact.protocol.dto.QunDTO;
import com.sparrow.chat.contact.protocol.enums.Category;
import com.sparrow.chat.contact.protocol.enums.ContactError;
import com.sparrow.chat.contact.protocol.enums.Nationality;
import com.sparrow.chat.contact.protocol.vo.CategoryVO;
import com.sparrow.chat.contact.protocol.vo.QunPlazaVO;
import com.sparrow.chat.contact.protocol.vo.QunVO;
import com.sparrow.chat.contact.protocol.vo.QunWrapDetailVO;
import com.sparrow.exception.Asserts;
import com.sparrow.passport.protocol.dto.UserProfileDTO;
import com.sparrow.protocol.BeanCopier;
import com.sparrow.protocol.BusinessException;
import com.sparrow.utility.StringUtility;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Named
@Slf4j
public class QunAssembler {
    @Inject
    private UserAssembler userAssembler;
    @Inject
    private BeanCopier beanCopier;

    public QunWrapDetailVO assembleQunWrapDetail(QunDetailWrapBO qunDetailWrap) throws BusinessException {
        QunWrapDetailVO qunWrapDetailVO = new QunWrapDetailVO();
        qunWrapDetailVO.setDetail(this.assembleQun(qunDetailWrap.getQun(), qunDetailWrap.getUserMaps()));
        qunWrapDetailVO.setMembers(qunDetailWrap.getMembers());
        qunWrapDetailVO.setUserDicts(qunDetailWrap.getUserMaps());
        return qunWrapDetailVO;
    }

    public QunVO assembleQun(QunDTO qun, Map<Long, UserProfileDTO> userDicts) throws BusinessException {
        QunVO qunVo = new QunVO();
        this.beanCopier.copyProperties(qun, qunVo);
        qunVo.setQunId(qun.getId().toString());
        qunVo.setQunName(qun.getName());
        Nationality nationality = Nationality.getById(qun.getNationalityId());
        Asserts.isTrue(nationality == null, ContactError.NATIONALITY_OF_QUN_EMPTY);
        qunVo.setNationality(nationality.getName());
        UserProfileDTO qunOwner = userDicts.get(qun.getOwnerId());
        if (qunOwner != null) {
            qunVo.setOwnerName(qunOwner.getNickName());
            if (StringUtility.isNullOrEmpty(qunVo.getOwnerName())) {
                qunVo.setOwnerName(qunOwner.getUserName());
            }
        }
        return qunVo;
    }


    public QunPlazaVO assembleQunPlaza(QunPlazaBO qunPlaza) {
        QunPlazaVO qunPlazaVO = new QunPlazaVO();
        List<QunDTO> qunList = qunPlaza.getQunList();
        Map<Long, UserProfileDTO> userDicts = qunPlaza.getOwnerDicts();
        Map<Integer, Category> categories = qunPlaza.getCategoryDicts();
        Map<Integer, List<QunVO>> qunMap = new HashMap<>();
        for (QunDTO qunBO : qunList) {
            QunVO qunVO;
            try {
                qunVO = this.assembleQun(qunBO, userDicts);
            } catch (BusinessException e) {
                log.error("qun assemble error qunId:{},qunName:{}", qunBO.getId(), qunBO.getName(), e);
                continue;
            }
            if (!qunMap.containsKey(qunVO.getCategoryId())) {
                qunMap.put(qunVO.getCategoryId(), new ArrayList<>());
            }
            qunMap.get(qunVO.getCategoryId()).add(qunVO);
        }
        qunPlazaVO.setQunMap(qunMap);
        Map<Integer, CategoryVO> categoryVOMap = new HashMap<>();
        for (Integer categoryId : categories.keySet()) {
            categoryVOMap.put(categoryId, this.assembleCategory(categories.get(categoryId)));
        }
        qunPlazaVO.setCategoryDicts(categoryVOMap);
        return qunPlazaVO;
    }

    private CategoryVO assembleCategory(Category category) {
        CategoryVO categoryVO = new CategoryVO();
        categoryVO.setId(category.getId());
        categoryVO.setCategoryName(category.getName());
        categoryVO.setDescription(category.getDescription());
        return categoryVO;
    }
}
