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

import java.util.*;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final MailSender mailSender;

    public UserServiceImpl(UserRepo userRepo, PasswordEncoder passwordEncoder, MailSender mailSender) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.mailSender = mailSender;
    }

    @Override
    public User findByName(String username) {
        return userRepo.findByUserName(username);
    }

    @Override
    public boolean save(User user) {
        User userFromDb = userRepo.findByUserName(user.getUserName());

        if(userFromDb != null) {
            return false;
        }

        user.setActive(false);
        user.setRoles(Collections.singleton(Role.USER));
        user.setActivationCode(UUID.randomUUID().toString());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepo.save(user);

        if(!user.getEmail().isBlank()) {
            String message = String.format(
                    "Hello, %s \n" +
                        "Welcome to Sweater. Please visit next link: http://localhost:8080/activate/%s",
                    user.getUserName(),
                    user.getActivationCode()
            );
            mailSender.send(user.getEmail(), "Activation code", message);
        }

        return true;
    }

    @Override
    public void edit(User user) {
        Integer id = user.getId();
        user.setActive(false);
        user.setActivationCode(UUID.randomUUID().toString());
        userRepo.save(user);

        if(!user.getEmail().isBlank()) {
            String message = String.format(
                    "Hello, %s \n" +
                            "Welcome to Sweater. Please visit next link: http://localhost:8080/activate/%s",
                    user.getUserName(),
                    user.getActivationCode()
            );
            mailSender.send(user.getEmail(), "Activation code", message);
        }
    }

    @Override
    public List<User> findAll() {
        return userRepo.findAll();
    }

    @Override
    public User findById(Integer id) {
        Optional<User> optional = userRepo.findById(id);
        User user = optional.get();
        return user;
    }

    @Override
    public boolean activateUser(String code) {
        User user = userRepo.findByActivationCode(code);

        if(user == null) {
            return false;
        }

        user.setActive(true);
        user.setActivationCode(null);

        userRepo.save(user);
        return true;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws RuntimeException {
        User user = userRepo.findByUserName(username);
        if(user == null) {
            throw new UsernameNotFoundException("User not found with name: " + username);
        }
        if(!user.isActive()) {
            throw new RuntimeException("User is not activated");
        }
        List<GrantedAuthority> roles = new ArrayList<>();
        for (Role role : user.getRoles()) {
            roles.add(new SimpleGrantedAuthority(role.name()));
        }
//        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return new org.springframework.security.core.userdetails.User(user.getUserName(), user.getPassword(), roles);
    }
}
