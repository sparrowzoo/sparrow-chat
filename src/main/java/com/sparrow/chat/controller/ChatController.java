package com.sparrow.chat.controller;

import com.sparrow.chat.protocol.ContactsDTO;
import com.sparrow.chat.protocol.SessionDTO;
import com.sparrow.chat.service.ChatService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChatController {
    @Autowired
    private ChatService chatService;

    @PostMapping("/contacts")
    public ContactsDTO getContactsList(String token) {
        int userId = Integer.parseInt(token);
        return chatService.getContacts(userId);
    }

    @PostMapping("/sessions")
    public List<SessionDTO> getSessions(String token) {
        return chatService.fetchSessions(100);
    }
}
