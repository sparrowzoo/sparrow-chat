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

package com.sparrow.chat.infrastructure.persistence.data.converter;

import com.sparrow.chat.contact.bo.CustomerServerBO;
import com.sparrow.chat.contact.dao.query.server.CountQuery;
import com.sparrow.chat.contact.dao.query.server.PagerServerQuery;
import com.sparrow.chat.contact.po.CustomerServer;
import com.sparrow.chat.contact.protocol.query.CustomerServerQuery;
import com.sparrow.protocol.BeanCopier;
import com.sparrow.protocol.dao.DatabasePagerQuery;
import com.sparrow.utility.CollectionsUtility;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Named
public class CustomerServiceConverter {
    @Inject
    private BeanCopier beanCopier;
    public PagerServerQuery toDbPagerQuery(CustomerServerQuery customerServiceQuery) {
        if (customerServiceQuery == null) {
            return new PagerServerQuery();
        }
        PagerServerQuery pagerServiceQuery = new PagerServerQuery();
        this.beanCopier.copyProperties(customerServiceQuery, pagerServiceQuery);
        pagerServiceQuery.setPagerQuery(new DatabasePagerQuery(customerServiceQuery));
        return pagerServiceQuery;
    }

    public CountQuery toCountQuery(CustomerServerQuery customerServiceQuery) {
        if (customerServiceQuery == null) {
            return new CountQuery();
        }
        CountQuery countAppQuery = new CountQuery();
        this.beanCopier.copyProperties(customerServiceQuery, countAppQuery);
        return countAppQuery;
    }

    public CustomerServerBO toBO(CustomerServer customerService) {
        if (customerService == null) {
            new CustomerServerBO();
        }
        CustomerServerBO customerServiceBO = new CustomerServerBO();
        this.beanCopier.copyProperties(customerService, customerServiceBO);
        return customerServiceBO;
    }

    public List<CustomerServerBO> toBOS(List<CustomerServer> customerServices) {
        if (CollectionsUtility.isNullOrEmpty(customerServices)) {
            return Collections.emptyList();
        }
        List<CustomerServerBO> customerServiceBOS = new ArrayList<>(customerServices.size());
        for (CustomerServer customerService : customerServices) {
            customerServiceBOS.add(toBO(customerService));
        }
        return customerServiceBOS;
    }

    public CustomerServer toPO(CustomerServerBO customerServiceBO) {
        if (customerServiceBO == null) {
            return new CustomerServer();
        }
        CustomerServer customerService = new CustomerServer();
        this.beanCopier.copyProperties(customerServiceBO, customerService);
        return customerService;
    }
}
