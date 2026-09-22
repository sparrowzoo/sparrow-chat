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

package com.sparrow.chat.im.po;

import com.sparrow.protocol.POJO;
import com.sparrow.protocol.enums.StatusRecord;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Table(name = "t_session")
public class Session implements POJO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "int UNSIGNED")
    private Long id;

    @Column(name = "user_id", columnDefinition = "int(11) NOT NULL DEFAULT 0 COMMENT '用户id'")
    private Long userId;

    /**
     * 只用于区分游客和注册用户，当注册用户类似改变时，不影响查询
     */
    @Column(name = "category", columnDefinition = "tinyint(2) NOT NULL DEFAULT 0 COMMENT '用户类型'")
    private Integer category;

    @Column(name = "session_key", columnDefinition = "varchar(64) NOT NULL DEFAULT '' COMMENT '会话标识键'")
    private String sessionKey;

    @Column(name = "chat_type", columnDefinition = "tinyint(2) NOT NULL DEFAULT 0 COMMENT '聊天类型 0 1v1 1 1vn'")
    private Integer chatType;

    @Column(name = "gmt_create", columnDefinition = "bigint UNSIGNED NOT NULL DEFAULT 0 COMMENT '创建时间'")
    private Long gmtCreate;

    @Column(name = "last_read_time", columnDefinition = "bigint UNSIGNED NOT NULL DEFAULT 0 COMMENT '最后阅读时间'")
    private Long lastReadTime;

    @Column(name = "sync_time", columnDefinition = "bigint  NOT NULL DEFAULT 0 COMMENT '同步时间'")
    private Long syncTime;

    @Column(name = "status", columnDefinition = "tinyint(1) NOT NULL DEFAULT 1 COMMENT '状态'")
    private StatusRecord status;
}
