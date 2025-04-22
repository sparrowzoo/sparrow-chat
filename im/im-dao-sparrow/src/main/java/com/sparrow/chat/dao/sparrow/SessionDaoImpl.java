package com.sparrow.chat.dao.sparrow;

import com.sparrow.chat.im.po.Session;
import com.sparrow.orm.query.*;
import com.sparrow.orm.template.impl.ORMStrategy;

import javax.inject.Named;
import java.util.List;

@Named
public class SessionDaoImpl extends ORMStrategy<Session, Long> implements SessionDao {
    public SessionDaoImpl() {
        System.out.println("sessionDaoImpl init");
    }

    @Override
    public List<Session> findByUser(String userId, Integer category) {
        long hourBefore24=System.currentTimeMillis()-8*60*60*1000;
        SearchCriteria searchCriteria = new SearchCriteria();
        searchCriteria.setWhere(
                BooleanCriteria.criteria
                                (Criteria.field(Session::getUserId).equal(userId))
                        .and
                                (Criteria.field(Session::getCategory).equal(category))
                        .and(Criteria.field(Session::getGmtCreate).greaterThan(hourBefore24)));
        searchCriteria.addOrderCriteria(OrderCriteria.desc(Session::getLastReadTime));
        return this.getList(searchCriteria);
    }

    @Override
    public Boolean exist(String userId, Integer category, String sessionKey) {
        SearchCriteria searchCriteria = new SearchCriteria();
        searchCriteria.setWhere(
                BooleanCriteria.criteria
                                (Criteria.field(Session::getUserId).equal(userId))
                        .and
                                (Criteria.field(Session::getCategory).equal(category))
                        .and
                                (Criteria.field(Session::getSessionKey).equal(sessionKey)));
        Long count = this.getCount(searchCriteria);
        if (count > 0) {
            return true;
        }
        return false;
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
