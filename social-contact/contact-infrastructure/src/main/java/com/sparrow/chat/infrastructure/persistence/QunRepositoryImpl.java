package com.sparrow.chat.infrastructure.persistence;

import com.sparrow.chat.contact.bo.AuditBO;
import com.sparrow.chat.contact.bo.QunMemberBO;
import com.sparrow.chat.contact.dao.QunDao;
import com.sparrow.chat.contact.dao.QunMemberDao;
import com.sparrow.chat.contact.po.Qun;
import com.sparrow.chat.contact.po.QunMember;
import com.sparrow.chat.contact.protocol.dto.QunDTO;
import com.sparrow.chat.contact.protocol.enums.ContactError;
import com.sparrow.chat.contact.protocol.qun.QunCreateParam;
import com.sparrow.chat.contact.protocol.qun.QunModifyParam;
import com.sparrow.chat.contact.protocol.qun.RemoveMemberOfQunParam;
import com.sparrow.chat.contact.repository.QunRepository;
import com.sparrow.chat.infrastructure.persistence.data.converter.QunConverter;
import com.sparrow.chat.infrastructure.persistence.data.converter.QunMemberConverter;
import com.sparrow.context.SessionContext;
import com.sparrow.exception.Asserts;
import com.sparrow.protocol.BusinessException;
import com.sparrow.protocol.LoginUser;
import com.sparrow.protocol.enums.StatusRecord;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.*;

@Named
public class QunRepositoryImpl implements QunRepository {
    @Inject
    private QunDao qunDao;

    @Inject
    private QunMemberDao qunMemberDao;

    @Inject
    private QunConverter qunConverter;

    @Inject
    private QunMemberConverter qunMemberConverter;

    @Override
    public Long joinQun(AuditBO qunAuditBo) {
        QunMember qunMember = this.qunMemberConverter.convert2QunMember(qunAuditBo);
        this.qunMemberDao.insert(qunMember);
        return qunMember.getId();
    }

    @Override
    public void removeMember(RemoveMemberOfQunParam removeMemberOfQunParam) {
        this.qunMemberDao.removeMember(removeMemberOfQunParam.getQunId(), removeMemberOfQunParam.getMemberId());
    }

    @Override
    public void dissolve(Long qunId) {
        this.qunDao.delete(qunId);
        this.qunMemberDao.dissolve(qunId);
    }

    @Override
    public Boolean isMember(Long qunId, Long memberId) {
        return this.qunMemberDao.isMember(qunId, memberId);
    }


    @Override
    public void transfer(QunDTO qunDTO, Long newOwnerId) throws BusinessException {
        this.qunDao.transfer(qunDTO.getId(), newOwnerId);
    }


    @Override
    public Long createQun(QunCreateParam qunCreateParam) {
        Qun qun = this.qunConverter.createParam2Po(qunCreateParam);
        Long qunId = this.qunDao.insert(qun);
        QunMember qunMember = this.qunMemberConverter.convert2QunMember(qunId);
        this.qunMemberDao.insert(qunMember);
        return qunId;
    }

    @Override
    public void modifyQun(QunModifyParam qunModifyParam) throws BusinessException {
        Qun oldQun = this.qunDao.getEntity(qunModifyParam.getQunId());
        Asserts.isTrue(null == oldQun, ContactError.QUN_NOT_FOUND);
        Asserts.isTrue(StatusRecord.ENABLE != oldQun.getStatus(), ContactError.QUN_STATUS_INVALID);
        Qun qun = this.qunConverter.modifyParam2Po(qunModifyParam);
        this.qunDao.update(qun);
    }

    @Override
    public QunDTO qunDetail(Long qunId) throws BusinessException {
        Qun qun = this.qunDao.getEntity(qunId);
        return this.qunConverter.po2Bo(qun);
    }

    @Override
    public List<QunMemberBO> qunMembers(Long qunId) throws BusinessException {
        List<QunMember> qunMembers = this.qunMemberDao.members(qunId);
        return this.qunConverter.membersPo2BoList(qunMembers);
    }

    @Override
    public List<QunDTO> queryQunPlaza() {
        List<Qun> quns = this.qunDao.queryEnabledQunList();
        return this.qunConverter.poList2BoList(quns);
    }

    @Override
    public List<QunDTO> getMyQunList() {
        LoginUser loginUser = SessionContext.getLoginUser();
        Set<Long> myQunIds = this.qunMemberDao.getQunsByMemberId(loginUser.getUserId());
        if (myQunIds == null || myQunIds.isEmpty()) {
            return new ArrayList<>();
        }
        List<Qun> myQuns = this.qunDao.getQuns(myQunIds);
        return this.qunConverter.poList2BoList(myQuns);
    }

    @Override
    public Map<Long,QunDTO> getQunList(List<Long> qunIds) {
        if(qunIds == null || qunIds.isEmpty()){
            return new HashMap<>();
        }
        List<Qun> myQuns = this.qunDao.getQuns(qunIds);
        Map<Long, QunDTO> qunMap = new HashMap<>();
        List<QunDTO> qunBos= this.qunConverter.poList2BoList(myQuns);
        for(QunDTO qunBO : qunBos){
            qunMap.put(qunBO.getId(),qunBO);
        }
        return qunMap;
    }
}
