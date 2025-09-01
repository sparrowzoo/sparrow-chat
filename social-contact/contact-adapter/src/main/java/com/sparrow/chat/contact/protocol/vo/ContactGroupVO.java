package com.sparrow.chat.contact.protocol.vo;

import com.sparrow.chat.contact.protocol.dto.FriendDetailDTO;
import com.sparrow.protocol.DTO;
import lombok.Data;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
public class ContactGroupVO implements DTO {
    public ContactGroupVO(Map<Long, ContactVO> userMap, List<QunVO> quns, List<FriendDetailDTO> friendDetails) {
        this.userMap = userMap;
        this.quns = quns;
        if (friendDetails == null) {
            this.contactIds = Collections.emptyList();
        } else {
            this.contactIds = friendDetails.stream().map(FriendDetailDTO::getFriendId).collect(Collectors.toList());
        }
        if (userMap == null) {
            this.userMap = Collections.emptyMap();
        }
    }

    private Map<Long, ContactVO> userMap;
    private List<QunVO> quns;
    //todo 这里的contactIds应该是Map  key是contactId value是 addTime
    private Collection<Long> contactIds;
}
