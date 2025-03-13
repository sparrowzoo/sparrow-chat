package com.sparrowzoo.chat.dao.sparrow;

import com.sparrow.orm.query.BooleanCriteria;
import com.sparrow.orm.query.Criteria;
import com.sparrow.orm.query.SearchCriteria;
import com.sparrow.orm.template.impl.ORMStrategy;
import com.sparrowzoo.chat.dao.sparrow.dao.SessionDao;
import com.sparrowzoo.chat.dao.sparrow.dao.po.Session;

import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@Named
public class SessionDaoImpl extends ORMStrategy<Session, Long> implements SessionDao {
    @Override
    public List<Session> findById(String userId, Integer category) {
        SearchCriteria searchCriteria = new SearchCriteria();
        searchCriteria.setWhere(
                BooleanCriteria.criteria
                                (Criteria.field("session.userId").equal(userId))
                        .and
                                (Criteria.field("session.category").equal(category)));
        return this.getList(searchCriteria);

    }
}
