package com.sparrow.chat.contact.protocol.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("类别")
public class CategoryVO {

    @ApiModelProperty("类别ID")
    private Integer id;
    @ApiModelProperty("类别名称")
    private String categoryName;
    @ApiModelProperty("类别描述")
    private String description;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
