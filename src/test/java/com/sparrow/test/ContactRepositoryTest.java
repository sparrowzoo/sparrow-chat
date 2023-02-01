package com.sparrow.test;

import com.sparrow.chat.boot.Application;
import com.sparrow.chat.protocol.QunDTO;
import com.sparrow.chat.protocol.UserDTO;
import com.sparrow.chat.repository.ContactRepository;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class ContactRepositoryTest {

    @Autowired
    private ContactRepository contactsRepository;

    @Test
    public void getContact() {
        List<QunDTO> quns = contactsRepository.getQunsByUserId(100);
        List<UserDTO> users = contactsRepository.getFriendsByUserId(100);

        Assert.assertEquals(quns.size(), 1);
        Assert.assertEquals(users.size(), 10);
    }
}
