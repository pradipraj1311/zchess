package com.zchess.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zchess.entity.User;
import com.zchess.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService     = userService;
        this.passwordEncoder = passwordEncoder;
    }

 
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        try {
            User saved = userService.registerUser(user);
            // don't return password hash
            saved.setPassword("[PROTECTED]");
            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Registration failed: " + e.getMessage());
        }
    }

    // LOGIN - verify credentials
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user, Authentication auth) {
        User dbUser = userService.findByUsername(user.getUsername());
        if (dbUser == null) {
            return ResponseEntity.status(401).body("User not found");
        }
        if (!passwordEncoder.matches(user.getPassword(), dbUser.getPassword())) {
            return ResponseEntity.status(401).body("Invalid password");
        }
        dbUser.setPassword("[PROTECTED]");
        return ResponseEntity.ok(dbUser);
    }

    // GET ALL USERS - ADMIN ONLY
    @GetMapping
    public ResponseEntity<?> getAllUsers(Authentication auth) {
        boolean isAdmin = auth.getAuthorities()
                .contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
        if (!isAdmin) {
            return ResponseEntity.status(403)
                    .body("Access denied. Admins only can view all users.");
        }
        List<User> users = userService.getAllUsers();
        // hide passwords
        users.forEach(u -> u.setPassword("[PROTECTED]"));
        return ResponseEntity.ok(users);
    }

    // GET USER BY ID - admin can see any, player can only see own profile
    @GetMapping("/{id}")
    public ResponseEntity<?> getUser(@PathVariable Long id, Authentication auth) {
        boolean isAdmin = auth.getAuthorities()
                .contains(new SimpleGrantedAuthority("ROLE_ADMIN"));

        // find logged-in user
        User loggedIn = userService.findByUsername(auth.getName());

        if (!isAdmin && (loggedIn == null || !loggedIn.getId().equals(id))) {
            return ResponseEntity.status(403)
                    .body("Access denied. You can only view your own profile.");
        }

        User user = userService.getUser(id);
        user.setPassword("[PROTECTED]");
        return ResponseEntity.ok(user);
    }

    // GET MY PROFILE - any logged-in user
    @GetMapping("/me")
    public ResponseEntity<?> getMyProfile(Authentication auth) {
        User user = userService.findByUsername(auth.getName());
        if (user == null) return ResponseEntity.status(404).body("User not found");
        user.setPassword("[PROTECTED]");
        return ResponseEntity.ok(user);
    }

    // DELETE USER - ADMIN ONLY
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id, Authentication auth) {
        boolean isAdmin = auth.getAuthorities()
                .contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
        if (!isAdmin) {
            return ResponseEntity.status(403).body("Access denied. Admins only.");
        }
        userService.deleteUser(id);
        return ResponseEntity.ok("User deleted successfully");
    }
}