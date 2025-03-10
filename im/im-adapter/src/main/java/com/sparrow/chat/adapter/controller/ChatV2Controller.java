package com.sparrow.chat.adapter.controller;

import com.sparrow.chat.domain.bo.ChatUser;
import com.sparrow.chat.domain.netty.UserContainer;
import com.sparrow.chat.domain.service.ChatService;
import com.sparrow.chat.domain.service.VisitorService;
import com.sparrow.chat.protocol.dto.ContactsDTO;
import com.sparrow.chat.protocol.dto.SessionDTO;
import com.sparrow.chat.protocol.query.ChatUserQuery;
import com.sparrow.chat.protocol.query.MessageCancelQuery;
import com.sparrow.chat.protocol.query.MessageReadQuery;
import com.sparrow.protocol.BusinessException;
import com.sparrow.protocol.ClientInformation;
import com.sparrow.protocol.LoginUser;
import com.sparrow.protocol.ThreadContext;
import com.sparrow.support.Authenticator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PostMapping("/get-user-id")
    public Integer getUserId(String token) throws BusinessException {
        ClientInformation client = ThreadContext.getClientInfo();
        return this.authenticator.authenticate(token, client.getDeviceId()).getUserId().intValue();
    }

    @GetMapping("/get-visitor-token")
    public String getVisitorToken() {
        return this.visitorService.generateVisitorToken();
    }

    @PostMapping("/is-online")
    public Boolean online(ChatUserQuery chatUser) {
        return UserContainer.getContainer().online(ChatUser.convertFromQuery(chatUser));
    }

    @CrossOrigin(origins = {"*"})
    @PostMapping("/contacts")
    public ContactsDTO getContactsList() {
        LoginUser loginUser = ThreadContext.getLoginToken();
        return chatService.getContacts(loginUser.getUserId());
    }

    @PostMapping("/session/read")
    public Boolean readSession(@RequestBody MessageReadQuery messageReadQuery) throws BusinessException {
        chatService.read(messageReadQuery);
        return true;
    }

    @PostMapping("/sessions")
    public List<SessionDTO> getSessions() {
        LoginUser loginUser = ThreadContext.getLoginToken();
        ChatUser chatUser = ChatUser.longUserId(loginUser.getUserId(), loginUser.getCategory());
        return chatService.fetchSessions(chatUser);
    }

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
