package com.sparrow.chat.contact.protocol.vo;

import com.sparrow.protocol.VO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("群详情")
public class QunVO implements VO {
    @ApiModelProperty("群ID")
    private String qunId;
    @ApiModelProperty("群名称")
    private String qunName;
    @ApiModelProperty("群公告")
    private String announcement;
    @ApiModelProperty("所属国籍")
    private String nationality;
    @ApiModelProperty("备注")
    private String remark;
    @ApiModelProperty("群主ID")
    private Long ownerId;
    @ApiModelProperty("群主名")
    private String ownerName;

    @ApiModelProperty("所属类型id")
    private Integer categoryId;
    @ApiModelProperty("所属类型名")
    private String categoryName;
    @ApiModelProperty("图标")
    private String avatar;
}
