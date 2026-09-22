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

@Data
@Table(name = "t_customer_service")
public class CustomerServer extends PO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "int(11) UNSIGNED ")
    private Long id;

    @Column(name = "tenant_id", columnDefinition = "varchar(64) NOT NULL DEFAULT '' comment '租户标识'")
    private String tenantId;

    @Column(name = "category", columnDefinition = "int(11)  NOT NULL DEFAULT '0' comment '租户类型 平台/中介'")
    private Integer category;

    @Column(name = "server_id", columnDefinition = "int(11)  NOT NULL DEFAULT '0' comment '客服ID'")
    private Integer serverId;

    @Column(name = "server_name", columnDefinition = "varchar(64) NOT NULL DEFAULT '' comment '客服名称'")
    private String serverName;

    @Column(name = "status", columnDefinition = "tinyint(1) NOT NULL DEFAULT '0' comment '状态'")
    private StatusRecord status;
}
