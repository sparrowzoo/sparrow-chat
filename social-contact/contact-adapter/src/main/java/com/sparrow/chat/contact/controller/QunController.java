package com.sparrow.chat.contact.controller;

import com.sparrow.chat.contact.assembler.QunAssembler;
import com.sparrow.chat.contact.bo.QunDetailWrapBO;
import com.sparrow.chat.contact.bo.QunPlazaBO;
import com.sparrow.chat.contact.protocol.qun.*;
import com.sparrow.chat.contact.protocol.vo.QunPlazaVO;
import com.sparrow.chat.contact.protocol.vo.QunVO;
import com.sparrow.chat.contact.protocol.vo.QunWrapDetailVO;
import com.sparrow.chat.contact.service.QunService;
import com.sparrow.protocol.BusinessException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;

@RestController
@RequestMapping("qun")
@Api(value = "群", tags = "IM 群管理")
public class QunController {

    @Inject
    private QunService qunService;

    @Inject
    private QunAssembler qunAssembler;

    @ApiOperation("创建群")
    @PostMapping("create.json")
    public Long createQun(@RequestBody QunCreateParam qunCreateParam) throws BusinessException {
        return this.qunService.createQun(qunCreateParam);
    }

    @ApiOperation("修改群")
    @PostMapping("modify.json")
    public void modify(@RequestBody QunModifyParam qunModifyParam) throws BusinessException {
        this.qunService.modify(qunModifyParam);
    }

    @GetMapping("plaza.json")
    @ApiOperation("获取所有群")
    public QunPlazaVO qunPlazaOfCategory() throws BusinessException {
        QunPlazaBO qunPlaza = this.qunService.qunPlaza();
        return this.qunAssembler.assembleQunPlaza(qunPlaza);
    }


    @ApiOperation("邀请好友加群")
    @PostMapping("invite-friend-join.json")
    public String inviteFriend(@RequestBody InviteFriendParam inviteFriendParam) throws BusinessException {
        return this.qunService.inviteFriend(inviteFriendParam);
    }

    @ApiOperation("退群")
    @PostMapping("exist-qun.json")
    public void existQun(Long qunId) throws Throwable {
        this.qunService.existQun(qunId);
    }

    @ApiOperation("移除群成员")
    @PostMapping("remove-member.json")
    public void removeMember(@RequestBody RemoveMemberOfQunParam removeMemberOfQunParam) throws Throwable {
        this.qunService.removeMember(removeMemberOfQunParam);
    }

    @ApiOperation("群解散")
    @PostMapping("dissolve.json")
    public void dissolve(@RequestBody Long qunId) throws BusinessException {
        this.qunService.dissolve(qunId);
    }

    @ApiOperation("转移群主")
    @PostMapping("transfer-owner.json")
    public void transfer(@RequestBody TransferOwnerOfQunParam transferOwnerOfQun) throws BusinessException {
        this.qunService.transfer(transferOwnerOfQun);
    }


    @ApiOperation("根据邀请链接获取群详情")
    @GetMapping("invite/{token}.json")
    public QunVO inviteJoinQun(@PathVariable("token") String token) throws BusinessException {
        return null;
    }

    @ApiOperation("获取群详情")
    @GetMapping("detail/{qunId}.json")
    public QunWrapDetailVO qunDetail(@PathVariable("qunId") Long qunId) throws BusinessException {
        QunDetailWrapBO qunDetailWrap = this.qunService.qunDetail(qunId);
        return this.qunAssembler.assembleQunWrapDetail(qunDetailWrap);
    }
}
