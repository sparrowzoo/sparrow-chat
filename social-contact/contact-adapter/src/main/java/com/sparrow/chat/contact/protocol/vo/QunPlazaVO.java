package com.sparrow.chat.contact.protocol.vo;

import com.sparrow.protocol.VO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

@ApiModel("群广场")
@Data
public class QunPlazaVO implements VO {

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
    private Map<Integer, List<QunVO>> qunMap;
}
