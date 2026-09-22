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

import com.sparrow.chat.contact.dao.CustomerServerDao;
import com.sparrow.chat.contact.dao.query.server.CountQuery;
import com.sparrow.chat.contact.dao.query.server.PagerServerQuery;
import com.sparrow.chat.contact.po.CustomerServer;
import com.sparrow.orm.query.BooleanCriteria;
import com.sparrow.orm.query.Criteria;
import com.sparrow.orm.query.SearchCriteria;
import com.sparrow.orm.template.impl.ORMStrategy;
import jakarta.inject.Named;

import java.util.List;

@Named
public class CustomerServerDaoImpl extends ORMStrategy<CustomerServer, Long> implements CustomerServerDao {
    private BooleanCriteria getCriteria(CountQuery pagerServiceQuery) {
        return BooleanCriteria.criteria(
                        Criteria.field(CustomerServer::getStatus).equal(pagerServiceQuery.getStatus()))

                .and(Criteria.field(CustomerServer::getServerId).equal(pagerServiceQuery.getCustomerServiceId()))

                .and(Criteria.field(CustomerServer::getServerName).contains(pagerServiceQuery.getCustomerServiceName()))

                .and(Criteria.field(CustomerServer::getTenantId).equal(pagerServiceQuery.getTenantId()));
    }

    @Override
    public List<CustomerServer> queryCustomerServer(PagerServerQuery pagerServerQuery) {
        BooleanCriteria criteria = getCriteria(pagerServerQuery);
        SearchCriteria searchCriteria = new SearchCriteria();
        searchCriteria.setWhere(criteria);
        searchCriteria.setPageSize(pagerServerQuery.getPagerQuery().getPageSize());
        searchCriteria.setPageNo(pagerServerQuery.getPagerQuery().getPageNo());
        return this.getList(searchCriteria);
    }

    @Override
    public Long countCustomerServer(CountQuery countQuery) {
        BooleanCriteria criteria = getCriteria(countQuery);
        SearchCriteria searchCriteria = new SearchCriteria();
        searchCriteria.setWhere(criteria);
        return this.getCount(searchCriteria);
    }

    @Override
    public List<CustomerServer> queryCustomerServicesByTenantId(String tenantId) {
        SearchCriteria searchCriteria = new SearchCriteria();
        searchCriteria.setWhere(Criteria.field(CustomerServer::getTenantId).equal(tenantId));
        return this.getList(searchCriteria);
    }
}
