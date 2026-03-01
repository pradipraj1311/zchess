package com.zchess.controller;

import org.springframework.web.bind.annotation.*;
import com.zchess.entity.User;
import com.zchess.service.UserService;

@RestController
@RequestMapping("/api/user")
@CrossOrigin
public class UserController {

    private final UserService service;

        public UserController(UserService service) {
                this.service = service;
                    }

                        @PostMapping("/register")
                            public User register(@RequestParam String username,
                                                     @RequestParam String password) {
                                                             return service.register(username, password);
                                                                 }

                                                                     @PostMapping("/login")
                                                                         public User login(@RequestParam String username,
                                                                                               @RequestParam String password) {
                                                                                                       return service.login(username, password);
                                                                                                           }
                                                                                                           }