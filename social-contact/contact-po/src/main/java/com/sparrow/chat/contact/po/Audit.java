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

package com.sparrow.chat.contact.po;

import com.sparrow.protocol.POJO;
import jakarta.persistence.*;
import lombok.Data;

@Table(name = "t_audit")
@Data
public class Audit implements POJO {
    /**
     * 主键 ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "int(11) UNSIGNED AUTO_INCREMENT")
    private Long id;
    /**
     * 申请人ID
     */
    @Column(name = "apply_user_id",
            columnDefinition = "int(11)  UNSIGNED DEFAULT 0 COMMENT '用户ID'",
            nullable = false,
            updatable = false)
    private Long applyUserId;
    /**
     * 业务类型  申请的群或者好友ID
     */
    @Column(
            name = "business_type",
            columnDefinition = "tinyint(1)  DEFAULT 0 COMMENT '业务类型'",
            nullable = false,
            updatable = false
    )
    private Integer businessType;
    /**
     * 业务ID  与业务类型对应
     * 如果是群，则为群ID
     * 如果是好友，则为好友ID
     */

    @Column(
            name = "business_id",
            columnDefinition = "int(11)  UNSIGNED DEFAULT 0 COMMENT '业务ID'",
            nullable = false,
            updatable = false
    )
    private Long businessId;
    /**
     * 申请时间
     */
    @Column(
            name = "apply_time",
            columnDefinition = "bigint(11)  DEFAULT 0 COMMENT '申请时间'",
            nullable = false
    )
    private Long applyTime;
    /**
     * 审核时间
     */

    @Column(
            name = "audit_time",
            columnDefinition = "bigint(11)  DEFAULT 0 COMMENT '审核时间'",
            nullable = false
    )
    private Long auditTime;
    /**
     * 审核人ID
     */
    @Column(
            name = "audit_user_id",
            columnDefinition = "int(11)  UNSIGNED DEFAULT 0 COMMENT '审核用户ID'",
            nullable = false
    )
    private Long auditUserId;
    /**
     * 申请的理由
     */
    @Column(name = "apply_reason", columnDefinition = "varchar(256)  DEFAULT '' COMMENT '申请理由'", nullable = false)
    private String applyReason;
    /**
     * 审核的理由
     */
    @Column(name = "audit_reason", columnDefinition = "varchar(256)  DEFAULT '' COMMENT '审核理由'", nullable = false)
    private String auditReason;
    /**
     * 审核的状态
     */
    @Column(
            name = "status",
            columnDefinition = "tinyint(1)  DEFAULT 0 COMMENT '审核状态'",
            nullable = false
    )
    private Integer status;

}
