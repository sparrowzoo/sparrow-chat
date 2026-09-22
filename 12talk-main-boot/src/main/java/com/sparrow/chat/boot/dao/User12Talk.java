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

package com.sparrow.chat.boot.dao;

import com.sparrow.protocol.POJO;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Table(name = "user")
public class User12Talk implements POJO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "account_code", unique = true)
    private String accountCode;
    @Column(name = "manager_id")
    private Long managerId;
    @Column(name = "password")
    private String password;
    @Column(name = "type")
    private String type;
    @Column(name = "name")
    private String name;
    @Column(name = "en_name")
    private String enName;
    @Column(name = "email")
    private String email;
    @Column(name = "remark")
    private String remark;
    @Column(name = "enabled")
    private Integer enabled;
    @Column(name = "deleted")
    private Integer deleted;
}
