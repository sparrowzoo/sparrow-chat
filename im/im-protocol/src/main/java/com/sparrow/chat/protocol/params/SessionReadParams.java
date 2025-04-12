package com.sparrow.chat.protocol.params;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SessionReadParams {
    private String sessionKey;
}
