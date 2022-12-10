package com.sparrow.chat.controller;

import com.sparrow.chat.init.InitQun;
import com.sparrow.chat.init.InitUser;
import com.sparrow.chat.protocol.ContactsDTO;
import com.sparrow.chat.protocol.SessionDTO;
import com.sparrow.chat.repository.MessageRepository;
import com.sparrow.chat.service.ChatService;
import java.util.List;
import javax.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
    @Autowired
    private MessageRepository messageRepository;

    @GetMapping("init")
    public Boolean initChatTest() {
        this.initQun.buildQunOfContact();
        this.initQun.initQun();
        this.initUser.initUser();
        this.initUser.initFriends();
        return true;
    }

    //    @CrossOrigin(origins = {"*"})
    @PostMapping("/contacts")
    public ContactsDTO getContactsList(String token) {
        int userId = Integer.parseInt(token);
        return chatService.getContacts(userId);
    }

    @GetMapping("/get-image/{msgId}")
    public String getImageBase64(@PathParam("msgId") String msgId) {
        return this.messageRepository.getImageContent(msgId);
    }

    @PostMapping("/sessions")
    public List<SessionDTO> getSessions(String token) {
        return chatService.fetchSessions(100);
    }
}
