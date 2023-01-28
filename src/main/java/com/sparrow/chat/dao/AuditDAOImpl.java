package com.sparrow.chat.dao;

import com.sparrow.chat.po.Audit;
import com.sparrow.orm.template.impl.ORMStrategy;
import com.sparrow.protocol.dao.DaoSupport;
import javax.inject.Named;

@Named
public class AuditDAOImpl extends ORMStrategy<Audit, Long> implements AuditDAO {
}
