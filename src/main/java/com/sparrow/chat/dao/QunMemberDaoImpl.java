package com.sparrow.chat.dao;

import com.sparrow.chat.po.QunMember;
import com.sparrow.orm.template.impl.ORMStrategy;
import javax.inject.Named;

@Named
public class QunMemberDaoImpl extends ORMStrategy<QunMember, Long> implements QunMemberDAO {
}
