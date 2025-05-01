package com.sparrow.chat.infrastructure.persistence.data.converter;

import com.sparrow.chat.contact.bo.CustomerServerBO;
import com.sparrow.chat.contact.dao.query.server.CountQuery;
import com.sparrow.chat.contact.dao.query.server.PagerServerQuery;
import com.sparrow.chat.contact.po.CustomerServer;
import com.sparrow.chat.contact.protocol.query.CustomerServerQuery;
import com.sparrow.protocol.dao.DatabasePagerQuery;
import com.sparrow.utility.CollectionsUtility;
import org.springframework.beans.BeanUtils;

import javax.inject.Named;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Named
public class CustomerServiceConverter {
    public PagerServerQuery toDbPagerQuery(CustomerServerQuery customerServiceQuery) {
        if (customerServiceQuery == null) {
            return new PagerServerQuery();
        }
        PagerServerQuery pagerServiceQuery = new PagerServerQuery();
        BeanUtils.copyProperties(customerServiceQuery, pagerServiceQuery);
        pagerServiceQuery.setPagerQuery(new DatabasePagerQuery(customerServiceQuery));
        return pagerServiceQuery;
    }

    public CountQuery toCountQuery(CustomerServerQuery customerServiceQuery) {
        if (customerServiceQuery == null) {
            return new CountQuery();
        }
        CountQuery countAppQuery = new CountQuery();
        BeanUtils.copyProperties(customerServiceQuery, countAppQuery);
        return countAppQuery;
    }

    public CustomerServerBO toBO(CustomerServer customerService) {
        if (customerService == null) {
            new CustomerServerBO();
        }
        CustomerServerBO customerServiceBO = new CustomerServerBO();
        BeanUtils.copyProperties(customerService, customerServiceBO);
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
        BeanUtils.copyProperties(customerServiceBO, customerService);
        return customerService;
    }
}
