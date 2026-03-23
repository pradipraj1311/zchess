package com.zchess.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.zchess.entity.User;
import com.zchess.repository.UserRepository;

import org.springframework.security.crypto.password.PasswordEncoder;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // constructor injection
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // register user with encrypted password
    public User registerUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    // get all users
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // get user by id
    public User getUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    // delete user
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    // find user by username
    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }
}