package com.sparrow.chat.contact.controller;

import com.sparrow.chat.contact.assembler.ContactAssembler;
import com.sparrow.chat.contact.bo.AuditWrapBO;
import com.sparrow.chat.contact.protocol.audit.FriendApplyParam;
import com.sparrow.chat.contact.protocol.audit.FriendAuditParam;
import com.sparrow.chat.contact.protocol.audit.JoinQunParam;
import com.sparrow.chat.contact.protocol.audit.QunAuditParam;
import com.sparrow.chat.contact.protocol.vo.AuditWrapVO;
import com.sparrow.chat.contact.service.AuditService;
import com.sparrow.protocol.BusinessException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;

@RestController
@RequestMapping("/audit")
@Api(value = "contact", tags = "IM 联系人审核")
public class AuditController {
    @Inject
    private ContactAssembler contactAssembler;

    @Inject
    private AuditService auditService;


    @GetMapping("friend-apply-list.json")
    @ApiOperation("获取好友申请列表")
    public AuditWrapVO friendApplyList() throws BusinessException {
        AuditWrapBO friendAuditBO = this.auditService.friendApplyList();
        return this.contactAssembler.toAuditVoList(friendAuditBO);
    }

    @GetMapping("qun-member-apply-list.json")
    @ApiOperation("群成员申请列表")
    public AuditWrapVO qunMemberApplyList() throws BusinessException {
        AuditWrapBO friendAuditBO = this.auditService.qunMemberApplyList();
        return this.contactAssembler.toAuditVoList(friendAuditBO);
    }

    @PostMapping("apply-friend.json")
    @ApiOperation("申请好友")
    public Long applyFriend(@RequestBody FriendApplyParam friendApplyParam) throws BusinessException {
        return this.auditService.applyFriend(friendApplyParam);
    }

    @PostMapping("/audit-friend-apply.json")
    @ApiOperation("对好友申请进行审核")
    public void auditFriendApply(@RequestBody FriendAuditParam friendAuditParam) throws Throwable {
        this.auditService.auditFriendApply(friendAuditParam);
    }

    @PostMapping("audit-qun-apply.json")
    @ApiOperation("对加群进行审核")
    public void auditQunApply(@RequestBody QunAuditParam qunAuditParam) throws Throwable {
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
    @PostMapping("join-qun.json")
    public void applyJoinQun(@RequestBody JoinQunParam joinQunParam) throws BusinessException {
        this.auditService.applyJoinQun(joinQunParam);
    }
}
