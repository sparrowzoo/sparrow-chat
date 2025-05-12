package com.sparrow.chat.contact.bo;

import com.sparrow.chat.contact.protocol.dto.QunDTO;
import com.sparrow.passport.protocol.dto.UserProfileDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
public class QunDetailWrapBO {
    private QunDTO qun;
    private List<QunMemberBO> members;
    private Map<Long, UserProfileDTO> userMaps;
}
