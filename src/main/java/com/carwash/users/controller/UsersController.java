package com.carwash.users.controller;
import com.carwash.users.entity.UsersEntity;
import com.carwash.users.service.UsersService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(value = "*")
public class UsersController {

    private final UsersService usersService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder; // PasswordEncoder inject kar rahe hain

    // Constructor injection
    public UsersController(UsersService usersService, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder) {
        this.usersService = usersService;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UsersEntity users) {
        // fetch email
        UsersEntity user = usersService.getByEmail(users.getEmail()).stream().findFirst().orElse(null);

        if (user != null && passwordEncoder.matches(users.getPassword(), user.getPassword())) { // Mathcing password
            // Create authentication token using email and password
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(users.getEmail(), users.getPassword());

            // Authenticate the user
            Authentication authentication = authenticationManager.authenticate(authenticationToken);

            // Set the authentication in the security context
            SecurityContextHolder.getContext().setAuthentication(authentication);

            return ResponseEntity.status(200).body("{\"message\": \"Login Successful!\", \"status\": 200}");
        }
        return ResponseEntity.status(401).body("{\"message\": \"Invalid Credentials!\", \"status\": 401}");
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UsersEntity users) {
        usersService.registerUser(users);
        return ResponseEntity.status(201).body("{\"message\": \"User registered successfully!\"}");
    }

    @GetMapping
    public String greet() {
        return "Welcome, you are logged in!";
    }
}
