package com.example.sweater.service;

import com.example.sweater.domain.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.Optional;


public interface UserService extends UserDetailsService {
    User findByName(String username);

    void save(User user);

    List<User> findAll();

    Optional<User> findById(Integer id);
}
