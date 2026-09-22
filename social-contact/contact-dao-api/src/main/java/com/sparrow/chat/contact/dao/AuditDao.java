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

package com.sparrow.chat.contact.dao;

import com.sparrow.chat.contact.po.Audit;
import com.sparrow.protocol.dao.DaoSupport;

import java.util.List;
import java.util.Set;

public interface AuditDao extends DaoSupport<Audit, Long> {
    List<Audit> getAuditingFriendList(Long userId);

    List<Audit> getAuditingQunMemberList(Long userId, Set<Long> qunIds);

    List<Audit> getMyApplingFriendList(Long userId);

    List<Audit> getMyApplingQunMemberList(Long userId);

    Audit exist(Audit audit);

    void  changeOwner(Long qunId, Long targetId);
}
