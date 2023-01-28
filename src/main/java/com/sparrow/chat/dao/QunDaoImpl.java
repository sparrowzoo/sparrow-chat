package com.sparrow.chat.dao;

import com.sparrow.chat.po.Qun;
import com.sparrow.orm.template.impl.ORMStrategy;
import javax.inject.Named;

@Named
public class QunDaoImpl extends ORMStrategy<Qun, Long> implements QunDAO {
}
