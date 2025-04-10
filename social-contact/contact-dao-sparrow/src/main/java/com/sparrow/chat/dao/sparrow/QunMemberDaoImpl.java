package com.sparrow.chat.dao.sparrow;

import com.sparrow.chat.contact.dao.QunMemberDao;
import com.sparrow.chat.contact.po.QunMember;
import com.sparrow.orm.query.BooleanCriteria;
import com.sparrow.orm.query.Criteria;
import com.sparrow.orm.query.OrderCriteria;
import com.sparrow.orm.query.SearchCriteria;
import com.sparrow.orm.template.impl.ORMStrategy;

import javax.inject.Named;
import java.util.List;
import java.util.Set;

@Named
public class QunMemberDaoImpl extends ORMStrategy<QunMember, Long> implements QunMemberDao {
    @Override
    public void removeMember(Long qunId, Long memberId) {
        SearchCriteria searchCriteria = new SearchCriteria();
        searchCriteria.setWhere(
                BooleanCriteria.criteria
                                (Criteria.field("qunMember.qunId").equal(qunId))
                        .and
                                (Criteria.field("qunMember.memberId").equal(memberId)));
        this.delete(searchCriteria);
    }

    @Override
    public Boolean isMember(Long qunId, Long memberId) {
        SearchCriteria searchCriteria = new SearchCriteria();
        searchCriteria.setWhere(
                BooleanCriteria.criteria
                                (Criteria.field("qunMember.qunId").equal(qunId))
                        .and
                                (Criteria.field("qunMember.memberId").equal(memberId)));
        return this.getCount(searchCriteria) > 0;
    }

    @Override
    public List<QunMember> members(Long qunId) {
        SearchCriteria searchCriteria = new SearchCriteria();
        searchCriteria.setWhere(
                BooleanCriteria.criteria
                        (Criteria.field("qunMember.qunId").equal(qunId)));
        return this.getList(searchCriteria);
    }

    public void dissolve(Long qunId) {
        SearchCriteria searchCriteria = new SearchCriteria();
        searchCriteria.setWhere(
                BooleanCriteria.criteria
                        (Criteria.field("qunMember.qunId").equal(qunId)));
        this.delete(searchCriteria);
    }

    @Override
    public Set<Long> getQunsByMemberId(Long memberId) {
        SearchCriteria searchCriteria = new SearchCriteria();
        searchCriteria.setFields("qunMember.qunId");
        searchCriteria.setWhere(
                BooleanCriteria.criteria
                        (Criteria.field(QunMember::getMemberId).equal(memberId)));
        searchCriteria.addOrderCriteria(OrderCriteria.asc("qunMember.applyTime"));
        return this.firstList(searchCriteria);
    }
}
