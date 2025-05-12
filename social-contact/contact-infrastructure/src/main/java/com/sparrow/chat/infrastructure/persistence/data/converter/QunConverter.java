package com.sparrow.chat.infrastructure.persistence.data.converter;

import com.sparrow.chat.contact.bo.QunMemberBO;
import com.sparrow.chat.contact.po.Qun;
import com.sparrow.chat.contact.po.QunMember;
import com.sparrow.chat.contact.protocol.dto.QunDTO;
import com.sparrow.chat.contact.protocol.qun.QunCreateParam;
import com.sparrow.chat.contact.protocol.qun.QunModifyParam;
import com.sparrow.protocol.LoginUser;
import com.sparrow.protocol.ThreadContext;
import com.sparrow.protocol.enums.StatusRecord;
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

    public QunDTO po2Bo(Qun qun) {
        QunDTO qunDto = new QunDTO();
        BeanUtility.copyProperties(qun, qunDto);
        return qunDto;
    }

    public List<QunMemberBO> membersPo2BoList(List<QunMember> members) {
        if (CollectionsUtility.isNullOrEmpty(members)) {
            return Collections.emptyList();
        }
        List<QunMemberBO> qunMemberBos = new ArrayList<>(members.size());
        for (QunMember qunMember : members) {
            QunMemberBO memberBO = new QunMemberBO();
            BeanUtility.copyProperties(qunMember, memberBO);
            qunMemberBos.add(memberBO);
        }
        return qunMemberBos;
    }

    public List<QunDTO> poList2BoList(List<Qun> quns) {
        if (CollectionsUtility.isNullOrEmpty(quns)) {
            return Collections.emptyList();
        }
        List<QunDTO> qunBos = new ArrayList<>(quns.size());
        for (Qun qun : quns) {
            qunBos.add(this.po2Bo(qun));
        }
        return qunBos;
    }
}
