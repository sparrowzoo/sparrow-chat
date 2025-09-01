package com.sparrow.chat.dao.sparrow;

import com.sparrow.chat.contact.dao.AuditDao;
import com.sparrow.chat.contact.po.Audit;
import com.sparrow.chat.contact.protocol.enums.AuditBusiness;
import com.sparrow.orm.query.*;
import com.sparrow.orm.template.impl.ORMStrategy;
import com.sparrow.protocol.enums.StatusRecord;

import javax.inject.Named;
import java.util.List;
import java.util.Set;

@Named
public class AuditDaoImpl extends ORMStrategy<Audit, Long> implements AuditDao {

    @Override
    public List<Audit> getAuditingFriendList(Long userId) {
        SearchCriteria searchCriteria = new SearchCriteria();
        searchCriteria.setWhere(
                BooleanCriteria.criteria
                                (Criteria.field(Audit::getBusinessId).equal(userId))
                        .and(Criteria.field(Audit::getApplyTime).greaterThan(System.currentTimeMillis() - 1000L * 60 * 60 * 24 * 30))
                        .and
                                (Criteria.field(Audit::getBusinessType).equal(AuditBusiness.FRIEND.getBusiness())));
        return this.getList(searchCriteria);
    }

    @Override
    public List<Audit> getAuditingQunMemberList(Long userId, Set<Long> qunIds) {


        SearchCriteria searchCriteria = new SearchCriteria();
        searchCriteria.setWhere(
                BooleanCriteria.criteria
                                (Criteria.field(Audit::getBusinessId).in(qunIds))
                        .and(Criteria.field(Audit::getApplyTime).greaterThan(System.currentTimeMillis() - 1000L * 60 * 60 * 24 * 30))
                        .and
                                (Criteria.field(Audit::getBusinessType).equal(AuditBusiness.GROUP.getBusiness())));
        return this.getList(searchCriteria);
    }

    @Override
    public List<Audit> getMyApplingFriendList(Long userId) {
        SearchCriteria searchCriteria = new SearchCriteria();
        searchCriteria.setWhere(
                BooleanCriteria.criteria
                                (Criteria.field(Audit::getApplyUserId).equal(userId))
                        .and(Criteria.field(Audit::getApplyTime).greaterThan(System.currentTimeMillis() - 1000L * 60 * 60 * 24 * 30))

                        .and
                                (Criteria.field(Audit::getBusinessType).equal(AuditBusiness.FRIEND.getBusiness())));
        return this.getList(searchCriteria);
    }

    @Override
    public List<Audit> getMyApplingQunMemberList(Long userId) {
        SearchCriteria searchCriteria = new SearchCriteria();
        searchCriteria.setWhere(
                BooleanCriteria.criteria
                                (Criteria.field(Audit::getApplyUserId).equal(userId))
                        .and(Criteria.field(Audit::getApplyTime).greaterThan(System.currentTimeMillis() - 1000L * 60 * 60 * 24 * 30))
                        .and
                                (Criteria.field(Audit::getBusinessType).equal(AuditBusiness.GROUP.getBusiness())));
        return this.getList(searchCriteria);
    }

    @Override
    public Audit exist(Audit audit) {
        SearchCriteria searchCriteria = new SearchCriteria();
        searchCriteria.setWhere(
                BooleanCriteria.criteria
                                (Criteria.field(Audit::getBusinessId).equal(audit.getBusinessId()))
                        .and
                                (Criteria.field(Audit::getBusinessType).equal(audit.getBusinessType()))
                        .and
                                (Criteria.field(Audit::getApplyUserId).equal(audit.getApplyUserId())));
        return this.getEntity(searchCriteria);
    }

    @Override
    public void changeOwner(Long qunId, Long targetId) {
        UpdateCriteria updateCriteria = new UpdateCriteria();
        updateCriteria.set(UpdateSetClausePair.field(Audit::getAuditUserId).equal(targetId));
        updateCriteria.setWhere(BooleanCriteria.criteria(
                Criteria.field(Audit::getBusinessType).equal(AuditBusiness.GROUP.getBusiness()))
                .and(Criteria.field(Audit::getBusinessId).equal(qunId))
                .and(Criteria.field(Audit::getStatus).equal(StatusRecord.ENABLE)));
    }
}
