package com.sparrow.chat.dao.sparrow;

import com.sparrow.chat.dao.sparrow.query.session.SessionDBQuery;
import com.sparrow.chat.im.po.Session;
import com.sparrow.chat.im.po.SessionMeta;
import com.sparrow.orm.query.BooleanCriteria;
import com.sparrow.orm.query.Criteria;
import com.sparrow.orm.query.SearchCriteria;
import com.sparrow.orm.template.impl.ORMStrategy;

import javax.inject.Named;
import java.util.List;

@Named
public class SessionMetaDaoImpl extends ORMStrategy<SessionMeta,Long> implements SessionMetaDao {
    @Override
    public List<SessionMeta> querySession(SessionDBQuery sessionQuery) {

            SearchCriteria searchCriteria=new SearchCriteria();
            searchCriteria.setWhere(
                    BooleanCriteria.criteria(
                                    BooleanCriteria.criteria(Criteria.field(SessionMeta::getSenderName).contains(sessionQuery.getSenderName()))
                                            .or(Criteria.field(SessionMeta::getReceiverName).contains(sessionQuery.getReceiverName()))
                                            .or(Criteria.field(SessionMeta::getSenderNickName).contains(sessionQuery.getSenderNickName()))
                                            .or(Criteria.field(SessionMeta::getReceiverNickName).contains(sessionQuery.getReceiverNickName()))
                                            .or(Criteria.field(SessionMeta::getGroupName).contains(sessionQuery.getGroupName())))
                            .and(BooleanCriteria.criteria(Criteria.field(Session::getGmtCreate).greaterThanEqual(sessionQuery.getBeginDate()))
                                    .and(Criteria.field(Session::getGmtCreate).lessThanEqual(sessionQuery.getEndDate()))));
            return this.getList(searchCriteria);
    }
}
