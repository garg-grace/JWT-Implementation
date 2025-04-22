package com.example.jwtdemo.user;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody UserAuth userAuthDetails){

        userAuthDetails.setPassword(passwordEncoder.encode(userAuthDetails.getPassword()));

        userService.save(userAuthDetails);

        return ResponseEntity.ok("user registered successfully");
    }

    @GetMapping("/home")
    public String getUserDetails(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return "fetched user details successfully";
    }
}
