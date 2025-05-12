package com.sparrow.chat.protocol.dto;

import com.sparrow.chat.contact.protocol.dto.QunDTO;
import com.sparrow.chat.protocol.constant.Chat;
import com.sparrow.passport.protocol.dto.UserProfileDTO;
import lombok.Data;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Data
public class HistoryMessageWrap {
    private List<MessageDTO> historyMessages;
    private Map<String, QunDTO> qunMaps;
    private Map<Long, UserProfileDTO> userMaps;

    public Set<String> qunIds() {
        Set<String> keys = new HashSet<>();
        for (MessageDTO messageDTO : this.historyMessages) {
            if(messageDTO.getChatType()== Chat.CHAT_TYPE_1_2_N) {
                keys.add(messageDTO.getSessionKey());
            }
        }
        return keys;
    }

    public Set<Long> userIds() {
        Set<Long> ids = new HashSet<>();
        for (MessageDTO messageDTO : this.historyMessages) {
            ids.add(Long.parseLong(messageDTO.getSender().getId()));
            if(messageDTO.getReceiver()!= null) {
                ids.add(Long.parseLong(messageDTO.getReceiver().getId()));
            }
        }
        return ids;
    }

}
