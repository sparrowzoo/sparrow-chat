package com.sparrow.chat.contact.dao.query.server;

import com.sparrow.protocol.dao.DatabasePagerQuery;
import lombok.Data;

@Data
public class PagerServerQuery extends CountQuery {
    public PagerServerQuery() {
        this.pagerQuery = new DatabasePagerQuery();
    }
    private DatabasePagerQuery pagerQuery;
    private String sortBy;
}
