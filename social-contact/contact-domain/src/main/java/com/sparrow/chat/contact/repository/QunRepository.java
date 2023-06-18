package com.sparrow.chat.contact.repository;

import com.sparrow.chat.contact.bo.QunBO;
import com.sparrow.chat.contact.protocol.qun.QunCreateParam;
import com.sparrow.chat.contact.protocol.qun.QunModifyParam;
import com.sparrow.protocol.BusinessException;

import java.util.List;

public interface QunRepository {
    Long createQun(QunCreateParam qunCreateParam);

    void modifyQun(QunModifyParam qunModifyParam) throws BusinessException;

    QunBO qunDetail(Long qunId) throws BusinessException;

    List<QunBO> queryQunPlaza(Long categoryId);

    List<QunBO> queryQunPlaza();

}
