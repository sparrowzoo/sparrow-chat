package com.sparrowzoo.chat.dao.sparrow;

import com.sparrow.orm.query.*;
import com.sparrow.orm.template.impl.ORMStrategy;
import com.sparrowzoo.chat.dao.sparrow.dao.SessionDao;
import com.sparrowzoo.chat.dao.sparrow.dao.po.Session;

import javax.inject.Named;
import java.util.List;

@Named
public class SessionDaoImpl extends ORMStrategy<Session, Long> implements SessionDao {
    @Override
    public List<Session> findById(String userId, Integer category) {
        SearchCriteria searchCriteria = new SearchCriteria();
        searchCriteria.setWhere(
                BooleanCriteria.criteria
                                (Criteria.field(Session::getUserId).equal(userId))
                        .and
                                (Criteria.field(Session::getCategory).equal(category)));
        return this.getList(searchCriteria);

    }

    @Override
    public Boolean read(String userId, Integer category, String sessionKey) {
        UpdateCriteria updateCriteria = new UpdateCriteria();
        updateCriteria.set(UpdateSetClausePair.field(Session::getLastReadTime).equal(System.currentTimeMillis()));
        updateCriteria.setWhere(
                BooleanCriteria.criteria
                                (Criteria.field(Session::getUserId).equal(userId))
                        .and
                                (Criteria.field(Session::getCategory).equal(category))
                        .and
                                (Criteria.field(Session::getSessionKey).equal(sessionKey)));
        this.update(updateCriteria);
        return true;
    }
}
