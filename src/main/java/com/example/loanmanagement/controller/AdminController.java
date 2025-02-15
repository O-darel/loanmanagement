package com.example.loanmanagement.controller;

import com.example.loanmanagement.dtos.RegisterUserDto;
import com.example.loanmanagement.entities.User;
import com.example.loanmanagement.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/admin")
@RestController
public class AdminController {

    private final UserService userService;

    public AdminController(UserService userService){
        this.userService=userService;
    }

    @PostMapping("/create-agent")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<User> createAgent(
            @RequestBody RegisterUserDto registerUserDto
            ){
        User agent =userService.createAgent(registerUserDto);
        return  ResponseEntity.ok(agent);
    }
}
