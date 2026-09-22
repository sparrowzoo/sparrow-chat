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

package com.sparrow.chat.contact.protocol.vo;

import com.sparrow.protocol.DTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "审核VO", description = "审核信息")
public class AuditVO implements DTO {
    @Schema(name = "审核ID", description = "审核ID")
    private Long auditId;
    @Schema(name = "业务类型", description = "业务类型 1:好友申请 2:群申请")
    private Integer auditBusiness;
    @Schema(name = "业务ID", description = "业务ID  与业务类型对应 如果是群，则为群ID 如果是好友，则为好友ID")
    /**
     * 业务ID  与业务类型对应
     * 如果是群，则为群ID
     * 如果是好友，则为好友ID
     */
    private Long businessId;
    @Schema(name = "申请人ID", description = "申请人ID")
    /**
     * 申请人ID
     */
    private Long applyUserId;
    @Schema(name = "申请时间", description = "申请时间")
    /**
     * 申请时间
     */
    private Long applyTime;
    @Schema(name = "审核时间", description = "审核时间")
    /**
     * 审核时间
     */
    private Long auditTime;

    @Schema(name = "审核人ID", description = "审核人ID")
    /**
     * 审核人ID
     */
    private Long auditUserId;

    @Schema(name = "申请的理由", description = "申请的理由")
    /**
     * 申请的理由
     */
    private String applyReason;

    @Schema(name = "审核的理由", description = "审核的理由")
    /**
     * 审核的理由
     */
    private String auditReason;

    @Schema(name = "审核的状态", description = "审核的状态 0:待审核 1:通过 2:拒绝")
    /**
     * 审核的状态
     */
    private Integer status;
}
