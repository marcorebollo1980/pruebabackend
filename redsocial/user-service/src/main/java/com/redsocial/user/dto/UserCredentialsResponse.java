package com.redsocial.user.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserCredentialsResponse {
    private Long id;
    private String username;
    private String password;
    // private Set<String> roles;
}

