package com.redsocial.controller;

import com.redsocial.dto.UserProfileResponse;
import com.redsocial.dto.UserRegistrationRequest;
import com.redsocial.security.CustomUserDetails;
import com.redsocial.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

/**
 * Controlador de Usuarios - Optimizado sin queries adicionales.
 */
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Obtiene el userId del usuario autenticado sin hacer queries a la BD.
     */
    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails) {
            return ((CustomUserDetails) authentication.getPrincipal()).getUserId();
        }
        throw new IllegalStateException("User not authenticated or invalid authentication object");
    }

    /**
     * Obtiene el perfil de un usuario.
     * ✅ OPTIMIZADO: Sin queries adicionales - userId viene del JWT.
     */
    @GetMapping("/{id}/profile")
    public ResponseEntity<UserProfileResponse> getUserProfile(@PathVariable(required = false) Long id) {
        if (id == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        
        Long authenticatedUserId = getCurrentUserId();

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
