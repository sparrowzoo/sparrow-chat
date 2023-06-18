package com.sparrow.chat.infrastructure.persistence.data.converter;

import com.sparrow.chat.contact.bo.QunBO;
import com.sparrow.chat.contact.protocol.qun.QunModifyParam;
import com.sparrow.protocol.enums.StatusRecord;

import com.sparrow.chat.contact.po.Qun;
import com.sparrow.chat.contact.protocol.qun.QunCreateParam;
import com.sparrow.protocol.LoginUser;
import com.sparrow.protocol.ThreadContext;
import com.sparrow.utility.BeanUtility;
import com.sparrow.utility.CollectionsUtility;

import javax.inject.Named;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Named
public class QunConverter {
    public Qun createParam2Po(QunCreateParam qunCreateParam) {
        Qun qun = new Qun();
        BeanUtility.copyProperties(qunCreateParam, qun);
        LoginUser loginUser = ThreadContext.getLoginToken();
        qun.setCreateUserId(loginUser.getUserId());
        qun.setModifiedUserId(loginUser.getUserId());
        qun.setGmtCreate(System.currentTimeMillis());
        qun.setCreateUserName(loginUser.getUserName());
        qun.setGmtModified(qun.getGmtCreate());
        qun.setModifiedUserName(loginUser.getUserName());
        qun.setOwnerId(loginUser.getUserId());
        qun.setStatus(StatusRecord.ENABLE);
        return qun;
    }


    public Qun modifyParam2Po(QunModifyParam qunModifyParam) {
        Qun qun = new Qun();
        BeanUtility.copyProperties(qunModifyParam, qun);
        qun.setId(qunModifyParam.getQunId());
        LoginUser loginUser = ThreadContext.getLoginToken();
        qun.setModifiedUserId(loginUser.getUserId());
        qun.setGmtModified(System.currentTimeMillis());
        qun.setModifiedUserName(loginUser.getUserName());
        qun.setStatus(StatusRecord.ENABLE);
        return qun;
    }

    public QunBO po2Bo(Qun qun) {
        QunBO qunBo = new QunBO();
        BeanUtility.copyProperties(qun, qunBo);
        return qunBo;
    }

    public List<QunBO> poList2BoList(List<Qun> quns) {
        if (CollectionsUtility.isNullOrEmpty(quns)) {
            return Collections.emptyList();
        }
        List<QunBO> qunBos = new ArrayList<>(quns.size());
        for (Qun qun : quns) {
            qunBos.add(this.po2Bo(qun));
        }
        return qunBos;
    }
}
