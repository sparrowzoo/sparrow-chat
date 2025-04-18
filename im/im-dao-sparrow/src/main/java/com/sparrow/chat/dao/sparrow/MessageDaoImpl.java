package com.sparrow.chat.dao.sparrow;

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
    public List<Message> getHistoryMessage(String session, long lastTime) {
        SearchCriteria searchCriteria = new SearchCriteria();
        searchCriteria.setPageSize(100);
        searchCriteria.setWhere(
                BooleanCriteria.criteria
                                (Criteria.field(Message::getSessionKey).equal(session))
                        .and
                                (Criteria.field(Message::getServerTime).greaterThan(lastTime)));

        searchCriteria.addOrderCriteria(OrderCriteria.asc(Message::getServerTime));
        return this.getList(searchCriteria);
    }
}
