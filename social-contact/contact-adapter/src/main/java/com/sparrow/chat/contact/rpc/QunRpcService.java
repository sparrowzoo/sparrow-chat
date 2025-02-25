package com.sparrow.chat.contact.rpc;

import com.sparrow.chat.contact.bo.QunMemberBO;
import com.sparrow.chat.contact.service.QunService;
import com.sparrow.protocol.BusinessException;
import com.sparrow.utility.CollectionsUtility;
import com.sparrowzoo.chat.contact.QunServiceApi;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Named
public class QunRpcService implements QunServiceApi {
    @Inject
    private QunService qunService;

    @Override
    public List<Long> getMemberById(Long qunId) throws BusinessException {
        List<QunMemberBO> memberBos = this.qunService.getMemberIdsById(qunId);
        if (CollectionsUtility.isNullOrEmpty(memberBos)) {
            return Collections.emptyList();
        }
        List<Long> memberIds = new ArrayList<>(memberBos.size());
        for (QunMemberBO memberBO : memberBos) {
            memberIds.add(memberBO.getMemberId());
        }
        return memberIds;
    }
}
