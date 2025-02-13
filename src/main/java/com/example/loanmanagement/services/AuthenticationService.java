package com.example.loanmanagement.services;

import com.example.loanmanagement.dtos.LoginUserDto;
import com.example.loanmanagement.dtos.RegisterUserDto;
import com.example.loanmanagement.entities.Role;
import com.example.loanmanagement.entities.RoleEnum;
import com.example.loanmanagement.entities.User;
import com.example.loanmanagement.repositories.RoleRepository;
import com.example.loanmanagement.repositories.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final RoleRepository roleRepository;

    public AuthenticationService(UserRepository userRepository,
                                 PasswordEncoder passwordEncoder,
                                 AuthenticationManager authenticationManager,
                                 RoleRepository roleRepository){
        this.userRepository=userRepository;
        this.passwordEncoder=passwordEncoder;
        this.authenticationManager=authenticationManager;
        this.roleRepository=roleRepository;
    }

    //sign up service
    public User signup(RegisterUserDto input){

        Optional<Role> optionalRole = roleRepository.findByName(RoleEnum.USER);

        if (optionalRole.isEmpty()) {
            return null;
        }

        User user=new User();
        user.setEmail(input.getEmail());
        user.setFullName(input.getFullName());
        user.setRole(optionalRole.get());
        //encoding password before storing
        user.setPassword(passwordEncoder.encode(input.getPassword()));
        //save
        return userRepository.save(user);
    }

    //login service
    public User authenticate(LoginUserDto input){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.getEmail(),
                        input.getPassword()
                )
        );

        return userRepository.findByEmail(input.getEmail())
                .orElseThrow();
    }

}
