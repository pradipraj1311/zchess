package com.zchess.controller;

import com.zchess.entity.Role;
import com.zchess.entity.User;
import com.zchess.service.UserService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

        public UserController(UserService userService){
                this.userService = userService;
                    }

                        @PostMapping("/register")
                            public User register(
                                        @RequestParam String username,
                                                    @RequestParam String password,
                                                                @RequestParam Role role){

                                                                        return userService.registerUser(username,password,role);
                                                                            }

                                                                                @GetMapping
                                                                                    public List<User> getAllUsers(){
                                                                                            return userService.getAllUsers();
                                                                                                }

                                                                                                    @DeleteMapping("/{id}")
                                                                                                        public void deleteUser(@PathVariable Long id){
                                                                                                                userService.deleteUser(id);
                                                                                                                    }
                                                                                                                    }