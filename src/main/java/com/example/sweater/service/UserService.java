package com.example.sweater.service;

import com.example.sweater.domain.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.Optional;


public interface UserService extends UserDetailsService {
    User findByName(String username);

    boolean save(User user);

    void edit(User user);

    List<User> findAll();

    User findById(Integer id);

    boolean activateUser(String code);

    @Override
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
}
