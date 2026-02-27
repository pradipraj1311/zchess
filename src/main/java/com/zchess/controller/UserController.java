package com.zchess.controller;

import com.zchess.entity.User;
import com.zchess.service.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
public class UserController {

    private final UserService service;

        public UserController(UserService service){
                this.service=service;
                    }

                        @PostMapping("/register")
                            public User register(@RequestParam String u,@RequestParam String p){
                                    return service.register(u,p);
                                        }

                                            @PostMapping("/login")
                                                public User login(@RequestParam String u,@RequestParam String p){
                                                        return service.login(u,p);
                                                            }
                                                            }