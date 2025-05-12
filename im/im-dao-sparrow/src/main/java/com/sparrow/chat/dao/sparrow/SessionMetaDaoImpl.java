package com.sparrow.chat.dao.sparrow;

import com.sparrow.chat.dao.sparrow.query.session.SessionDBQuery;
import com.sparrow.chat.im.po.SessionMeta;
import com.sparrow.orm.query.*;
import com.sparrow.orm.template.impl.ORMStrategy;
import com.sparrow.protocol.enums.StatusRecord;

import javax.inject.Named;
import java.util.List;
import java.util.Set;

@Named
public class SessionMetaDaoImpl extends ORMStrategy<SessionMeta, Long> implements SessionMetaDao {
    @Override
    public List<SessionMeta> querySession(SessionDBQuery sessionQuery) {

        SearchCriteria searchCriteria = new SearchCriteria();
        searchCriteria.setWhere(
                BooleanCriteria.criteria(
                                BooleanCriteria.criteria(
                                                Criteria.field(SessionMeta::getUserName).contains(sessionQuery.getUserName()))
                                        .or(Criteria.field(SessionMeta::getOppositeName).contains(sessionQuery.getUserName())))
                        .and(
                                BooleanCriteria.criteria(Criteria.field(SessionMeta::getUserNickName).contains(sessionQuery.getUserNickName()))
                                        .or(Criteria.field(SessionMeta::getOppositeNickName).contains(sessionQuery.getUserNickName())))
                        .and(
                                BooleanCriteria.criteria(Criteria.field(SessionMeta::getUserId).equal(sessionQuery.getUserId()))
                                        .or(Criteria.field(SessionMeta::getOppositeId).equal(sessionQuery.getUserId())))
                        .and(Criteria.field(SessionMeta::getGmtCreate).greaterThanEqual(sessionQuery.getBeginDate()))
                        .and(Criteria.field(SessionMeta::getGmtCreate).lessThanEqual(sessionQuery.getEndDate())));
        searchCriteria.addOrderCriteria(OrderCriteria.desc(SessionMeta::getGmtCreate));
        searchCriteria.setPageSize(100);
        return this.getList(searchCriteria);
    }

    @Override
    public SessionMeta exists(String sessionKey) {
        SearchCriteria searchCriteria = new SearchCriteria();
        searchCriteria.setWhere(Criteria.field(SessionMeta::getSessionKey).equal(sessionKey));
        return this.getEntity(searchCriteria);
    }

    public void disable(String sessionKey) {
        UpdateCriteria updateCriteria = new UpdateCriteria();
        updateCriteria.setWhere(Criteria.field(SessionMeta::getSessionKey).equal(sessionKey));
        updateCriteria.set(UpdateSetClausePair.field(SessionMeta::getStatus).equal(StatusRecord.DISABLE));
        this.update(updateCriteria);
    }

    @Override
    public List<SessionMeta> querySession(Set<String> sessionKeys) {
        SearchCriteria searchCriteria = new SearchCriteria();
        searchCriteria.setWhere(
                BooleanCriteria.criteria(Criteria.field(SessionMeta::getSessionKey).in(sessionKeys)).and(
                        Criteria.field(SessionMeta::getStatus).equal(StatusRecord.ENABLE)));
        return this.getList(searchCriteria);
    }
}
