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

package com.sparrow.chat.contact.rpc;

import com.sparrow.chat.contact.QunServiceApi;
import com.sparrow.chat.contact.bo.QunMemberBO;
import com.sparrow.chat.contact.service.QunService;
import com.sparrow.protocol.BusinessException;
import com.sparrow.utility.CollectionsUtility;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
@Slf4j
public class QunRpcService implements QunServiceApi {
    public QunRpcService() {
        log.info("QunRpcService init");
    }

    @Inject
    private QunService qunService;

    @Override
    public List<Long> getMemberById(Long qunId) throws BusinessException {
        List<QunMemberBO> memberBos = this.qunService.getMemberIdsById(qunId);
        if (CollectionsUtility.isNullOrEmpty(memberBos)) {
            return Collections.emptyList();
        }
        List<Long> memberIds = new ArrayList<>(memberBos.size());
        for (QunMemberBO memberBO : memberBos) {
            memberIds.add(memberBO.getMemberId());
        }
        return memberIds;
    }
}
