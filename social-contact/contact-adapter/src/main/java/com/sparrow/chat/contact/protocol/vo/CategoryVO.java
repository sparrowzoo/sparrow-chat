package com.sparrow.chat.contact.protocol.vo;

import com.sparrow.protocol.DTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("类别")
@Data
public class CategoryVO implements DTO {

    @ApiModelProperty("类别ID")
    private Integer id;
    @ApiModelProperty("类别名称")
    private String categoryName;
    @ApiModelProperty("类别描述")
    private String description;
}
