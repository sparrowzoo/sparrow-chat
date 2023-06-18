package com.sparrow.chat.contact.controller;

import com.sparrow.chat.contact.assembler.ContactAssembler;
import com.sparrow.chat.contact.bo.ContactBO;
import com.sparrow.chat.contact.protocol.FindUserSecretParam;
import com.sparrow.chat.contact.protocol.vo.UserFriendApplyVO;
import com.sparrow.chat.contact.service.ContactService;
import com.sparrow.protocol.BusinessException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(value = "contact", tags = "IM 联系人接口")
@RequestMapping("/contact")
public class ContactController {

    @Autowired
    private ContactService contactService;

    @Autowired
    private ContactAssembler contactAssembler;

    /**
     * 通过用户标识查找用户密文标识 和 用户基本信息
     *
     * @param findUserSecretParam
     * @return
     */
    @PostMapping("/find-friend")
    @ApiOperation("通过用户标识（email）查找用户")
    public UserFriendApplyVO findFriend(FindUserSecretParam findUserSecretParam) throws BusinessException {
        ContactBO contactBO = this.contactService.findFriend(findUserSecretParam.getUserIdentify());
        return this.contactAssembler.toUserFriendApplyVO(contactBO);
    }
}
