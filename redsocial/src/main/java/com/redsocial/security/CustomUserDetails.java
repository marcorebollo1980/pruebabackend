package com.redsocial.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

/**
 * CustomUserDetails extiende Spring Security User para incluir userId.
 * Esto permite acceder al ID del usuario sin hacer queries adicionales.
 */
public class CustomUserDetails extends User {
    
    private final Long userId;

    public CustomUserDetails(String username, String password, 
                            Collection<? extends GrantedAuthority> authorities, 
                            Long userId) {
        super(username, password, authorities);
        this.userId = userId;
    }

    public CustomUserDetails(String username, String password, 
                            boolean enabled, boolean accountNonExpired,
                            boolean credentialsNonExpired, boolean accountNonLocked,
                            Collection<? extends GrantedAuthority> authorities, 
                            Long userId) {
        super(username, password, enabled, accountNonExpired, 
              credentialsNonExpired, accountNonLocked, authorities);
        this.userId = userId;
    }

    /**
     * Obtiene el ID del usuario autenticado.
     * @return ID del usuario
     */
    public Long getUserId() {
        return userId;
    }
}
