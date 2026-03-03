package com.zchess.service;

import com.zchess.entity.Role;
import com.zchess.entity.User;
import com.zchess.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
        private final PasswordEncoder passwordEncoder;

            public UserService(UserRepository userRepository,
                                   PasswordEncoder passwordEncoder) {
                                           this.userRepository = userRepository;
                                                   this.passwordEncoder = passwordEncoder;
                                                       }

                                                           public User registerUser(String username, String password, Role role) {

                                                                   User user = new User();
                                                                           user.setUsername(username);
                                                                                   user.setPassword(passwordEncoder.encode(password));
                                                                                           user.setRole(role);

                                                                                                   return userRepository.save(user);
                                                                                                       }

                                                                                                           public List<User> getAllUsers() {
                                                                                                                   return userRepository.findAll();
                                                                                                                       }

                                                                                                                           public void deleteUser(Long id) {
                                                                                                                                   userRepository.deleteById(id);
                                                                                                                                       }
                                                                                                                                       }