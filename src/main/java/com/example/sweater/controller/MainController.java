package com.example.sweater.controller;

import com.example.sweater.MessageService;
import com.example.sweater.domain.Message;
import com.example.sweater.domain.User;
import com.example.sweater.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.UUID;

@Controller
public class MainController {
    @Autowired
    private MessageService messageService;
    @Value("${upload.path}")
    private String uploadPath;
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
    public String getMain(@RequestParam(required = false) String field,
                          Model model)  {
        File uploadDir = new File(uploadPath);
        if(!uploadDir.exists()) {
            uploadDir.mkdir();
        }
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
    public String postMain(@ModelAttribute Message message,
                           @RequestParam("file") MultipartFile file,
                           Model model,
                           Principal principal) throws IOException {
        if(file != null && !file.getOriginalFilename().isBlank()) {
            System.out.println(file.getOriginalFilename());
            String uuidFile = UUID.randomUUID().toString();
            String resultFileName = uuidFile + "." + file.getOriginalFilename();
            file.transferTo(new File(uploadPath + "/" + resultFileName));
            message.setFileName(resultFileName);
        }
        if(principal != null) {
            User user = userService.findByName(principal.getName());
            message.setAuthor(user);
        }
        if(!message.getText().isBlank() || !message.getTag().isBlank())
            messageService.save(message);
        return "redirect:/main";
    }
}
