package com.sparrow.chat.infrastructure.persistence;

import com.sparrow.chat.contact.bo.QunBO;
import com.sparrow.chat.contact.dao.QunDao;
import com.sparrow.chat.contact.po.Qun;
import com.sparrow.chat.contact.protocol.enums.ContactError;
import com.sparrow.chat.contact.protocol.qun.QunCreateParam;
import com.sparrow.chat.contact.protocol.qun.QunModifyParam;
import com.sparrow.chat.contact.repository.QunRepository;
import com.sparrow.chat.infrastructure.persistence.data.converter.QunConverter;
import com.sparrow.exception.Asserts;
import com.sparrow.protocol.BusinessException;
import com.sparrow.protocol.enums.StatusRecord;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;

@Named
public class QunRepositoryImpl implements QunRepository {
    @Inject
    private QunDao qunDao;

    @Inject
    private QunConverter qunConverter;

    @Override
    public Long createQun(QunCreateParam qunCreateParam) {
        Qun qun = this.qunConverter.createParam2Po(qunCreateParam);
        return this.qunDao.insert(qun);
    }

    @Override
    public void modifyQun(QunModifyParam qunModifyParam) throws BusinessException {
        Qun oldQun = this.qunDao.getEntity(qunModifyParam.getQunId());
        Asserts.isTrue(null == oldQun, ContactError.QUN_NOT_FOUND);
        Asserts.isTrue(!StatusRecord.ENABLE.equals(oldQun.getStatus()), ContactError.QUN_STATUS_INVALID);
        Qun qun = this.qunConverter.modifyParam2Po(qunModifyParam);
        this.qunDao.update(qun);
    }

    @Override
    public QunBO qunDetail(Long qunId) throws BusinessException {
        Qun qun = this.qunDao.getEntity(qunId);
        return this.qunConverter.po2Bo(qun);
    }

    @Override
    public List<QunBO> queryQunPlaza(Long categoryId) {
        List<Qun> quns = this.qunDao.queryQunList(categoryId);
        return this.qunConverter.poList2BoList(quns);
    }

    @Override
    public List<QunBO> queryQunPlaza() {
        List<Qun> quns = this.qunDao.queryEnabledQunList();
        return this.qunConverter.poList2BoList(quns);
    }
}
