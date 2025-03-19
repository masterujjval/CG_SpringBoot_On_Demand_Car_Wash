package com.carwash.users.service;
import com.carwash.users.entity.UsersEntity;
import com.carwash.users.repository.UsersRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;
import java.util.List;
import java.util.Optional;

@Service
@CrossOrigin(value = "*")
public class UsersService {

    @Autowired
    public  UsersRepo userRepo;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


    //to register users
    public void registerUser(UsersEntity users) {
        // Encode the password before saving to DB
        String encodedPassword=passwordEncoder.encode(users.getPassword());
        users.setPassword(encodedPassword);
        userRepo.save(users); // Save user in database
        System.out.println("User registered successfully!");
    }
    // to get user using their initials
   public Optional<UsersEntity> getByEmail(String email){
        return userRepo.findByEmail(email);
   }
    // get all users
    public List<UsersEntity> getAllUsers(){
        return userRepo.findAll();
    }
    // here we will also write code to get car wash history of the user


}
