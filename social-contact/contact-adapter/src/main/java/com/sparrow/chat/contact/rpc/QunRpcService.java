package com.sparrow.chat.contact.rpc;

import com.sparrow.chat.contact.QunServiceApi;
import com.sparrow.chat.contact.bo.QunMemberBO;
import com.sparrow.chat.contact.service.QunService;
import com.sparrow.protocol.BusinessException;
import com.sparrow.utility.CollectionsUtility;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
@Slf4j
public class QunRpcService implements QunServiceApi {
    public QunRpcService() {
        log.info("QunRpcService init");
    }

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
