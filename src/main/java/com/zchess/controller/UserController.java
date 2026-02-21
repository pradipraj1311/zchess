package com.zchess.controller;

import org.springframework.web.bind.annotation.*;
import com.zchess.repository.UserRepository;
import com.zchess.entity.User;

@RestController
@RequestMapping("/api/users")
public class UserController {


    private final UserRepository userRepository;
    
        // constructor injection (manual)
            public UserController(UserRepository userRepository) {
                    this.userRepository = userRepository;
                        }
                        
                            @GetMapping("/create")
                                public User createUser() {
                                        User u = new User();
                                                u.setUsername("pradip");
                                                        u.setPassword("123");
                                                                return userRepository.save(u);
                                                                    }
                                                                    
                                                                        @GetMapping("/{id}")
                                                                            public User getUser(@PathVariable Long id) {
                                                                                    return userRepository.findById(id).orElse(null);
                                                                                        }
                                                                                        }                                                   