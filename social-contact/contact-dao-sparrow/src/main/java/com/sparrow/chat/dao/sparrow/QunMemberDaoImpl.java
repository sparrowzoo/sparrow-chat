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

import com.sparrow.chat.contact.dao.QunMemberDao;
import com.sparrow.chat.contact.po.QunMember;
import com.sparrow.orm.query.BooleanCriteria;
import com.sparrow.orm.query.Criteria;
import com.sparrow.orm.query.OrderCriteria;
import com.sparrow.orm.query.SearchCriteria;
import com.sparrow.orm.template.impl.ORMStrategy;
import jakarta.inject.Named;

import java.util.List;
import java.util.Set;

@Named
public class QunMemberDaoImpl extends ORMStrategy<QunMember, Long> implements QunMemberDao {
    @Override
    public void removeMember(Long qunId, Long memberId) {
        SearchCriteria searchCriteria = new SearchCriteria();
        searchCriteria.setWhere(
                BooleanCriteria.criteria(Criteria.field("qunMember.qunId").equal(qunId))
                        .and(Criteria.field("qunMember.memberId").equal(memberId)));
        this.delete(searchCriteria);
    }

    @Override
    public Boolean isMember(Long qunId, Long memberId) {
        SearchCriteria searchCriteria = new SearchCriteria();
        searchCriteria.setWhere(
                BooleanCriteria.criteria(Criteria.field("qunMember.qunId").equal(qunId))
                        .and(Criteria.field("qunMember.memberId").equal(memberId)));
        return this.getCount(searchCriteria) > 0;
    }

    @Override
    public List<QunMember> members(Long qunId) {
        SearchCriteria searchCriteria = new SearchCriteria();
        searchCriteria.setWhere(
                BooleanCriteria.criteria(Criteria.field("qunMember.qunId").equal(qunId)));
        return this.getList(searchCriteria);
    }

    public void dissolve(Long qunId) {
        SearchCriteria searchCriteria = new SearchCriteria();
        searchCriteria.setWhere(
                BooleanCriteria.criteria(Criteria.field("qunMember.qunId").equal(qunId)));
        this.delete(searchCriteria);
    }

    @Override
    public Set<Long> getQunsByMemberId(Long memberId) {
        SearchCriteria searchCriteria = new SearchCriteria();
        searchCriteria.setFields("qunMember.qunId");
        searchCriteria.setWhere(
                BooleanCriteria.criteria(Criteria.field(QunMember::getMemberId).equal(memberId)));
        searchCriteria.addOrderCriteria(OrderCriteria.asc("qunMember.applyTime"));
        return this.firstList(searchCriteria);
    }
}
