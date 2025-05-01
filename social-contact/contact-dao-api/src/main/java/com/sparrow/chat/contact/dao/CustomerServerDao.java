package com.sparrow.chat.contact.dao;

import com.sparrow.chat.contact.dao.query.server.CountQuery;
import com.sparrow.chat.contact.dao.query.server.PagerServerQuery;
import com.sparrow.chat.contact.po.CustomerServer;
import com.sparrow.protocol.dao.DaoSupport;

import java.util.List;

public interface CustomerServerDao extends DaoSupport<CustomerServer, Long> {
    List<CustomerServer> queryCustomerServer(PagerServerQuery pagerServerQuery);

    Long countCustomerServer(CountQuery countQuery);

    List<CustomerServer> queryCustomerServicesByTenantId(String tenantId);

}
