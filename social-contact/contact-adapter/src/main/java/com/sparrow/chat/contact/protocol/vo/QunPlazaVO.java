package com.sparrow.chat.contact.protocol.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;
import java.util.Map;

@ApiModel("群广场")
public class QunPlazaVO {

    /**
     * key:categoryId
     * value:类别
     */
    @ApiModelProperty("类别字典")
    private Map<Integer, CategoryVO> categoryDicts;

    /**
     * key:categoryId
     * value:qun 列表
     */
    @ApiModelProperty("群分类列表")
    private Map<Long, List<QunVO>> qunMap;

    public Map<Integer, CategoryVO> getCategoryDicts() {
        return categoryDicts;
    }

    public void setCategoryDicts(Map<Integer, CategoryVO> categoryDicts) {
        this.categoryDicts = categoryDicts;
    }

    public Map<Long, List<QunVO>> getQunMap() {
        return qunMap;
    }

    public void setQunMap(Map<Long, List<QunVO>> qunMap) {
        this.qunMap = qunMap;
    }
}
