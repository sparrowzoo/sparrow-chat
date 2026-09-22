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

package com.sparrow.chat.contact.repository;

import com.sparrow.chat.contact.bo.AuditBO;
import com.sparrow.chat.contact.bo.QunMemberBO;
import com.sparrow.chat.contact.protocol.dto.QunDTO;
import com.sparrow.chat.contact.protocol.qun.QunCreateParam;
import com.sparrow.chat.contact.protocol.qun.QunModifyParam;
import com.sparrow.chat.contact.protocol.qun.RemoveMemberOfQunParam;
import com.sparrow.protocol.BusinessException;

import java.util.List;
import java.util.Map;

public interface QunRepository {

    Long joinQun(AuditBO auditBo);

    void removeMember(RemoveMemberOfQunParam removeMemberOfQunParam) throws BusinessException;

    void dissolve(Long qunId);

    Boolean isMember(Long qunId, Long newOwnerId);


    void transfer(QunDTO newQun, Long newOwnerId) throws BusinessException;

    Long createQun(QunCreateParam qunCreateParam);

    void modifyQun(QunModifyParam qunModifyParam) throws BusinessException;

    QunDTO qunDetail(Long qunId) throws BusinessException;


    List<QunMemberBO> qunMembers(Long qunId) throws BusinessException;


    List<QunDTO> queryQunPlaza();

    List<QunDTO> getMyQunList();

    Map<Long,QunDTO> getQunList(List<Long> qunIds);

}
