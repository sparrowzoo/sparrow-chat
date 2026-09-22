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

package com.sparrow.chat.contact.protocol.enums;

import java.util.HashMap;
import java.util.Map;

public enum Category {

    J2SE_BASIC(101, "JAVA 基础", "JAVA 0基础入门"),
    J2SE_ADVANCE(102, "JAVA 进阶", "JAVA  高级api"),
    JAVA_WEB_FRAMEWORK(103, "JAVA 框架", "JAVA web 框架"),
    JAVA_MIDDLEWARE(104, "JAVA 中间件", "java 中间件");


    private Integer id;
    private String name;

    private String description;

    Category(Integer id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public static Category getById(Integer id) {
        for (Category category : Category.values()) {
            if (category.getId().equals(id)) {
                return category;
            }
        }
        return null;
    }

    public static Map<Integer, Category> getMap() {
        Map<Integer, Category> categories = new HashMap<>();
        for (Category category : Category.values()) {
            categories.put(category.getId(), category);
        }
        return categories;
    }
}
