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

@Table(name = "t_qun_member")
@Data
public class QunMember implements POJO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "int(11) UNSIGNED")
    private Long id;
    @Column(name = "qun_id", columnDefinition = "int(11) UNSIGNED DEFAULT 0 COMMENT '群ID'", nullable = false, updatable = false)
    private Long qunId;
    @Column(name = "member_id", columnDefinition = "int(11) UNSIGNED  DEFAULT 0 COMMENT '群成员ID'", nullable = false, updatable = false)
    private Long memberId;
    @Column(name = "apply_time", columnDefinition = "bigint(11)  DEFAULT 0 COMMENT '申请时间'", nullable = false, updatable = false)
    private Long applyTime;
    @Column(name = "audit_time", columnDefinition = "bigint(11)  DEFAULT 0 COMMENT '审核时间'", nullable = false, updatable = false)
    private Long auditTime;
}
