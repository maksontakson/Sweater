package com.example.sweater.controller;

import com.example.sweater.domain.Role;
import com.example.sweater.domain.User;
import com.example.sweater.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;
    @GetMapping
    public String getUsers(Model model) {
        List<User> users = userService.findAll();
        model.addAttribute("users", users);
        return "users";
    }

    @GetMapping("/profile")
    public String getProfile(Model model, Principal principal) {
        User userDto = userService.findByName(principal.getName());
        model.addAttribute("user", userDto);
        return "profile";
    }

    @GetMapping("/{id}")
    public String userEditForm(@PathVariable Integer id, Model model) {
        User userDto = userService.findById(id);
        model.addAttribute("user", userDto);
        model.addAttribute("roles", Role.values());
        return "userEdit";
    }

    @PostMapping("/profile/userSave")
    public String userSaveProfile(@ModelAttribute User user) {
        userService.edit(user);
        SecurityContextHolder.getContext().setAuthentication(null);
        return "redirect:/login";
    }

    @PostMapping("/userSave")
    public String userSave(@ModelAttribute User user) {
        userService.edit(user);
        return "redirect:/users";
    }
}
