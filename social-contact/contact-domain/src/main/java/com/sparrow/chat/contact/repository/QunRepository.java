package com.sparrow.chat.contact.repository;

import com.sparrow.chat.contact.bo.AuditBO;
import com.sparrow.chat.contact.bo.QunBO;
import com.sparrow.chat.contact.bo.QunMemberBO;
import com.sparrow.chat.contact.protocol.qun.QunCreateParam;
import com.sparrow.chat.contact.protocol.qun.QunModifyParam;
import com.sparrow.chat.contact.protocol.qun.RemoveMemberOfQunParam;
import com.sparrow.protocol.BusinessException;

import java.util.List;

public interface QunRepository {

    Long joinQun(AuditBO auditBo);

    void removeMember(RemoveMemberOfQunParam removeMemberOfQunParam) throws BusinessException;

    void dissolve(Long qunId);


    Boolean isMember(Long qunId, Long newOwnerId);


    void transfer(QunBO newQun, Long newOwnerId) throws BusinessException;

    Long createQun(QunCreateParam qunCreateParam);

    void modifyQun(QunModifyParam qunModifyParam) throws BusinessException;

    QunBO qunDetail(Long qunId) throws BusinessException;


    List<QunMemberBO> qunMembers(Long qunId) throws BusinessException;


    List<QunBO> queryQunPlaza(Long categoryId);

    List<QunBO> queryQunPlaza();

    List<QunBO> getMyQunList();

}
