package com.sparrow.chat.infrastructure.persistence;

import com.sparrow.chat.dao.AuditDAO;
import com.sparrow.chat.dao.ContactDAO;
import com.sparrow.chat.enums.AuditStatus;
import com.sparrow.chat.infrastructure.converter.AuditConverter;
import com.sparrow.chat.infrastructure.converter.ContactConverter;
import com.sparrow.chat.po.Audit;
import com.sparrow.chat.po.Contact;
import com.sparrow.chat.protocol.audit.FriendApplyParam;
import com.sparrow.chat.protocol.audit.FriendAuditParam;
import com.sparrow.chat.protocol.audit.QunApplyParam;
import com.sparrow.chat.repository.AuditRepository;
import com.sparrow.chat.repository.ContactRepository;
import javax.inject.Inject;
import javax.inject.Named;

@Named
public class AuditRepositoryImpl implements AuditRepository {
    @Inject
    private AuditDAO auditDao;
    @Inject
    private ContactDAO contactDao;
    @Inject
    private AuditConverter auditConverter;
    @Inject
    private ContactConverter contactConverter;
    @Inject
    private ContactRepository contactRepository;

    @Override public Long applyFriend(Long currentUserId, FriendApplyParam friendApplyParam) {
        Audit audit = this.auditConverter.applyFriend2Po(currentUserId, friendApplyParam);
        return this.auditDao.insert(audit);
    }

    @Override public Long applyQun(Long currentUserId, QunApplyParam qunApplyParam) {
        Audit audit = this.auditConverter.applyQun2Po(currentUserId, qunApplyParam);
        return this.auditDao.insert(audit);
    }

    @Override public Integer auditFriend(Audit audit, FriendAuditParam friendAuditParam) {
        audit.setStatus(friendAuditParam.getAgree() ? AuditStatus.PASS : AuditStatus.REJECT);
        audit.setAuditReason(friendAuditParam.getReason());
        //todo 应该加分布式事务 rocket mq 解耦 后续迭代
        //更新审核状态
        Integer count = this.auditDao.update(audit);
        //添加当前联系人
        Contact currentContact = this.contactConverter.toCurrentContact(audit);
        this.contactDao.insert(currentContact);

        //添加对方联系人
        Contact friendContact = this.contactConverter.toFriendContact(audit);
        this.contactDao.insert(friendContact);

        //更新双方联系人缓存
        //清空能保证一致
        this.contactRepository.clearContactCache(audit.getUserId().intValue());
        this.contactRepository.clearContactCache(audit.getBusinessId().intValue());
        return count;
    }

    public Audit getAudit(Long auditId) {
        return this.auditDao.getEntity(auditId);
    }
}
