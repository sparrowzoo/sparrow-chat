package com.sparrow.chat.contact.controller;

import com.sparrow.chat.contact.assembler.QunAssembler;
import com.sparrow.chat.contact.bo.QunDetailWrapBO;
import com.sparrow.chat.contact.bo.QunPlazaBO;
import com.sparrow.chat.contact.protocol.qun.*;
import com.sparrow.chat.contact.protocol.vo.QunPlazaVO;
import com.sparrow.chat.contact.protocol.vo.QunVO;
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
    @PostMapping("create")
    public Long createQun(@RequestBody QunCreateParam qunCreateParam) throws BusinessException {
        return this.qunService.createQun(qunCreateParam);
    }

    @ApiOperation("修改群")
    @PostMapping("modify")
    public void modify(@RequestBody QunModifyParam qunModifyParam) throws BusinessException {
        this.qunService.modify(qunModifyParam);
    }


    @ApiOperation("群详情")
    @GetMapping("detail/{qunId}")
    public QunVO detail(@PathVariable("qunId") Long qunId) throws BusinessException {
        QunDetailWrapBO qunDetail = this.qunService.qunDetail(qunId);
        return this.qunAssembler.assembleQun(qunDetail);
    }

    @PostMapping("plaza-of-category-id/{categoryId}")
    @ApiOperation("获取类别下的群列表")
    public QunPlazaVO qunPlazaOfCategory(@PathVariable("categoryId") Long categoryId) throws BusinessException {
        QunPlazaBO qunPlaza = this.qunService.qunPlaza(categoryId);
        return this.qunAssembler.assembleQunPlaza(qunPlaza);
    }


    @ApiOperation("邀请好友加群")
    @PostMapping("invite-friend-join")
    public String inviteFriend(@RequestBody InviteFriendParam inviteFriendParam) throws BusinessException {
        return this.qunService.inviteFriend(inviteFriendParam);
    }

    @ApiOperation("退群")
    @PostMapping("exist-qun")
    public void existQun(Long qunId) throws BusinessException {
        this.qunService.existQun(qunId);
    }

    @ApiOperation("移除群成员")
    @PostMapping("remove-member")
    public void removeMember(@RequestBody RemoveMemberOfQunParam removeMemberOfQunParam) throws BusinessException {
        this.qunService.removeMember(removeMemberOfQunParam);
    }

    @ApiOperation("群解散")
    @PostMapping("dissolve")
    public void dissolve(@RequestBody Long qunId) throws BusinessException {
        this.qunService.dissolve(qunId);
    }

    @ApiOperation("转移群主")
    @PostMapping
    public void transfer(@RequestBody TransferOwnerOfQunParam transferOwnerOfQun) throws BusinessException {
        this.qunService.transfer(transferOwnerOfQun);
    }


    @ApiOperation("根据邀请链接获取群详情")
    @GetMapping("invite/{token}")
    public QunVO inviteJoinQun(@PathVariable("token") String token) throws BusinessException {
        return null;
    }
}
