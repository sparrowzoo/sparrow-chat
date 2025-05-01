package com.sparrow.chat.dao.sparrow;

import com.sparrow.chat.contact.dao.CustomerServerDao;
import com.sparrow.chat.contact.dao.query.server.CountQuery;
import com.sparrow.chat.contact.dao.query.server.PagerServerQuery;
import com.sparrow.chat.contact.po.CustomerServer;
import com.sparrow.orm.query.BooleanCriteria;
import com.sparrow.orm.query.Criteria;
import com.sparrow.orm.query.SearchCriteria;
import com.sparrow.orm.template.impl.ORMStrategy;

import javax.inject.Named;
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
