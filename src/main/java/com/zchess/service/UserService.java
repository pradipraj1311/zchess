package com.zchess.service;

import com.zchess.entity.User;
import com.zchess.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
        private UserRepository userRepository;

            public User registerUser(User user) {
                    return userRepository.save(user);
                        }

                            public List<User> getAllUsers() {
                                    return userRepository.findAll();
                                        }

                                            public User getUser(Long id) {
                                                    return userRepository.findById(id)
                                                                    .orElseThrow(() -> new RuntimeException("User not found"));
                                                                        }

                                                                            public void deleteUser(Long id) {
                                                                                    userRepository.deleteById(id);
                                                                                        }
                                                                                        }