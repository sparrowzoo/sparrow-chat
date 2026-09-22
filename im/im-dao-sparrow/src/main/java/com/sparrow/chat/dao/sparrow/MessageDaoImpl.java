/*
Licensed to the Apache Software Foundation (ASF) under one or more
contributor license agreements.  See the NOTICE file distributed with
this work for additional information regarding copyright ownership.
The ASF licenses this file to You under the Apache License, Version 2.0
(the "License"); you may not use this file except in compliance with
the License.  You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package com.sparrow.chat.dao.sparrow;

import com.sparrow.chat.dao.sparrow.query.session.MessageDBQuery;
import com.sparrow.chat.im.po.Message;
import com.sparrow.orm.query.BooleanCriteria;
import com.sparrow.orm.query.Criteria;
import com.sparrow.orm.query.OrderCriteria;
import com.sparrow.orm.query.SearchCriteria;
import com.sparrow.orm.template.impl.ORMStrategy;
import jakarta.inject.Named;

import java.util.List;

@Named
public class MessageDaoImpl extends ORMStrategy<Message, Long> implements MessageDao {
    @Override
    public List<Message> getHistoryMessage(MessageDBQuery messageQuery) {
        SearchCriteria searchCriteria = new SearchCriteria();
        searchCriteria.setPageSize(30);
        BooleanCriteria booleanCriteria = BooleanCriteria.criteria(Criteria.field(Message::getSessionKey).equal(messageQuery.getSessionKey()))
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
