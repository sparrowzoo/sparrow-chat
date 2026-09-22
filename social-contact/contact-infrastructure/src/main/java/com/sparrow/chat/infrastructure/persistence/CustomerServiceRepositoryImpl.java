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

package com.sparrow.chat.infrastructure.persistence;

import com.sparrow.chat.contact.bo.CustomerServerBO;
import com.sparrow.chat.contact.dao.CustomerServerDao;
import com.sparrow.chat.contact.po.CustomerServer;
import com.sparrow.chat.contact.protocol.query.CustomerServerQuery;
import com.sparrow.chat.contact.repository.CustomerServerRepository;
import com.sparrow.chat.infrastructure.persistence.data.converter.CustomerServiceConverter;
import com.sparrow.context.SessionContext;
import com.sparrow.protocol.LoginUser;
import com.sparrow.utility.CollectionsUtility;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.util.Collections;
import java.util.List;

@Named
public class CustomerServiceRepositoryImpl implements CustomerServerRepository {
    @Inject
    private CustomerServerDao customerServiceDao;

    @Inject
    private CustomerServiceConverter customerServiceConverter;

    public List<CustomerServerBO> queryCustomerServer(CustomerServerQuery customerServiceQuery) {
        List<CustomerServer> customerServices = this.customerServiceDao.queryCustomerServer(this.customerServiceConverter.toDbPagerQuery(customerServiceQuery));
        if (CollectionsUtility.isNullOrEmpty(customerServices)) {
            return Collections.emptyList();
        }
        return this.customerServiceConverter.toBOS(customerServices);
    }

    @Override
    public Long countCustomerServer(CustomerServerQuery countQuery) {
        return this.customerServiceDao.countCustomerServer(this.customerServiceConverter.toCountQuery(countQuery));
    }

    @Override
    public List<CustomerServerBO> queryCustomerServiceById(String tenantId) {
        List<CustomerServer> customerServices = this.customerServiceDao.queryCustomerServicesByTenantId(tenantId);
        return this.customerServiceConverter.toBOS(customerServices);
    }

    @Override
    public void saveCustomerServer(CustomerServerBO customerServerBO) {
        CustomerServer customerService = this.customerServiceConverter.toPO(customerServerBO);
        if (customerServerBO.getId() == null) {
            this.customerServiceDao.insert(customerService);
            return;
        }
        this.customerServiceDao.update(customerService);
    }

    @Override
    public Boolean permit() {
        LoginUser loginUser = SessionContext.getLoginUser();

        return null;
    }
}
