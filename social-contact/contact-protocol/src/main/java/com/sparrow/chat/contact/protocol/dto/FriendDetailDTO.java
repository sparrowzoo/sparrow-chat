package com.sparrow.chat.contact.protocol.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FriendDetailDTO {
    private Long friendId;
    private Long userId;
    private Long addTime;
}
