package com.sparrow.chat.contact.controller;

import com.sparrow.chat.contact.assembler.ContactAssembler;
import com.sparrow.chat.contact.bo.FriendAuditWrapBO;
import com.sparrow.chat.contact.protocol.audit.FriendApplyParam;
import com.sparrow.chat.contact.protocol.audit.FriendAuditParam;
import com.sparrow.chat.contact.protocol.audit.JoinQunParam;
import com.sparrow.chat.contact.protocol.audit.QunAuditParam;
import com.sparrow.chat.contact.protocol.vo.FriendAuditWrapVO;
import com.sparrow.chat.contact.service.AuditService;
import com.sparrow.protocol.BusinessException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;

@RestController
@Api(value = "contact", tags = "IM 联系人审核")
public class AuditController {

    @Inject
    private ContactAssembler contactAssembler;

    @Inject
    private AuditService auditService;


    @PostMapping("friend-apply-list")
    @ApiOperation("获取好友申请列表")
    public FriendAuditWrapVO friendApplyList() throws BusinessException {
        FriendAuditWrapBO friendAuditBO = this.auditService.friendApplyList();
        return this.contactAssembler.toUserFriendApplyVoList(friendAuditBO);
    }

    @PostMapping("apply-friend")
    @ApiOperation("申请好友")
    public Long applyFriend(FriendApplyParam friendApplyParam) throws BusinessException {
        return this.auditService.applyFriend(friendApplyParam);
    }

    @PostMapping("audit-friend-apply")
    @ApiOperation("对好友申请进行审核")
    public void auditFriendApply(@RequestBody FriendAuditParam friendAuditParam) throws BusinessException {
        this.auditService.auditFriendApply(friendAuditParam);
    }

    @PostMapping("audit-qun-apply")
    @ApiOperation("对加群进行审核")
    public void auditQunApply(@RequestBody QunAuditParam qunAuditParam) throws BusinessException {
        this.auditService.auditQunApply(qunAuditParam);
    }

    /**
     * 1. 从controller 获取loginUser 并放入joinQunParam
     * 2. 在业务里直接使用loginUser 参数不透传
     * 3. 从service 逐层传递
     *
     * @param joinQunParam
     * @throws BusinessException
     */
    @ApiOperation("加群")
    @PostMapping("join")
    public void joinQun(@RequestBody JoinQunParam joinQunParam) throws BusinessException {
        this.auditService.joinQun(joinQunParam);
    }
}
