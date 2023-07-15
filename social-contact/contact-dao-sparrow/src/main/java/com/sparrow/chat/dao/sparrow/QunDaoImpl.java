package com.sparrow.chat.dao.sparrow;

import com.sparrow.chat.contact.dao.QunDao;
import com.sparrow.chat.contact.po.Qun;
import com.sparrow.orm.query.*;
import com.sparrow.orm.template.impl.ORMStrategy;
import com.sparrow.protocol.enums.StatusRecord;

import javax.inject.Named;
import java.util.Collection;
import java.util.List;

@Named
public class QunDaoImpl extends ORMStrategy<Qun, Long> implements QunDao {
    @Override
    public List<Qun> queryQunList(Long category) {
        SearchCriteria searchCriteria = new SearchCriteria();
        searchCriteria.setWhere(
                BooleanCriteria.criteria
                                (Criteria.field("qun.categoryId").equal(category))
                        .and
                                (Criteria.field("qun.status").equal(StatusRecord.ENABLE)));
        return this.getList(searchCriteria);
    }

    @Override
    public List<Qun> queryEnabledQunList() {
        SearchCriteria searchCriteria = new SearchCriteria();
        searchCriteria.setWhere(Criteria.field("qun.status").equal(StatusRecord.ENABLE));
        return this.getList(searchCriteria);
    }

    @Override
    public void transfer(Long qunId, Long newOwnerId) {
        UpdateCriteria updateCriteria = new UpdateCriteria();
        updateCriteria.set(UpdateSetClausePair.field("qun.id").equal(qunId));
        updateCriteria.setWhere(Criteria.field("qun.ownerId").equal(newOwnerId));
        this.update(updateCriteria);
    }

    @Override
    public List<Qun> getQuns(Collection<Long> qunIds) {
        SearchCriteria searchCriteria = new SearchCriteria();
        searchCriteria.setWhere(Criteria.field("qun.id").in(qunIds));
        return this.getList(searchCriteria);
    }
}
