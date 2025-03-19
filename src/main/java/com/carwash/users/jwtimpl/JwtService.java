package com.carwash.users.jwtimpl;

import com.carwash.users.entity.UsersEntity;
import com.carwash.users.repository.UsersRepo;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class JwtService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UsersRepo usersRepo;
    private final PasswordEncoder passwordEncoder;


    public JwtService(AuthenticationManager authenticationManager, JwtUtil jwtUtil, UsersRepo usersRepo, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.usersRepo = usersRepo;
        this.passwordEncoder = passwordEncoder;

    }

    public String authenticateAndGetToken(String email, String password){
        // verifying user
        Optional<UsersEntity> user=usersRepo.findByEmail(email);
        if(user.isPresent() && passwordEncoder.matches(password,user.get().getPassword())){
            Authentication authentication=authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email,password));
            return jwtUtil.generateToken(email);
        }
        return null; // if credentials dont match
    }


}
