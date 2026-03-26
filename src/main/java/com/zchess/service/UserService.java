package com.zchess.service;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.stereotype.Service;

import com.zchess.entity.User;
import com.zchess.repository.UserRepository;


@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RatingService ratingService;

    // constructor injection
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder,RatingService ratingService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    this.ratingService=ratingService;
    }

    // register user with encrypted password
    public User registerUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
       User saved=userRepository.save(user);
       
        ratingService.createRating(saved);
        return saved;
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