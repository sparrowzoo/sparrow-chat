package com.sparrow.chat.contact.protocol.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("好友申请密文标识vo")
@Data
public class UserFriendApplyVO {
    @ApiModelProperty("用户密文标识")
    private String userSecretIdentify;
    @ApiModelProperty("用户头象")
    private String avatar;
    @ApiModelProperty("用户昵称")
    private String nickName;
}
