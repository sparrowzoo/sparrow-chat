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

package com.sparrow.chat.protocol.constant;

import com.sparrow.messaging.protocol.MessageType;

/**
 * 业务消息类型，为前端选择展示组件和解释消息正文提供支持。
 *
 * <p>content 统一以 UTF-8 字符串传输，当前可以包含文本或 HTML
 * （例如 img 标签），未来也可以承载 JSON 等复杂数据结构。
 * 各业务类型可约定自己的正文结构；本枚举不表示字符编码或正文的二进制格式，
 * 不能据此将正文分流为 String 和 byte[]。混合图文也可以由同一正文承载。
 *
 * <p>新增展示方式时，按前后端约定扩展消息类型及其正文结构。
 * ID 在协议中占一个无符号字节，范围为 0–255；Java、DTO 和存储层使用整数 ID。
 * ID 必须显式定义并保持稳定，不得使用 ordinal()、重排已有 ID 或复用已有含义。
 * 新增类型无需改变正文的 UTF-8 编解码方式。
 */
public enum ChatMessageType implements MessageType {
    TEXT(0),
    HTML(1);

    ChatMessageType(Integer id) {
        this.id = id;
    }

    private final Integer id;

    @Override
    public Integer getIdentity() {
        return this.id;
    }
}
