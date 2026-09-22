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

import com.sparrow.protocol.dao.PO;
import com.sparrow.protocol.enums.StatusRecord;
import jakarta.persistence.*;
import lombok.Data;

@Table(name = "t_qun")
@Data
public class Qun extends PO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "int(11) UNSIGNED ")
    private Long id;
    @Column(name = "name", columnDefinition = "varchar(32)  DEFAULT '' COMMENT '群名称'", nullable = false)
    private String name;
    @Column(name = "avatar", columnDefinition = "varchar(256)  DEFAULT '' COMMENT '群头象'", nullable = false)
    private String avatar;
    @Column(name = "announcement", columnDefinition = "varchar(255)  DEFAULT '' COMMENT '群公告'", nullable = false)
    private String announcement;
    @Column(name = "nationality_id", columnDefinition = "int(11)  DEFAULT 0 COMMENT '国籍id'", nullable = false, updatable = false)
    private Integer nationalityId;
    @Column(name = "organization_id", columnDefinition = "int(11) UNSIGNED  DEFAULT 0 COMMENT '机构ID'", nullable = false, updatable = false)
    private Long organizationId;
    @Column(name = "owner_id", columnDefinition = "int(11) UNSIGNED  DEFAULT 0 COMMENT '群主'", nullable = false, updatable = false)
    private Long ownerId;
    @Column(name = "category_id", columnDefinition = "int(11) UNSIGNED  DEFAULT 0 COMMENT '所属类别'", nullable = false, updatable = false)
    private Integer categoryId;
    @Column(name = "remark", columnDefinition = "varchar(255)  DEFAULT '' COMMENT '备注'", nullable = false)
    private String remark;
    @Column(name = "status", columnDefinition = "tinyint(2)  DEFAULT 1 COMMENT '状态'", nullable = false)
    private StatusRecord status;
}
