package com.example.sweater.controller;

import com.example.sweater.MessageService;
import com.example.sweater.domain.Message;
import com.example.sweater.domain.User;
import com.example.sweater.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
public class MainController {
    @Autowired
    private MessageService messageService;
    @Autowired
    private UserService userService;

    @GetMapping
    public String getGreeting(Principal principal, Model model) {
        if(principal != null) {
            model.addAttribute("name", principal.getName());
        }
        return "greeting";
    }

    @GetMapping("/main")
    public String getMain(@RequestParam(required = false) String field, Model model)  {
        List<Message> messageList;
        if(field == null || field.isEmpty()) {
            messageList = messageService.getAllMessages();
        } else messageList = messageService.getMessagesByField(field);
        Message message = new Message();
        model.addAttribute("message", message);
        model.addAttribute("messages", messageList);
        model.addAttribute("field", field);
        return "main";
    }

    @PostMapping("/main")
    public String postMain(@ModelAttribute Message message, Model model, Principal principal) {
        if(principal != null) {
            User user = userService.findByName(principal.getName());
            message.setAuthor(user);
        }
        messageService.save(message);
        return "redirect:/main";
    }
}
