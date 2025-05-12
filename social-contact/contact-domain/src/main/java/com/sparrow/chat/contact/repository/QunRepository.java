package com.sparrow.chat.contact.repository;

import com.sparrow.chat.contact.bo.AuditBO;
import com.sparrow.chat.contact.bo.QunMemberBO;
import com.sparrow.chat.contact.protocol.dto.QunDTO;
import com.sparrow.chat.contact.protocol.qun.QunCreateParam;
import com.sparrow.chat.contact.protocol.qun.QunModifyParam;
import com.sparrow.chat.contact.protocol.qun.RemoveMemberOfQunParam;
import com.sparrow.protocol.BusinessException;

import java.util.List;
import java.util.Map;

public interface QunRepository {

    Long joinQun(AuditBO auditBo);

    void removeMember(RemoveMemberOfQunParam removeMemberOfQunParam) throws BusinessException;

    void dissolve(Long qunId);

    Boolean isMember(Long qunId, Long newOwnerId);


    void transfer(QunDTO newQun, Long newOwnerId) throws BusinessException;

    Long createQun(QunCreateParam qunCreateParam);

    void modifyQun(QunModifyParam qunModifyParam) throws BusinessException;

    QunDTO qunDetail(Long qunId) throws BusinessException;


    List<QunMemberBO> qunMembers(Long qunId) throws BusinessException;


    List<QunDTO> queryQunPlaza();

    List<QunDTO> getMyQunList();

    Map<Long,QunDTO> getQunList(List<Long> qunIds);

}
