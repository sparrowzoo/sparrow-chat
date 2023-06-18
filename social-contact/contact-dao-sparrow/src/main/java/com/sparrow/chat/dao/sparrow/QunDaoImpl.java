package com.sparrow.chat.dao.sparrow;

import com.sparrow.chat.contact.dao.QunDao;
import com.sparrow.chat.contact.po.Qun;
import com.sparrow.orm.query.BooleanCriteria;
import com.sparrow.orm.query.Criteria;
import com.sparrow.orm.query.SearchCriteria;
import com.sparrow.orm.template.impl.ORMStrategy;
import com.sparrow.protocol.enums.StatusRecord;

import javax.inject.Named;
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
}
