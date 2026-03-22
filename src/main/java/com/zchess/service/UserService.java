package com.zchess.service;

import com.zchess.entity.User;
import com.zchess.entity.Role;
import com.zchess.repository.UserRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    //  constructor injection
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    //  REGISTER USER
    public User registerUser(User user) {

        //  username check
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }

        //  password validation
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            throw new RuntimeException("Password cannot be empty");
        }

        //  default role
        if (user.getRole() == null) {
            user.setRole(Role.USER);
        }

        //  encode password
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepository.save(user);
    }

    //  GET ALL USERS
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    //  GET USER BY ID
    public User getUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    //  DELETE USER
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}