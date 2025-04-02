package com.sparrow.chat.infrastructure.persistence;

import com.sparrow.chat.contact.bo.AuditBO;
import com.sparrow.chat.contact.bo.QunBO;
import com.sparrow.chat.contact.bo.QunMemberBO;
import com.sparrow.chat.contact.dao.QunDao;
import com.sparrow.chat.contact.dao.QunMemberDao;
import com.sparrow.chat.contact.po.Qun;
import com.sparrow.chat.contact.po.QunMember;
import com.sparrow.chat.contact.protocol.enums.ContactError;
import com.sparrow.chat.contact.protocol.qun.QunCreateParam;
import com.sparrow.chat.contact.protocol.qun.QunModifyParam;
import com.sparrow.chat.contact.protocol.qun.RemoveMemberOfQunParam;
import com.sparrow.chat.contact.repository.QunRepository;
import com.sparrow.chat.infrastructure.persistence.data.converter.QunConverter;
import com.sparrow.chat.infrastructure.persistence.data.converter.QunMemberConverter;
import com.sparrow.exception.Asserts;
import com.sparrow.protocol.BusinessException;
import com.sparrow.protocol.LoginUser;
import com.sparrow.protocol.ThreadContext;
import com.sparrow.protocol.enums.StatusRecord;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    public void transfer(QunBO qunBO, Long newOwnerId) throws BusinessException {
        this.qunDao.transfer(qunBO.getId(), newOwnerId);
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
    public QunBO qunDetail(Long qunId) throws BusinessException {
        Qun qun = this.qunDao.getEntity(qunId);
        return this.qunConverter.po2Bo(qun);
    }

    @Override
    public List<QunMemberBO> qunMembers(Long qunId) throws BusinessException {
        List<QunMember> qunMembers = this.qunMemberDao.members(qunId);
        return this.qunConverter.membersPo2BoList(qunMembers);
    }

    @Override
    public List<QunBO> queryQunPlaza(Long categoryId) {
        List<Qun> quns = this.qunDao.queryQunList(categoryId);
        return this.qunConverter.poList2BoList(quns);
    }

    @Override
    public List<QunBO> queryQunPlaza() {
        List<Qun> quns = this.qunDao.queryEnabledQunList();
        return this.qunConverter.poList2BoList(quns);
    }

    @Override
    public List<QunBO> getMyQunList() {
        LoginUser loginUser = ThreadContext.getLoginToken();
        Map<Long, Long> myQunIds = this.qunMemberDao.getQunsByMemberId(loginUser.getUserId());
        if (myQunIds == null || myQunIds.isEmpty()) {
            return new ArrayList<>();
        }
        List<Qun> myQuns = this.qunDao.getQuns(myQunIds.values());
        return this.qunConverter.poList2BoList(myQuns);
    }
}
