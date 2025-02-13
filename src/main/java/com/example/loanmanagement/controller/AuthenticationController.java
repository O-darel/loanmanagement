package com.example.loanmanagement.controller;


import com.example.loanmanagement.dtos.LoginResponse;
import com.example.loanmanagement.dtos.LoginUserDto;
import com.example.loanmanagement.dtos.RegisterUserDto;
import com.example.loanmanagement.entities.User;
import com.example.loanmanagement.services.AuthenticationService;
import com.example.loanmanagement.services.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/auth")
@RestController
public class AuthenticationController {
    private  final JwtService jwtService;
    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService,
                                    JwtService jwtService){
        this.authenticationService=authenticationService;
        this.jwtService=jwtService;
    }

    //register endpoint
    @PostMapping("/register")
    public ResponseEntity<User> register (
            @RequestBody RegisterUserDto registerUserDto
            ){
        User registeredUser=authenticationService.signup(registerUserDto);
        return ResponseEntity.ok(registeredUser);
    }

    //loging endpoint
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @RequestBody LoginUserDto loginUserDto
            ){
        User authenticatedUser = authenticationService.authenticate(loginUserDto);
        String jwtToken = jwtService.generateToken(authenticatedUser);
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setToken(jwtToken);
        loginResponse.setExpiresIn(jwtService.getExpirationTime());

        return ResponseEntity.ok(loginResponse);
    }
}