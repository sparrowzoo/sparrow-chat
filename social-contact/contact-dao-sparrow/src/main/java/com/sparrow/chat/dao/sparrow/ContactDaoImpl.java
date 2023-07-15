package com.sparrow.chat.dao.sparrow;

import com.sparrow.chat.contact.dao.ContactDao;
import com.sparrow.chat.contact.po.Contact;
import com.sparrow.orm.query.BooleanCriteria;
import com.sparrow.orm.query.Criteria;
import com.sparrow.orm.query.SearchCriteria;
import com.sparrow.orm.template.impl.ORMStrategy;

import javax.inject.Named;
import java.util.List;

@Named
public class ContactDaoImpl extends ORMStrategy<Contact, Long> implements ContactDao {
    @Override
    public List<Contact> getMyContact(Long userId) {
        SearchCriteria searchCriteria = new SearchCriteria();
        searchCriteria.setWhere(
                BooleanCriteria.criteria
                        (Criteria.field("contact.userId").equal(userId)));
        return this.getList(searchCriteria);
    }
}
