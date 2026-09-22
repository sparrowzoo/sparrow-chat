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

import com.sparrow.chat.contact.dao.QunDao;
import com.sparrow.chat.contact.po.Qun;
import com.sparrow.orm.query.*;
import com.sparrow.orm.template.impl.ORMStrategy;
import com.sparrow.protocol.enums.StatusRecord;
import jakarta.inject.Named;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Named
public class QunDaoImpl extends ORMStrategy<Qun, Long> implements QunDao {
    @Override
    public List<Qun> queryQunList(Long category) {
        SearchCriteria searchCriteria = new SearchCriteria();
        searchCriteria.setWhere(
                BooleanCriteria.criteria(Criteria.field("qun.categoryId").equal(category))
                        .and(Criteria.field("qun.status").equal(StatusRecord.ENABLE)));
        return this.getList(searchCriteria);
    }

    @Override
    public List<Qun> queryEnabledQunList() {
        SearchCriteria searchCriteria = new SearchCriteria();
        searchCriteria.setWhere(Criteria.field("qun.status").equal(StatusRecord.ENABLE));
        return this.getList(searchCriteria);
    }

    @Override
    public void transfer(Long qunId, Long newOwnerId) {
        UpdateCriteria updateCriteria = new UpdateCriteria();
        updateCriteria.set(UpdateSetClausePair.field("qun.id").equal(qunId));
        updateCriteria.setWhere(Criteria.field("qun.ownerId").equal(newOwnerId));
        this.update(updateCriteria);
    }

    @Override
    public List<Qun> getQuns(Collection<Long> qunIds) {
        SearchCriteria searchCriteria = new SearchCriteria();
        searchCriteria.setWhere(Criteria.field("qun.id").in(qunIds));
        return this.getList(searchCriteria);
    }

    @Override
    public Set<Long> getQunIdsByOwner(Long userId) {
        SearchCriteria searchCriteria = new SearchCriteria();
        searchCriteria.setFields("qun.id");
        searchCriteria.setWhere(Criteria.field(Qun::getOwnerId).equal(userId));
        return this.firstList(searchCriteria);
    }
}
