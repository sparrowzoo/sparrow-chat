package com.sparrow.chat.dao.sparrow;

import com.sparrow.chat.dao.sparrow.query.session.MessageDBQuery;
import com.sparrow.chat.im.po.Message;
import com.sparrow.orm.query.BooleanCriteria;
import com.sparrow.orm.query.Criteria;
import com.sparrow.orm.query.OrderCriteria;
import com.sparrow.orm.query.SearchCriteria;
import com.sparrow.orm.template.impl.ORMStrategy;

import javax.inject.Named;
import java.util.List;

@Named
public class MessageDaoImpl extends ORMStrategy<Message, Long> implements MessageDao {
    @Override
    public List<Message> getHistoryMessage(MessageDBQuery messageQuery) {
        SearchCriteria searchCriteria = new SearchCriteria();
        searchCriteria.setPageSize(30);
        BooleanCriteria booleanCriteria = BooleanCriteria.criteria
                        (Criteria.field(Message::getSessionKey).equal(messageQuery.getSessionKey()))
                .and(Criteria.field(Message::getContent).contains(messageQuery.getContent()))
                .and(Criteria.field(Message::getServerTime).greaterThanEqual(messageQuery.getBeginDate()))
                .and(Criteria.field(Message::getServerTime).lessThanEqual(messageQuery.getEndDate()));

        if (messageQuery.getLastMessageId() > 0) {
            booleanCriteria.and(Criteria.field(Message::getId).lessThan(messageQuery.getLastMessageId()));
        }
        searchCriteria.setWhere(booleanCriteria);
        searchCriteria.addOrderCriteria(OrderCriteria.desc(Message::getId));
        return this.getList(searchCriteria);
    }
}
