package com.redsocial.controller;

import com.redsocial.dto.UserProfileResponse;
import com.redsocial.dto.UserRegistrationRequest;
import com.redsocial.service.UserService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}/profile")
    public ResponseEntity<UserProfileResponse> getUserProfile(@PathVariable Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        Long authenticatedUserId = userService.findByUsername(username)
                .map(com.redsocial.model.User::getId)
                .orElseThrow(() -> new UsernameNotFoundException("Authenticated user ID not found for username: " + username));

        if (!authenticatedUserId.equals(id)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        return userService.getUserProfile(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/register")
    public ResponseEntity<UserProfileResponse> registerUser(@Valid @RequestBody UserRegistrationRequest request) {
        try {
            UserProfileResponse newUser = userService.registerNewUser(request);
            return new ResponseEntity<>(newUser, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // O un mensaje de error más detallado
        }
    }
}
