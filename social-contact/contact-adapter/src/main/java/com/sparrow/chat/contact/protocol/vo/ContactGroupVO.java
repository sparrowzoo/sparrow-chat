package com.sparrow.chat.contact.protocol.vo;

import com.sparrow.protocol.VO;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
public class ContactGroupVO implements VO {
    private Map<Long, ContactVO> userMap;
    private List<QunVO> quns;
    private List<Long> contactIds;
}
