package com.zchess.service;

import org.springframework.stereotype.Service;
import com.zchess.entity.User;
import com.zchess.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository repository;

        public UserService(UserRepository repository) {
                this.repository = repository;
                    }

                        public User register(String username, String password) {

                                User user = new User();
                                        user.setUsername(username);
                                                user.setPassword(password);

                                                        return repository.save(user);
                                                            }

                                                                public User login(String username, String password) {

                                                                        return repository.findByUsername(username)
                                                                                        .filter(u -> u.getPassword().equals(password))
                                                                                                        .orElse(null);
                                                                                                            }
                                                                                                            }