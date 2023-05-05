package com.sparrow.chat.controller;

import com.sparrow.chat.commons.TokenParser;
import com.sparrow.chat.core.UserContainer;
import com.sparrow.chat.protocol.ContactsDTO;
import com.sparrow.chat.protocol.MessageCancelParam;
import com.sparrow.chat.protocol.MessageReadParam;
import com.sparrow.chat.protocol.SessionDTO;
import com.sparrow.chat.service.ChatService;
import com.sparrow.protocol.BusinessException;
import com.sparrow.protocol.LoginUser;
import com.sparrow.protocol.ThreadContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/chat/v2")
public class ChatV2Controller {
    private static Logger logger = LoggerFactory.getLogger(ChatV2Controller.class);
    @Autowired
    private ChatService chatService;

    @RequestMapping("/get-user-id")
    public Integer getUserId(String token) throws BusinessException {
        return TokenParser.parseUserId(token).getUserId().intValue();
    }

    @RequestMapping("/is-online")
    public Boolean online() throws BusinessException {
        LoginUser loginUser=ThreadContext.getLoginToken();
        int userId =loginUser.getUserId().intValue();
        return UserContainer.getContainer().online(String.valueOf(userId));
    }

    //    @CrossOrigin(origins = {"*"})
    @PostMapping("/contacts")
    public ContactsDTO getContactsList() throws BusinessException {
        LoginUser loginUser= ThreadContext.getLoginToken();
        return chatService.getContacts(loginUser.getUserId().intValue());
    }

    @PostMapping("/session/read")
    public Boolean readSession(@RequestBody MessageReadParam messageRead) throws BusinessException {
        LoginUser loginUser= ThreadContext.getLoginToken();
        int userId =loginUser.getUserId().intValue();
        messageRead.setUserId(userId);
        chatService.read(messageRead);
        return true;
    }

    @PostMapping("/sessions")
    public List<SessionDTO> getSessions() throws BusinessException {
        LoginUser loginUser=ThreadContext.getLoginToken();
        int userId = loginUser.getUserId().intValue();
        return chatService.fetchSessions(userId);
    }

    @PostMapping("/cancel")
    public Boolean cancel(@RequestBody MessageCancelParam messageCancel) {
        try {
            int userId = ThreadContext.getLoginToken().getUserId().intValue();
            messageCancel.setFromUserId(userId);
            chatService.cancel(messageCancel);
        } catch (Exception e) {
            logger.error("cancel error", e);
            return false;
        }
        return true;
    }
}
