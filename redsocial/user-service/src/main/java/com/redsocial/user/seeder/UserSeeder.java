package com.redsocial.user.seeder;

import com.redsocial.user.model.User;
import com.redsocial.user.service.UserService;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.time.LocalDate;
import java.util.Arrays;

@Component
public class UserSeeder {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public UserSeeder(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    public void init() {
        if (userService.findByUsername("user1").isEmpty()) {
            User user1 = new User();
            user1.setFirstName("Marco");
            user1.setLastName("Rebollo");
            user1.setDateOfBirth(LocalDate.of(1990, 5, 15));
            user1.setAlias("marquito");
            user1.setUsername("user1");
            user1.setPassword(passwordEncoder.encode("password"));
            userService.save(user1);
        }

        if (userService.findByUsername("user2").isEmpty()) {
            User user2 = new User();
            user2.setFirstName("Ana");
            user2.setLastName("García");
            user2.setDateOfBirth(LocalDate.of(1992, 8, 20));
            user2.setAlias("anita");
            user2.setUsername("user2");
            user2.setPassword(passwordEncoder.encode("password"));
            userService.save(user2);
        }
    }
}

