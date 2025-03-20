package com.sparrow.chat.im.controller;

import com.sparrow.chat.domain.bo.ChatUser;
import com.sparrow.chat.domain.netty.UserContainer;
import com.sparrow.chat.domain.service.ChatService;
import com.sparrow.chat.domain.service.UserLoginService;
import com.sparrow.chat.domain.service.VisitorService;
import com.sparrow.chat.protocol.dto.ContactsDTO;
import com.sparrow.chat.protocol.dto.MessageDTO;
import com.sparrow.chat.protocol.dto.SessionDTO;
import com.sparrow.chat.protocol.params.SessionReadParams;
import com.sparrow.chat.protocol.query.ChatUserQuery;
import com.sparrow.chat.protocol.query.MessageCancelQuery;
import com.sparrow.chat.protocol.query.MessageQuery;
import com.sparrow.protocol.BusinessException;
import com.sparrow.protocol.ClientInformation;
import com.sparrow.protocol.LoginUser;
import com.sparrow.protocol.ThreadContext;
import com.sparrow.support.Authenticator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

@Api(tags = "聊天接口")
@RestController
@RequestMapping("/chat/v2")
public class ChatV2Controller {
    private static Logger logger = LoggerFactory.getLogger(ChatV2Controller.class);
    @Autowired
    private ChatService chatService;

    @Autowired
    private Authenticator authenticator;

    @Autowired
    private VisitorService visitorService;

    @Autowired
    private UserLoginService loginService;

    @ApiOperation(value = "解析登录Token")
    @PostMapping("/parse-token")
    public LoginUser parseToken(String token) throws BusinessException {
        ClientInformation client = ThreadContext.getClientInfo();
        return this.authenticator.authenticate(token, client.getDeviceId());
    }

    @ApiOperation(value = "获取当前用户信息")
    @GetMapping("/get-current-user")
    public LoginUser getCurrentUser() {
        return ThreadContext.getLoginToken();
    }

    @ApiOperation(value = "获取访客Token")
    @GetMapping("/get-visitor-token")
    public String getVisitorToken() {
        return this.visitorService.generateVisitorToken();
    }

    @ApiOperation(value = "登录")
    @PostMapping("/login")
    public String login(@RequestBody ChatUserQuery userQuery) {
        return this.loginService.login(Long.parseLong(userQuery.getId()));
    }

    @ApiOperation(value = "登录")
    @PostMapping("/long-login")
    public String login2(@RequestBody Long userId) {
        return this.loginService.login(userId);
    }

    @ApiOperation(value = "获取用户在线状态")
    @PostMapping("/is-online")
    public Boolean online(ChatUserQuery chatUser) {
        return UserContainer.getContainer().online(ChatUser.convertFromQuery(chatUser));
    }

    @ApiIgnore
    @ApiOperation(value = "获取用户联系人列表")
    @CrossOrigin(origins = {"*"})
    @GetMapping("/contacts")
    public ContactsDTO getContactsList() {
        LoginUser loginUser = ThreadContext.getLoginToken();
        return chatService.getContacts(loginUser.getUserId());
    }

    @ApiOperation(value = "会话已读状态")
    @PostMapping("/session/read")
    public Boolean readSession(@RequestBody SessionReadParams sessionReadParams) throws BusinessException {
        chatService.read(sessionReadParams);
        return true;
    }

    @ApiOperation(value = "获取会话列表")
    @GetMapping("/sessions")
    public List<SessionDTO> getSessions() throws BusinessException {
        return chatService.fetchSessions();
    }

    @ApiOperation(value = "获取消息列表")
    @PostMapping("/messages")
    public List<MessageDTO> getMessages(String sessionKey) throws BusinessException {
        return chatService.fetchMessages(sessionKey);
    }

    @ApiOperation(value = "获取历史消息列表")
    @PostMapping("/history-messages")
    public List<MessageDTO> getHistoryMessages(MessageQuery messageQuery) throws BusinessException {
        return chatService.fetchHistoryMessages(messageQuery);
    }

    @ApiIgnore
    @PostMapping("/cancel")
    public Boolean cancel(@RequestBody MessageCancelQuery messageCancel) {
        try {
            chatService.cancel(messageCancel);
        } catch (Exception e) {
            logger.error("cancel error", e);
            return false;
        }
        return true;
    }
}
