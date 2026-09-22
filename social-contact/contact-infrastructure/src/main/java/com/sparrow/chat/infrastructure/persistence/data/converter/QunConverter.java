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

package com.sparrow.chat.infrastructure.persistence.data.converter;

import com.sparrow.chat.contact.bo.QunMemberBO;
import com.sparrow.chat.contact.po.Qun;
import com.sparrow.chat.contact.po.QunMember;
import com.sparrow.chat.contact.protocol.dto.QunDTO;
import com.sparrow.chat.contact.protocol.qun.QunCreateParam;
import com.sparrow.chat.contact.protocol.qun.QunModifyParam;
import com.sparrow.context.SessionContext;
import com.sparrow.protocol.BeanCopier;
import com.sparrow.protocol.LoginUser;
import com.sparrow.protocol.enums.StatusRecord;
import com.sparrow.utility.CollectionsUtility;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Named
public class QunConverter {
    @Inject
    private BeanCopier beanCopier;

    public Qun createParam2Po(QunCreateParam qunCreateParam) {
        Qun qun = new Qun();
        beanCopier.copyProperties(qunCreateParam, qun);
        LoginUser loginUser = SessionContext.getLoginUser();
        qun.setCreateUserId(loginUser.getUserId());
        qun.setModifiedUserId(loginUser.getUserId());
        qun.setGmtCreate(System.currentTimeMillis());
        qun.setCreateUserName(loginUser.getUserName());
        qun.setGmtModified(qun.getGmtCreate());
        qun.setModifiedUserName(loginUser.getUserName());
        qun.setOwnerId(loginUser.getUserId());
        qun.setStatus(StatusRecord.ENABLE);
        return qun;
    }


    public Qun modifyParam2Po(QunModifyParam qunModifyParam) {
        Qun qun = new Qun();
        beanCopier.copyProperties(qunModifyParam, qun);
        qun.setId(qunModifyParam.getQunId());
        LoginUser loginUser = SessionContext.getLoginUser();
        qun.setModifiedUserId(loginUser.getUserId());
        qun.setGmtModified(System.currentTimeMillis());
        qun.setModifiedUserName(loginUser.getUserName());
        qun.setStatus(StatusRecord.ENABLE);
        return qun;
    }

    public QunDTO po2Bo(Qun qun) {
        QunDTO qunDto = new QunDTO();
        beanCopier.copyProperties(qun, qunDto);
        return qunDto;
    }

    public List<QunMemberBO> membersPo2BoList(List<QunMember> members) {
        if (CollectionsUtility.isNullOrEmpty(members)) {
            return Collections.emptyList();
        }
        List<QunMemberBO> qunMemberBos = new ArrayList<>(members.size());
        for (QunMember qunMember : members) {
            QunMemberBO memberBO = new QunMemberBO();
            beanCopier.copyProperties(qunMember, memberBO);
            qunMemberBos.add(memberBO);
        }
        return qunMemberBos;
    }

    public List<QunDTO> poList2BoList(List<Qun> quns) {
        if (CollectionsUtility.isNullOrEmpty(quns)) {
            return Collections.emptyList();
        }
        List<QunDTO> qunBos = new ArrayList<>(quns.size());
        for (Qun qun : quns) {
            qunBos.add(this.po2Bo(qun));
        }
        return qunBos;
    }
}
