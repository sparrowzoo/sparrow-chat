package com.sparrow.chat.dao.sparrow;

import com.sparrow.chat.contact.dao.AuditDao;
import com.sparrow.chat.contact.po.Audit;
import com.sparrow.chat.contact.protocol.enums.AuditBusiness;
import com.sparrow.orm.query.BooleanCriteria;
import com.sparrow.orm.query.Criteria;
import com.sparrow.orm.query.SearchCriteria;
import com.sparrow.orm.template.impl.ORMStrategy;

import javax.inject.Named;
import java.util.List;

@Named
public class AuditDaoImpl extends ORMStrategy<Audit, Long> implements AuditDao {
    @Override
    public List<Audit> getAudits(Long userId) {
        SearchCriteria searchCriteria = new SearchCriteria();
        searchCriteria.setWhere(
                BooleanCriteria.criteria
                                (Criteria.field("audit.businessId").equal(userId))
                        .and
                                (Criteria.field("audit.businessType").equal(AuditBusiness.FRIEND.getBusiness())));
        return this.getList(searchCriteria);
    }

    @Override
    public Audit exist(Audit audit) {
        SearchCriteria searchCriteria = new SearchCriteria();
        searchCriteria.setWhere(
                BooleanCriteria.criteria
                                (Criteria.field("audit.businessId").equal(audit.getBusinessId()))
                        .and
                                (Criteria.field("audit.businessType").equal(AuditBusiness.FRIEND.getBusiness()))
                        .and
                                (Criteria.field("audit.applyUserId").equal(audit.getApplyUserId())));
        return this.getEntity(searchCriteria);
    }
}
