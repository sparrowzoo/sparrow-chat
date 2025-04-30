package com.sparrow.chat.contact.protocol.vo;

import com.sparrow.protocol.VO;
import lombok.Data;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Data
public class ContactGroupVO implements VO {
    public ContactGroupVO(Map<Long, ContactVO> userMap, List<QunVO> quns, List<Long> contactIds) {
        this.userMap = userMap;
        this.quns = quns;
        this.contactIds = contactIds;
        if (contactIds == null) {
            this.contactIds = Collections.emptyList();
        }
        if (userMap == null) {
            this.userMap = Collections.emptyMap();
        }
    }

    private Map<Long, ContactVO> userMap;
    private List<QunVO> quns;
    private List<Long> contactIds;
}
