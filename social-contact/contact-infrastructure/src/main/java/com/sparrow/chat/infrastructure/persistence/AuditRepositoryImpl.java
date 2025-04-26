package com.sparrow.chat.infrastructure.persistence;

import com.sparrow.chat.contact.bo.AuditBO;
import com.sparrow.chat.contact.bo.AuditWrapBO;
import com.sparrow.chat.contact.bo.FriendApplyBO;
import com.sparrow.chat.contact.dao.AuditDao;
import com.sparrow.chat.contact.po.Audit;
import com.sparrow.chat.contact.protocol.audit.FriendAuditParam;
import com.sparrow.chat.contact.protocol.audit.JoinQunParam;
import com.sparrow.chat.contact.protocol.audit.QunAuditParam;
import com.sparrow.chat.contact.repository.AuditRepository;
import com.sparrow.chat.infrastructure.persistence.data.converter.AuditConverter;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;

@Named
public class AuditRepositoryImpl implements AuditRepository {
    @Inject
    private AuditDao auditDao;
    @Inject
    private AuditConverter auditConverter;

    @Override
    public Long applyFriend(FriendApplyBO friendApply) {
        Audit audit = this.auditConverter.friendApply2AuditPo(friendApply);
        Audit oldAudit = this.auditDao.exist(audit);
        if (oldAudit != null) {
            audit.setId(oldAudit.getId());
            return (long) this.auditDao.update(audit);
        }
        return this.auditDao.insert(audit);
    }

    @Override
    public AuditWrapBO getFriendList(Long userId) {
        List<Audit> auditingFriends = this.auditDao.getAuditingFriendList(userId);
        List<Audit> myApplingFriendList = this.auditDao.getMyApplingFriendList(userId);
        List<AuditBO> auditingBOList= this.auditConverter.auditList2AuditBOList(auditingFriends);
        List<AuditBO> myApplingFriendBOList=this.auditConverter.auditList2AuditBOList(myApplingFriendList);
        return new AuditWrapBO(auditingBOList, myApplingFriendBOList);
    }

    @Override
    public AuditWrapBO getQunMemberList(Long userId) {
        List<Audit> auditingQunMembers = this.auditDao.getAuditingQunMemberList(userId);
        List<Audit> myApplingQunMemberList = this.auditDao.getMyApplingQunMemberList(userId);
        List<AuditBO> auditingQunMemberBOList= this.auditConverter.auditList2AuditBOList(auditingQunMembers);
        List<AuditBO> myApplingQunMemberBOList=this.auditConverter.auditList2AuditBOList(myApplingQunMemberList);
        return new AuditWrapBO(auditingQunMemberBOList, myApplingQunMemberBOList);
    }

    @Override
    public Long joinQun(JoinQunParam joinQun) {
        Audit audit = this.auditConverter.joinQun2AuditPo(joinQun);
        Audit oldAudit = this.auditDao.exist(audit);
        if (oldAudit != null) {
            audit.setId(oldAudit.getId());
            return (long) this.auditDao.update(audit);
        }
        return this.auditDao.insert(audit);
    }

    @Override
    public Integer auditFriend(AuditBO auditBO, FriendAuditParam friendAuditParam) {
        Audit audit = this.auditConverter.convert2po(auditBO, friendAuditParam);
        return this.auditDao.update(audit);
    }

    @Override
    public Integer auditQun(AuditBO auditBO, QunAuditParam qunAuditParam) {
        Audit audit = this.auditConverter.convert2po(auditBO, qunAuditParam);
        return this.auditDao.update(audit);
    }

    @Override
    public AuditBO getAudit(Long auditId) {
        Audit audit = this.auditDao.getEntity(auditId);
        return this.auditConverter.audit2AuditBO(audit);
    }

    @Override
    public void changeOwner(Long qunId, Long userId) {
        this.auditDao.changeOwner(qunId, userId);
    }
}
