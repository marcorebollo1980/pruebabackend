package com.redsocial.service;

import com.redsocial.model.User;
import com.redsocial.repository.UserRepository;
import com.redsocial.security.CustomUserDetails;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Carga el usuario por username y retorna CustomUserDetails con userId incluido.
     * Esto evita tener que hacer queries adicionales en los controladores.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
        
        // Retorna CustomUserDetails con el userId para evitar queries posteriores
        return new CustomUserDetails(
            user.getUsername(), 
            user.getPassword(), 
            new ArrayList<>(),
            user.getId()  // Incluye el userId
        );
    }
}

