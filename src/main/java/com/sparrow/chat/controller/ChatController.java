package com.sparrow.chat.controller;

import com.sparrow.chat.protocol.ContactsDTO;
import com.sparrow.chat.protocol.SessionDTO;
import com.sparrow.chat.service.ChatService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChatController {
    @Autowired
    private ChatService chatService;

    @GetMapping("/contacts")
    public ContactsDTO getContactsList(String token) {
        return chatService.getContacts(100);
    }

    @GetMapping("/sessions")
    public List<SessionDTO> getSessions(String token) {
        return chatService.fetchSessions(100);
    }
}
