package com.example.sweater.service;

import com.example.sweater.domain.Role;
import com.example.sweater.domain.User;
import com.example.sweater.repos.UserRepo;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepo userRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User findByName(String username) {
        return userRepo.findByUserName(username);
    }

    @Override
    public void save(User user) {
        userRepo.save(user);
    }

    @Override
    public List<User> findAll() {
        return userRepo.findAll();
    }

    @Override
    public Optional<User> findById(Integer id) {
        return userRepo.findById(id);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByUserName(username);
        if(user == null) {
            throw new UsernameNotFoundException("User not found with name: " + username);
        }
        List<GrantedAuthority> roles = new ArrayList<>();
        for (Role role : user.getRoles()) {
            roles.add(new SimpleGrantedAuthority(role.name()));
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return new org.springframework.security.core.userdetails.User(user.getUserName(), user.getPassword(), roles);
    }
}
