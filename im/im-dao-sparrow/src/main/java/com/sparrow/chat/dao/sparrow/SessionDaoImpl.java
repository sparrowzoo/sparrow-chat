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

import com.sparrow.chat.im.po.Session;
import com.sparrow.orm.query.*;
import com.sparrow.orm.template.impl.ORMStrategy;
import com.sparrow.protocol.LoginUser;
import com.sparrow.protocol.enums.StatusRecord;
import jakarta.inject.Named;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Named
public class SessionDaoImpl extends ORMStrategy<Session, Long> implements SessionDao {
    private static final Logger log = LoggerFactory.getLogger(SessionDaoImpl.class);

    /**
     * 一对一聊天
     */
    public static final int CHAT_TYPE_1_2_1 = 0;

    public SessionDaoImpl() {
        log.info("sessionDaoImpl init");
    }

    @Override
    public List<Session> findByUser(String userId, Integer category) {
        SearchCriteria searchCriteria = new SearchCriteria();
        searchCriteria.setWhere(
                BooleanCriteria.criteria(Criteria.field(Session::getUserId).equal(Long.parseLong(userId)))
                        .and(Criteria.field(Session::getStatus).equal(StatusRecord.ENABLE)));
        if (category == LoginUser.CATEGORY_VISITOR) {
            searchCriteria.getWhere().and(Criteria.field(Session::getCategory).equal(LoginUser.CATEGORY_VISITOR));
        }
        searchCriteria.addOrderCriteria(OrderCriteria.desc(Session::getLastReadTime));
        return this.getList(searchCriteria);
    }

    @Override
    public Boolean exist(String userId, int category, String sessionKey) {
        SearchCriteria searchCriteria = new SearchCriteria();
        BooleanCriteria criteria =
                BooleanCriteria.criteria(Criteria.field(Session::getUserId).equal(Long.parseLong(userId)))
                        .and(Criteria.field(Session::getSessionKey).equal(sessionKey));
        if (category == LoginUser.CATEGORY_VISITOR) {
            criteria.and(Criteria.field(Session::getCategory).equal(LoginUser.CATEGORY_VISITOR));
        }
        searchCriteria.setWhere(criteria);
        Long count = this.getCount(searchCriteria);
        if (count > 0) {
            return true;
        }
        return false;
    }

    @Override
    public Boolean read(String userId, int category, String sessionKey) {
        UpdateCriteria updateCriteria = new UpdateCriteria();
        updateCriteria.set(UpdateSetClausePair.field(Session::getLastReadTime).equal(System.currentTimeMillis()));
        updateCriteria.setWhere(
                BooleanCriteria.criteria(Criteria.field(Session::getUserId).equal(Long.parseLong(userId)))
                        .and(Criteria.field(Session::getSessionKey).equal(sessionKey)));
        if (category == LoginUser.CATEGORY_VISITOR) {
            updateCriteria.getWhere().and(Criteria.field(Session::getCategory).equal(LoginUser.CATEGORY_VISITOR));
        }
        this.update(updateCriteria);
        return true;
    }

    @Override
    public List<Session> fetchUnSyncSessions(int limit) {
        long hourBefore = System.currentTimeMillis() - 8 * 60 * 60 * 1000;
        SearchCriteria searchCriteria = new SearchCriteria();
        searchCriteria.setWhere(
                BooleanCriteria.criteria(Criteria.field(Session::getSyncTime).lessThan(hourBefore))
                        .and(Criteria.field(Session::getStatus).equal(StatusRecord.ENABLE))
                        .and(Criteria.field(Session::getChatType).equal(CHAT_TYPE_1_2_1)));
        searchCriteria.setPageSize(limit);
        return this.getList(searchCriteria);
    }
}
