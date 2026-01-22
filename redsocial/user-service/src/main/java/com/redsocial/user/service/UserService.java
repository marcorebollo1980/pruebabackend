package com.redsocial.user.service;

import com.redsocial.user.dto.UserCredentialsResponse;
import com.redsocial.user.dto.UserProfileResponse;
import com.redsocial.user.dto.UserRegistrationRequest;
import com.redsocial.user.model.User;
import com.redsocial.user.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Optional<UserProfileResponse> getUserProfile(Long id) {
        return userRepository.findById(id)
                .map(this::mapToUserProfileResponse);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public Optional<UserCredentialsResponse> findUserCredentialsByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(this::mapToUserCredentialsResponse);
    }

    public UserProfileResponse registerNewUser(UserRegistrationRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Username already exists");
        }

        User newUser = new User();
        newUser.setFirstName(request.getFirstName());
        newUser.setLastName(request.getLastName());
        newUser.setDateOfBirth(request.getDateOfBirth());
        newUser.setAlias(request.getAlias());
        newUser.setUsername(request.getUsername());
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));

        User savedUser = userRepository.save(newUser);
        return mapToUserProfileResponse(savedUser);
    }


    private UserProfileResponse mapToUserProfileResponse(User user) {
        UserProfileResponse dto = new UserProfileResponse();
        dto.setId(user.getId());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setDateOfBirth(user.getDateOfBirth());
        dto.setAlias(user.getAlias());
        dto.setUsername(user.getUsername());
        return dto;
    }

    private UserCredentialsResponse mapToUserCredentialsResponse(User user) {
        UserCredentialsResponse dto = new UserCredentialsResponse();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setPassword(user.getPassword());
        return dto;
    }
}

