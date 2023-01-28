package com.sparrow.chat.controller;

import com.sparrow.chat.init.InitQun;
import com.sparrow.chat.init.InitUser;
import com.sparrow.chat.protocol.ContactsDTO;
import com.sparrow.chat.protocol.MessageCancelParam;
import com.sparrow.chat.protocol.MessageReadParam;
import com.sparrow.chat.protocol.SessionDTO;
import com.sparrow.chat.service.ChatService;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chat")
public class ChatController {
    private static Logger logger = LoggerFactory.getLogger(ChatController.class);
    @Autowired
    private ChatService chatService;
    @Autowired
    private InitQun initQun;
    @Autowired
    private InitUser initUser;

    @GetMapping("init")
    public Boolean initChatTest() {
        this.initUser.initUser();
        String qunId = "qun-0";
        this.initQun.initQun(qunId);

        for (int i = 0; i < 100; i++) {

            this.initUser.initFriends(i);
            this.initQun.buildQunOfContact(i, qunId);
        }
        return true;
    }

    //    @CrossOrigin(origins = {"*"})
    @PostMapping("/contacts")
    public ContactsDTO getContactsList(String token) {
        int userId = Integer.parseInt(token);
        return chatService.getContacts(userId);
    }

    @PostMapping("/session/read")
    public Boolean readSession(@RequestBody MessageReadParam messageRead) {
        chatService.read(messageRead);
        return true;
    }

    @PostMapping("/sessions")
    public List<SessionDTO> getSessions(Integer token) {
        return chatService.fetchSessions(token);
    }

    @PostMapping("/cancel")
    public Boolean cancel(@RequestBody MessageCancelParam messageCancel) {
        try {
            chatService.cancel(messageCancel);
        } catch (Exception e) {
            logger.error("cancel error", e);
            return false;
        }
        return true;
    }
}
