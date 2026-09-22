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
@Table(name = "t_session_meta")
public class SessionMeta implements POJO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "int UNSIGNED")
    private Long id;

    @Column(name = "session_key", columnDefinition = "varchar(64) NOT NULL DEFAULT '' COMMENT '用户名'")
    private String sessionKey;

    @Column(name = "user_id", columnDefinition = "int(10) NOT NULL DEFAULT 0 COMMENT '用户ID'")
    private Long userId;

    @Column(name = "user_category", columnDefinition = "int(10) NOT NULL DEFAULT 0 COMMENT '用户分类")
    private Integer userCategory;

    @Column(name = "user_name", columnDefinition = "varchar(64) NOT NULL DEFAULT '' COMMENT '用户名'")
    private String userName;

    @Column(name = "user_nick_name", columnDefinition = "varchar(64) NOT NULL DEFAULT '' COMMENT '发送人昵称'")
    private String userNickName;

    @Column(name = "opposite_id", columnDefinition = "int(10) NOT NULL DEFAULT '' COMMENT '对方 ID'")
    private Long oppositeId;

    @Column(name = "opposite_category", columnDefinition = "int(10) NOT NULL DEFAULT 0 COMMENT '对方分类")
    private Integer oppositeCategory;

    @Column(name = "opposite_name", columnDefinition = "varchar(64) NOT NULL DEFAULT '' COMMENT '对方名'")
    private String oppositeName;

    @Column(name = "opposite_nick_name", columnDefinition = "varchar(64) NOT NULL DEFAULT '' COMMENT '对方昵称'")
    private String oppositeNickName;

    @Column(name = "is_visitor", columnDefinition = "tinyint(2) NOT NULL DEFAULT 0 COMMENT '是否临时会话 1 visitor, 0 register'")
    private Boolean isVisitor;

    @Column(name = "gmt_create", columnDefinition = "bigint UNSIGNED NOT NULL DEFAULT 0 COMMENT '创建时间'")
    private Long gmtCreate;

    @Column(name = "gmt_modified", columnDefinition = "bigint UNSIGNED NOT NULL DEFAULT 0 COMMENT '更新时间'")
    private Long gmtModified;

    @Column(name = "status", columnDefinition = "tinyint(2) NOT NULL DEFAULT 0 COMMENT '状态")
    private StatusRecord status;
}
