package com.sparrow.chat.protocol.params;

import lombok.Data;

@Data
public class SessionReadParams {
    private Integer chatType;
    private String sessionKey;
}
