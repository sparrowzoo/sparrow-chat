package com.sparrow.chat.contact.protocol.vo;

import com.sparrow.chat.contact.bo.QunMemberBO;
import com.sparrow.passport.protocol.dto.UserProfileDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

@ApiModel("群详情包装类")
@Data
public class QunWrapDetailVO {
    @ApiModelProperty("群详情")
    private QunVO detail;
    @ApiModelProperty("群成员列表")
    private List<QunMemberBO> members;
    @ApiModelProperty("用户字典")
    private Map<Long, UserProfileDTO> userDicts;
}
