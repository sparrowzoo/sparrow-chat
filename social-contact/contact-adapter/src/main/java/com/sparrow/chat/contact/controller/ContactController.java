package com.sparrow.chat.contact.controller;

import com.sparrow.chat.contact.assembler.ContactAssembler;
import com.sparrow.chat.contact.bo.ContactsWrapBO;
import com.sparrow.chat.contact.bo.CustomerServerBO;
import com.sparrow.chat.contact.bo.UserProfileBO;
import com.sparrow.chat.contact.protocol.FindUserSecretParam;
import com.sparrow.chat.contact.protocol.vo.ContactGroupVO;
import com.sparrow.chat.contact.protocol.vo.ContactVO;
import com.sparrow.chat.contact.protocol.vo.UserFriendApplyVO;
import com.sparrow.chat.contact.service.ContactService;
import com.sparrow.chat.contact.service.CustomerServerService;
import com.sparrow.exception.Asserts;
import com.sparrow.passport.protocol.dto.UserProfileDTO;
import com.sparrow.protocol.BusinessException;
import com.sparrow.protocol.LoginUser;
import com.sparrow.protocol.ThreadContext;
import com.sparrow.protocol.constant.SparrowError;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @Autowired
    private CustomerServerService customerServerService;

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

    @GetMapping("/contacts.json")
    @ApiOperation("联系人接口")
    public ContactGroupVO getContacts() throws BusinessException {
        LoginUser loginUser = ThreadContext.getLoginToken();
        Asserts.isTrue(loginUser.isVisitor(), SparrowError.USER_NOT_LOGIN);
        ContactsWrapBO contactsWrapBO = this.contactService.getContacts();
        return this.contactAssembler.assembleVO(contactsWrapBO);
    }


    @PostMapping("/get-users-by-ids.json")
    @ApiOperation("通过用户ID获取用户列表")
    public List<ContactVO> getUsersByIds(@RequestBody List<Long> userIds) throws BusinessException {
        Map<Long, UserProfileDTO> userProfileDTOMap = this.contactService.getUserMap(userIds);
        return this.contactAssembler.assembleUserListVO(userProfileDTOMap.values());
    }

    @PostMapping("/get-customer-servers.json")
    @ApiOperation("获取客服列表")
    public List<ContactVO> getUsersTenantId() throws BusinessException {
        LoginUser loginUser = ThreadContext.getLoginToken();
        List<CustomerServerBO> customerServers = this.customerServerService.getCustomerServerListByTenantId(loginUser.getTenantId());
        List<Long> customerServerIds = customerServers.stream().map(CustomerServerBO::getServerId).collect(java.util.stream.Collectors.toList());
        Map<Long, UserProfileDTO> userProfileDTOMap = this.contactService.getUserMap(customerServerIds);
        return this.contactAssembler.assembleUserListVO(userProfileDTOMap.values());
    }
}
