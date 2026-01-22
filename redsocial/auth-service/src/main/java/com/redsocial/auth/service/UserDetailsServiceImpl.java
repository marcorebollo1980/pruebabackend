package com.redsocial.auth.service;

import com.redsocial.user.dto.UserCredentialsResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final RestTemplate restTemplate;

    @Value("${user.service.url}")
    private String userServiceUrl;

    public UserDetailsServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String url = userServiceUrl + "/users/internal/credentials/" + username;

        UserCredentialsResponse userCredentials = restTemplate.getForObject(url, UserCredentialsResponse.class);

        if (userCredentials == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }

        return new User(userCredentials.getUsername(), userCredentials.getPassword(), new ArrayList<>());
    }
}

