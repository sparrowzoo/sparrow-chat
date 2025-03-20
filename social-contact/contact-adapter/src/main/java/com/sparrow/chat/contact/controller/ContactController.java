package com.sparrow.chat.contact.controller;

import com.sparrow.chat.contact.assembler.ContactAssembler;
import com.sparrow.chat.contact.bo.ContactsWrapBO;
import com.sparrow.chat.contact.bo.UserProfileBO;
import com.sparrow.chat.contact.protocol.FindUserSecretParam;
import com.sparrow.chat.contact.protocol.vo.ContactVO;
import com.sparrow.chat.contact.protocol.vo.UserFriendApplyVO;
import com.sparrow.chat.contact.protocol.vo.UserVO;
import com.sparrow.chat.contact.service.ContactService;
import com.sparrow.passport.protocol.dto.UserProfileDTO;
import com.sparrow.protocol.BusinessException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

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
    @PostMapping("/find-friend.json")
    @ApiOperation("通过用户标识（Email）查找用户")
    public UserFriendApplyVO findFriend(@RequestBody FindUserSecretParam findUserSecretParam) throws BusinessException {
        UserProfileBO contactBO = this.contactService.findFriend(findUserSecretParam.getUserIdentify());
        return this.contactAssembler.toUserFriendApplyVO(contactBO);
    }

    @PostMapping("/contacts.json")
    @ApiOperation("联系人接口")
    public ContactVO getContacts() throws BusinessException {
        ContactsWrapBO contactsWrapBO = this.contactService.getContacts();
        return this.contactAssembler.assembleVO(contactsWrapBO);
    }


    @PostMapping("/get-users-by-ids.json")
    @ApiOperation("通过用户ID获取用户列表")
    public List<UserVO> getUsersByIds(@RequestBody List<Long> userIds) throws BusinessException {
        Map<Long, UserProfileDTO> userProfileDTOMap = this.contactService.getUserMap(userIds);
        return this.contactAssembler.assembleUserListVO(userProfileDTOMap.values());
    }
}
