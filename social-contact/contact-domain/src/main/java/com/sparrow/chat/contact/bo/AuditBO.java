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

package com.sparrow.chat.contact.bo;

import com.sparrow.chat.contact.protocol.enums.AuditBusiness;
import com.sparrow.protocol.BO;
import com.sparrow.protocol.enums.StatusRecord;
import lombok.Data;

@Data
public class AuditBO implements BO {
    private Long auditId;
    private AuditBusiness auditBusiness;
    /**
     * 业务ID  与业务类型对应
     * 如果是群，则为群ID
     * 如果是好友，则为好友ID
     */
    private Long businessId;
    /**
     * 申请人ID
     */
    private Long applyUserId;
    /**
     * 申请时间
     */
    private Long applyTime;
    /**
     * 审核时间
     */
    private Long auditTime;
    /**
     * 审核人ID
     */
    private Long auditUserId;
    /**
     * 申请的理由
     */
    private String applyReason;
    /**
     * 审核的理由
     */
    private String auditReason;
    /**
     * 审核的状态
     */
    private StatusRecord status;

}
