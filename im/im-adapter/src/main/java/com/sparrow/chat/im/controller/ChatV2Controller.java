package com.sparrow.chat.im.controller;

import com.sparrow.authenticator.Authenticator;
import com.sparrow.authenticator.HostAuthenticationToken;
import com.sparrow.authenticator.token.BearerToken;
import com.sparrow.chat.domain.bo.ChatUser;
import com.sparrow.chat.domain.netty.UserContainer;
import com.sparrow.chat.domain.service.ChatService;
import com.sparrow.chat.domain.service.MessageService;
import com.sparrow.chat.domain.service.UserLoginService;
import com.sparrow.chat.protocol.dto.HistoryMessageWrap;
import com.sparrow.chat.protocol.dto.MessageDTO;
import com.sparrow.chat.protocol.dto.SessionDTO;
import com.sparrow.chat.protocol.dto.SessionMetaDTO;
import com.sparrow.chat.protocol.params.SessionReadParams;
import com.sparrow.chat.protocol.query.ChatUserQuery;
import com.sparrow.chat.protocol.query.MessageCancelQuery;
import com.sparrow.chat.protocol.query.MessageQuery;
import com.sparrow.chat.protocol.query.SessionQuery;
import com.sparrow.context.SessionContext;
import com.sparrow.protocol.BusinessException;
import com.sparrow.protocol.ClientInformation;
import com.sparrow.protocol.LoginUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

@Api(tags = "聊天接口")
@RestController
@RequestMapping("/chat/v2")
@Slf4j
public class ChatV2Controller {
    @Autowired
    private ChatService chatService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private Authenticator authenticator;

    @Autowired
    private UserLoginService loginService;

    @ApiOperation(value = "解析登录Token")
    @PostMapping("/parse-token.json")
    public LoginUser parseToken(String token) throws BusinessException {
        ClientInformation client = SessionContext.getClientInfo();
        HostAuthenticationToken hostAuthenticationToken = new BearerToken(token, client.getDeviceId());
        return this.authenticator.authenticate(hostAuthenticationToken);
    }

    @ApiOperation(value = "获取当前用户信息")
    @GetMapping("/get-current-user.json")
    public LoginUser getCurrentUser() {
        return SessionContext.getLoginUser();
    }


    @ApiOperation(value = "登录")
    @PostMapping("/login.json")
    public String login(@RequestBody ChatUserQuery userQuery) throws BusinessException {
        return this.loginService.login(Long.parseLong(userQuery.getId()));
    }

    @ApiOperation(value = "登录")
    @PostMapping("/long-login.json")
    public String login2(@RequestBody Long userId) throws BusinessException {
        return this.loginService.login(userId);
    }

    @ApiOperation(value = "获取用户在线状态")
    @PostMapping("/is-online.json")
    public Boolean online(ChatUserQuery chatUser) {
        return UserContainer.getContainer().online(ChatUser.convertFromQuery(chatUser));
    }


    @ApiOperation(value = "会话已读状态")
    @PostMapping("/session/read.json")
    public Boolean readSession(@RequestBody SessionReadParams sessionReadParams) throws BusinessException {
        chatService.read(sessionReadParams);
        return true;
    }

    @ApiOperation(value = "获取会话列表")
    @GetMapping("/sessions.json")
    public List<SessionDTO> getSessions() throws BusinessException {
        return chatService.fetchSessions();
    }

    @ApiOperation(value = "获取消息列表")
    @PostMapping("/messages.json")
    public List<MessageDTO> getMessages(@RequestBody String sessionKey) throws BusinessException {
        return chatService.fetchMessages(sessionKey);
    }

    @ApiOperation(value = "获取历史消息列表")
    @PostMapping("/query-history-messages.json")
    public HistoryMessageWrap queryHistoryMessages(@RequestBody MessageQuery messageQuery) throws BusinessException {
        return chatService.queryHistoryMessages(messageQuery);
    }

    @ApiOperation(value = "查询历史会话列表")
    @PostMapping("/session-list.json")
    public List<SessionMetaDTO> getSessionList(@RequestBody SessionQuery sessionQuery) throws BusinessException {
        return this.messageService.querySessionList(sessionQuery);
    }

    @ApiIgnore
    @PostMapping("/cancel.json")
    public Boolean cancel(@RequestBody MessageCancelQuery messageCancel) {
        try {
            chatService.cancel(messageCancel);
        } catch (Exception e) {
            log.error("cancel error", e);
            return false;
        }
        return true;
    }
}
