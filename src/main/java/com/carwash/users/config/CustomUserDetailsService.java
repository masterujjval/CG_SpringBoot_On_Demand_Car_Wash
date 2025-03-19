package com.carwash.users.config;

import com.carwash.users.entity.UsersEntity;
import com.carwash.users.repository.UsersRepo;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    private final UsersRepo usersRepo;
    private CustomUserDetailsService customUserDetailsService;
    private final PasswordEncoder passwordEncoder;


    public CustomUserDetailsService(UsersRepo usersRepo, PasswordEncoder passwordEncoder) {
        this.usersRepo = usersRepo;
        this.passwordEncoder=passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // fetching email from the database
        UsersEntity user = usersRepo.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        // Return user with email and password (encrypted password)
        return User.builder()
                .username(user.getEmail()) // Here we are using email as username
                .password(user.getPassword()) // Encrypted password
                .roles("USER")  // default role
                .build();
    }

}
