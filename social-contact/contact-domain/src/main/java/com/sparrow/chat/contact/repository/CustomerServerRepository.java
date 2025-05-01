package com.sparrow.chat.contact.repository;

import com.sparrow.chat.contact.bo.CustomerServerBO;
import com.sparrow.chat.contact.protocol.query.CustomerServerQuery;

import java.util.List;

public interface CustomerServerRepository {
    List<CustomerServerBO> queryCustomerServer(CustomerServerQuery customerServiceQuery);

    Long countCustomerServer(CustomerServerQuery countQuery);

    List<CustomerServerBO> queryCustomerServiceById(String tenantId);

    void saveCustomerServer(CustomerServerBO customerServerBO);

    Boolean permit();

}
