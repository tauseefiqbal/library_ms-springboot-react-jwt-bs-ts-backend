package com.luv2read.springbootlibrary.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.luv2read.springbootlibrary.dao.MessageRepository;
import com.luv2read.springbootlibrary.entity.Message;
import com.luv2read.springbootlibrary.requestmodels.AdminQuestionRequest;

@Service
@Transactional
public class MessagesService {

    private final MessageRepository messageRepository;

    public MessagesService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public void postMessage(Message messageRequest, String userEmail) {
        Message message = new Message(messageRequest.getTitle(), messageRequest.getQuestion());
        message.setUserEmail(userEmail);
        messageRepository.save(message);
    }

    public void putMessage(AdminQuestionRequest adminQuestionRequest, String userEmail) throws Exception {
        Optional<Message> message = messageRepository.findById(adminQuestionRequest.id());
        if (!message.isPresent()) {
            throw new Exception("Message not found");
        }

        message.get().setAdminEmail(userEmail);
        message.get().setResponse(adminQuestionRequest.response());
        message.get().setClosed(true);
        messageRepository.save(message.get());
    }

}
