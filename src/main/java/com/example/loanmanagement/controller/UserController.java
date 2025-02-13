package com.example.loanmanagement.controller;

import com.example.loanmanagement.entities.User;
import com.example.loanmanagement.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Security;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    //private final Logger logger;

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    public UserController(UserService userService){
        this.userService=userService;
        //this.logger = LoggerFactory.getLogger(UserController.class);
    }

    //list all users
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'AGENT')") //FOR AGENT AND ADMIN ONLY
    public ResponseEntity<List<User>> listUsers(){
        logger.debug("Getting users");
        List<User> users= userService.listUserS();

        return ResponseEntity.ok(users);
    }

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<User> getUser(){
        logger.debug("Fetching me");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser=(User) authentication.getPrincipal();
        return  ResponseEntity.ok(currentUser);
    }

}
