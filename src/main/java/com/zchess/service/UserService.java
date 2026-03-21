package com.zchess.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import com.zchess.entity.User;
import com.zchess.repository.UserRepository;

@Service
public class UserService {

    @Autowired
        private UserRepository userRepository;

            public User registerUser(@NonNull User user) {
                    return userRepository.save(user);
                        }

                            public List<User> getAllUsers() {
                                    return userRepository.findAll();
                                        }

                                            public User getUser(@NonNull Long id) {
                                                    return userRepository.findById(id)
                                                                    .orElseThrow(() -> new RuntimeException("User not found"));
                                                                        }

                                                                            public void deleteUser(@NonNull Long id) {
                                                                                    userRepository.deleteById(id);
                                                                                        }
                                                                                        }