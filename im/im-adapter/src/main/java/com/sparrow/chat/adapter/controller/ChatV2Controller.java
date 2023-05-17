package com.sparrow.chat.adapter.controller;

import com.sparrow.chat.domain.netty.UserContainer;
import com.sparrow.chat.domain.service.ChatService;
import com.sparrow.chat.protocol.dto.ContactsDTO;
import com.sparrow.chat.protocol.param.MessageCancelParam;
import com.sparrow.chat.protocol.param.MessageReadParam;
import com.sparrow.chat.protocol.dto.SessionDTO;
import com.sparrow.protocol.BusinessException;
import com.sparrow.protocol.ClientInformation;
import com.sparrow.protocol.LoginUser;
import com.sparrow.protocol.ThreadContext;
import com.sparrow.support.Authenticator;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chat/v2")
public class ChatV2Controller {
    private static Logger logger = LoggerFactory.getLogger(ChatV2Controller.class);
    @Autowired
    private ChatService chatService;

    @Autowired
    private Authenticator authenticator;

    @RequestMapping("/get-user-id")
    public Integer getUserId(String token) throws BusinessException {
        ClientInformation client=ThreadContext.getClientInfo();
        return this.authenticator.authenticate(token, client.getDeviceId()).getUserId().intValue();
    }

    @RequestMapping("/is-online")
    public Boolean online(LoginUser loginUser) {
        //LoginUser loginUser = ThreadContext.getLoginToken();
        int userId = loginUser.getUserId().intValue();
        return UserContainer.getContainer().online(String.valueOf(userId));
    }

    @CrossOrigin(origins = {"*"})
    @PostMapping("/contacts")
    public ContactsDTO getContactsList() {
        LoginUser loginUser = ThreadContext.getLoginToken();
        return chatService.getContacts(loginUser.getUserId().intValue());
    }

    @PostMapping("/session/read")
    public Boolean readSession(@RequestBody MessageReadParam messageRead) throws BusinessException {
        LoginUser loginUser = ThreadContext.getLoginToken();
        int userId = loginUser.getUserId().intValue();
        messageRead.setUserId(userId);
        chatService.read(messageRead);
        return true;
    }

    @PostMapping("/sessions")
    public List<SessionDTO> getSessions() throws BusinessException {
        LoginUser loginUser = ThreadContext.getLoginToken();
        int userId = loginUser.getUserId().intValue();
        return chatService.fetchSessions(userId);
    }

    @PostMapping("/cancel")
    public Boolean cancel(@RequestBody MessageCancelParam messageCancel) {
        try {
            int userId = ThreadContext.getLoginToken().getUserId().intValue();
            messageCancel.setSender(userId);
            chatService.cancel(messageCancel);
        } catch (Exception e) {
            logger.error("cancel error", e);
            return false;
        }
        return true;
    }
}
