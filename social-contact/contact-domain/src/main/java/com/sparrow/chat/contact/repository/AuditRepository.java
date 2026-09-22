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
import com.sparrow.chat.contact.bo.AuditWrapBO;
import com.sparrow.chat.contact.bo.FriendApplyBO;
import com.sparrow.chat.contact.protocol.audit.FriendAuditParam;
import com.sparrow.chat.contact.protocol.audit.JoinQunParam;
import com.sparrow.chat.contact.protocol.audit.QunAuditParam;


public interface AuditRepository {
    Long applyFriend(FriendApplyBO friendApply);

    AuditWrapBO getFriendList(Long userId);


    AuditWrapBO getQunMemberList(Long userId);

    Long joinQun(JoinQunParam joinQun);


    Integer auditFriend(AuditBO auditBO, FriendAuditParam friendAuditParam);

    Integer auditQun(AuditBO auditBO, QunAuditParam friendAuditParam);

    AuditBO getAudit(Long auditId);

    void  changeOwner(Long qunId, Long userId);
}
