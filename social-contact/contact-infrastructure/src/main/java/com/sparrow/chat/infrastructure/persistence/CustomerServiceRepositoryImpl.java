package com.sparrow.chat.infrastructure.persistence;

import com.sparrow.chat.contact.bo.CustomerServerBO;
import com.sparrow.chat.contact.dao.CustomerServerDao;
import com.sparrow.chat.contact.po.CustomerServer;
import com.sparrow.chat.contact.protocol.query.CustomerServerQuery;
import com.sparrow.chat.contact.repository.CustomerServerRepository;
import com.sparrow.chat.infrastructure.persistence.data.converter.CustomerServiceConverter;
import com.sparrow.protocol.LoginUser;
import com.sparrow.protocol.ThreadContext;
import com.sparrow.utility.CollectionsUtility;

import javax.inject.Inject;
import javax.inject.Named;
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
        LoginUser loginUser = ThreadContext.getLoginToken();

        return null;
    }
}
