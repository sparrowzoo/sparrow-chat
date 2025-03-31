package com.sparrow.chat.protocol.dto;

import lombok.Data;

@Data
public class ContactStatusDTO {
    private String id;
    private int category;
    private boolean online;
    private long lastActiveTime;
}
