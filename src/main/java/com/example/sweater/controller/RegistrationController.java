package com.example.sweater.controller;

import com.example.sweater.domain.Role;
import com.example.sweater.domain.User;
import com.example.sweater.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Collections;

@Controller
public class RegistrationController {
    @Autowired
    private UserService userService;

    @GetMapping("/registration")
    public String registration(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        return "registration";
    }
    @PostMapping("/registration")
    public String addUser(User user, Model model) throws NoSuchFieldException {
        User userFromDb = userService.findByName(user.getUserName());
        if(userFromDb != null) {
            model.addAttribute("model", "User exists!");
//            throw new NoSuchFieldException("There is already exists user with the specified name");
            return "registration";
        }
        user.setActive(true);
        user.setRoles(Collections.singleton(Role.USER));
        userService.save(user);
        return "redirect:/login";
    }
}
