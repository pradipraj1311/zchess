package com.zchess.service;

import com.zchess.entity.User;
import com.zchess.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository repo;

        public UserService(UserRepository repo){
                this.repo = repo;
                    }

                        public User register(String username, String password){
                                User u = new User();
                                        u.setUsername(username);
                                                u.setPassword(password);
                                                        return repo.save(u);
                                                            }

                                                                public User login(String username, String password){
                                                                        return repo.findByUsernameAndPassword(username,password).orElse(null);
                                                                            }
                                                                            }