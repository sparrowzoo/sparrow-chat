package com.sparrow.chat.contact.service;

import com.sparrow.chat.contact.bo.CustomerServerBO;
import com.sparrow.chat.contact.protocol.query.CustomerServerQuery;
import com.sparrow.chat.contact.repository.CustomerServerRepository;
import com.sparrow.exception.Asserts;
import com.sparrow.protocol.BusinessException;
import com.sparrow.protocol.ListRecordTotalBO;
import com.sparrow.protocol.constant.SparrowError;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;

@Named
public class CustomerServerService {
    @Inject
    private CustomerServerRepository customerServerRepository;

    public ListRecordTotalBO<CustomerServerBO> getCustomerServerList(CustomerServerQuery query) throws BusinessException {
        Boolean hasPermission=this.customerServerRepository.permit();
        Asserts.isTrue(hasPermission, SparrowError.SYSTEM_PERMISSION_DENIED);
        Long total = customerServerRepository.countCustomerServer(query);
        if (total > 0) {
            List<CustomerServerBO> result = customerServerRepository.queryCustomerServer(query);
            return new ListRecordTotalBO<>(result, total);
        }
        return ListRecordTotalBO.empty();
    }

    public void saveCustomerServer(CustomerServerBO customerServer) throws BusinessException {
        Boolean hasPermission=this.customerServerRepository.permit();
        Asserts.isTrue(hasPermission, SparrowError.SYSTEM_PERMISSION_DENIED);
        customerServerRepository.saveCustomerServer(customerServer);
    }

    public List<CustomerServerBO> getCustomerServerListByTenantId(String tenantId) {
        return this.customerServerRepository.queryCustomerServiceById(tenantId);
    }
}
