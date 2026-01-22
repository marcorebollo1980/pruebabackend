package com.redsocial.user.controller;

import com.redsocial.user.dto.UserCredentialsResponse;
import com.redsocial.user.dto.UserProfileResponse;
import com.redsocial.user.dto.UserRegistrationRequest;
import com.redsocial.user.service.UserService;
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

        Long authenticatedUserId;
        try {
            authenticatedUserId = Long.parseLong(username);
        } catch (NumberFormatException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        if (!authenticatedUserId.equals(id)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        return userService.getUserProfile(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/internal/credentials/{username}")
    public ResponseEntity<UserCredentialsResponse> getUserCredentials(@PathVariable String username) {
        return userService.findUserCredentialsByUsername(username)
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

