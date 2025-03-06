package com.sparrow.chat.adapter.controller;

import com.sparrow.chat.domain.netty.UserContainer;
import com.sparrow.chat.domain.service.ChatService;
import com.sparrow.chat.protocol.ChatUser;
import com.sparrow.chat.protocol.dto.ContactsDTO;
import com.sparrow.chat.protocol.dto.SessionDTO;
import com.sparrow.chat.protocol.param.MessageCancelParam;
import com.sparrow.chat.protocol.param.MessageReadParam;
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

    @PostMapping("/get-user-id")
    public Integer getUserId(String token) throws BusinessException {
        ClientInformation client = ThreadContext.getClientInfo();
        return this.authenticator.authenticate(token, client.getDeviceId()).getUserId().intValue();
    }

    @PostMapping("/is-online")
    public Boolean online(ChatUser chatUser) {
        return UserContainer.getContainer().online(chatUser);
    }

    @CrossOrigin(origins = {"*"})
    @PostMapping("/contacts")
    public ContactsDTO getContactsList() {
        LoginUser loginUser = ThreadContext.getLoginToken();
        return chatService.getContacts(loginUser.getUserId());
    }

    @PostMapping("/session/read")
    public Boolean readSession(@RequestBody MessageReadParam messageRead) throws BusinessException {
        LoginUser loginUser = ThreadContext.getLoginToken();
        messageRead.setUser(ChatUser.longUserId(loginUser.getUserId(), loginUser.getCategory()));
        chatService.read(messageRead);
        return true;
    }

    @PostMapping("/sessions")
    public List<SessionDTO> getSessions() throws BusinessException {
        LoginUser loginUser = ThreadContext.getLoginToken();
        ChatUser chatUser = ChatUser.longUserId(loginUser.getUserId(), loginUser.getCategory());
        return chatService.fetchSessions(chatUser);
    }

    @PostMapping("/cancel")
    public Boolean cancel(@RequestBody MessageCancelParam messageCancel) {
        try {
            LoginUser loginUser = ThreadContext.getLoginToken();
            Long userId = loginUser.getUserId();
            Integer category = loginUser.getCategory();
            messageCancel.setSender(ChatUser.longUserId(userId, category));
            chatService.cancel(messageCancel);
        } catch (Exception e) {
            logger.error("cancel error", e);
            return false;
        }
        return true;
    }
}
