package com.example.sweater.controller;

import com.example.sweater.domain.Role;
import com.example.sweater.domain.User;
import com.example.sweater.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/{id}")
    public String userEditForm(@PathVariable Integer id, Model model) {
        Optional<User> user = userService.findById(id);
        User userDto = user.get();
        model.addAttribute("user", userDto);
        model.addAttribute("roles", Role.values());
        return "userEdit";
    }

    @PostMapping("/userSave")
    public String userSave(@ModelAttribute User user) {
        User userFromDb = userService.findByName(user.getUserName());
        userFromDb.setUserName(user.getUserName());
        userFromDb.setPassword(user.getPassword());
        userFromDb.setRoles(user.getRoles());
        userService.save(userFromDb);
        return "redirect:/users";
    }
}
