package com.redsocial.service;

import com.redsocial.dto.UserProfileResponse;
import com.redsocial.dto.UserRegistrationRequest;
import com.redsocial.model.User;
import com.redsocial.repository.UserRepository;
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
}

