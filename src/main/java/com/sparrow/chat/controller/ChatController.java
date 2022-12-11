package com.sparrow.chat.controller;

import com.sparrow.chat.init.InitQun;
import com.sparrow.chat.init.InitUser;
import com.sparrow.chat.protocol.ContactsDTO;
import com.sparrow.chat.protocol.MessageReadParam;
import com.sparrow.chat.protocol.SessionDTO;
import com.sparrow.chat.repository.MessageRepository;
import com.sparrow.chat.service.ChatService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chat")
public class ChatController {
    @Autowired
    private ChatService chatService;
    @Autowired
    private InitQun initQun;
    @Autowired
    private InitUser initUser;

    @GetMapping("init")
    public Boolean initChatTest() {
        this.initUser.initUser();
        for (int i = 0; i < 100; i++) {
            String qunId = "qun-" + i;
            this.initQun.initQun(qunId);
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
    public void readSession(@RequestBody MessageReadParam messageRead) {
        chatService.read(messageRead);
    }

    @PostMapping("/sessions")
    public List<SessionDTO> getSessions(Integer token) {
        return chatService.fetchSessions(token);
    }
}
