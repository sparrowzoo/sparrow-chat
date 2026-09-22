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

package com.sparrow.chat.contact.service;

import com.sparrow.chat.contact.bo.CustomerServerBO;
import com.sparrow.chat.contact.protocol.query.CustomerServerQuery;
import com.sparrow.chat.contact.repository.CustomerServerRepository;
import com.sparrow.exception.Asserts;
import com.sparrow.protocol.BusinessException;
import com.sparrow.protocol.ListRecordTotalBO;
import com.sparrow.protocol.constant.SparrowError;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.util.List;

@Named
public class CustomerServerService {
    @Inject
    private CustomerServerRepository customerServerRepository;

    public ListRecordTotalBO<CustomerServerBO> getCustomerServerList(CustomerServerQuery query) throws BusinessException {
        Boolean hasPermission = this.customerServerRepository.permit();
        Asserts.isTrue(hasPermission, SparrowError.SYSTEM_PERMISSION_DENIED);
        Long total = customerServerRepository.countCustomerServer(query);
        if (total > 0) {
            List<CustomerServerBO> result = customerServerRepository.queryCustomerServer(query);
            return new ListRecordTotalBO<>(result, total);
        }
        return ListRecordTotalBO.empty();
    }

    public void saveCustomerServer(CustomerServerBO customerServer) throws BusinessException {
        Boolean hasPermission = this.customerServerRepository.permit();
        Asserts.isTrue(hasPermission, SparrowError.SYSTEM_PERMISSION_DENIED);
        customerServerRepository.saveCustomerServer(customerServer);
    }

    public List<CustomerServerBO> getCustomerServerListByTenantId(String tenantId) {
        return this.customerServerRepository.queryCustomerServiceById(tenantId);
    }
}
