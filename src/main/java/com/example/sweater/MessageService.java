package com.example.sweater;

import com.example.sweater.domain.Message;
import com.example.sweater.repos.MessageRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.util.List;

@Service
public class MessageService {
    @Autowired
    private MessageRepo messageRepo;

    public List<Message> getAllMessages() {
        return messageRepo.findAll();
    }

    public void save(Message message) {
        messageRepo.save(message);
    }

    public List<Message> getMessagesByField(String field) {
        return messageRepo.findByText(field);
    }
}
