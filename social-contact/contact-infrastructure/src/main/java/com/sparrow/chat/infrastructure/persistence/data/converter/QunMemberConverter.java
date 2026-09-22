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

import com.sparrow.chat.contact.bo.AuditBO;
import com.sparrow.chat.contact.po.QunMember;
import com.sparrow.context.SessionContext;
import com.sparrow.protocol.LoginUser;
import jakarta.inject.Named;

@Named
public class QunMemberConverter {
    public QunMember convert2QunMember(AuditBO auditBo) {
        QunMember qunMember = new QunMember();
        qunMember.setQunId(auditBo.getBusinessId());
        qunMember.setMemberId(auditBo.getApplyUserId());
        qunMember.setAuditTime(System.currentTimeMillis());
        qunMember.setApplyTime(auditBo.getApplyTime());
        return qunMember;
    }

    public QunMember convert2QunMember(Long qunId) {
        LoginUser loginUser = SessionContext.getLoginUser();
        QunMember qunMember = new QunMember();
        qunMember.setQunId(qunId);
        qunMember.setMemberId(loginUser.getUserId());
        qunMember.setAuditTime(System.currentTimeMillis());
        qunMember.setApplyTime(System.currentTimeMillis());
        return qunMember;
    }
}
