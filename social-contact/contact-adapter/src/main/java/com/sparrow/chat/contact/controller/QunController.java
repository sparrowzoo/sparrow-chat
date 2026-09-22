/*
Licensed to the Apache Software Foundation (ASF) under one or more
contributor license agreements.  See the NOTICE file distributed with
this work for additional information regarding copyright ownership.
The ASF licenses this file to You under the Apache License, Version 2.0
(the "License"); you may not use this file except in compliance with
the License.  You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

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
import jakarta.inject.Inject;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("qun")
public class QunController {

    @Inject
    private QunService qunService;

    @Inject
    private QunAssembler qunAssembler;

    @PostMapping("create.json")
    public Long createQun(@RequestBody QunCreateParam qunCreateParam) throws BusinessException {
        return this.qunService.createQun(qunCreateParam);
    }

    @PostMapping("modify.json")
    public void modify(@RequestBody QunModifyParam qunModifyParam) throws BusinessException {
        this.qunService.modify(qunModifyParam);
    }

    @GetMapping("plaza.json")
    public QunPlazaVO qunPlazaOfCategory() throws BusinessException {
        QunPlazaBO qunPlaza = this.qunService.qunPlaza();
        return this.qunAssembler.assembleQunPlaza(qunPlaza);
    }


    @PostMapping("invite-friend-join.json")
    public String inviteFriend(@RequestBody InviteFriendParam inviteFriendParam) throws BusinessException {
        return this.qunService.inviteFriend(inviteFriendParam);
    }

    @PostMapping("exist-qun.json")
    public void existQun(Long qunId) throws Throwable {
        this.qunService.existQun(qunId);
    }

    @PostMapping("remove-member.json")
    public void removeMember(@RequestBody RemoveMemberOfQunParam removeMemberOfQunParam) throws Throwable {
        this.qunService.removeMember(removeMemberOfQunParam);
    }

    @PostMapping("dissolve.json")
    public void dissolve(@RequestBody Long qunId) throws BusinessException {
        this.qunService.dissolve(qunId);
    }

    @PostMapping("transfer-owner.json")
    public void transfer(@RequestBody TransferOwnerOfQunParam transferOwnerOfQun) throws BusinessException {
        this.qunService.transfer(transferOwnerOfQun);
    }


    @GetMapping("invite/{token}.json")
    public QunVO inviteJoinQun(@PathVariable("token") String token) throws BusinessException {
        return null;
    }

    @GetMapping("detail/{qunId}.json")
    public QunWrapDetailVO qunDetail(@PathVariable("qunId") Long qunId) throws BusinessException {
        QunDetailWrapBO qunDetailWrap = this.qunService.qunDetail(qunId);
        return this.qunAssembler.assembleQunWrapDetail(qunDetailWrap);
    }
}
