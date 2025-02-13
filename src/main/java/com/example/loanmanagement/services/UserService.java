package com.example.loanmanagement.services;


import com.example.loanmanagement.dtos.RegisterUserDto;
import com.example.loanmanagement.entities.Role;
import com.example.loanmanagement.entities.RoleEnum;
import com.example.loanmanagement.entities.User;
import com.example.loanmanagement.repositories.RoleRepository;
import com.example.loanmanagement.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       RoleRepository roleRepository,
                       PasswordEncoder passwordEncoder){
        this.userRepository=userRepository;
        this.roleRepository=roleRepository;
        this.passwordEncoder=passwordEncoder;

    }

    public List<User> listUserS(){
        List<User> users = new ArrayList<>();
        userRepository.findAll().forEach(users::add);
        return users;
    }

    public User createAgent(RegisterUserDto input){
        Optional<Role> optionalRole = roleRepository.findByName(RoleEnum.AGENT);

        if (optionalRole.isEmpty()) {
            return null;
        }
        User agent=new User();
        agent.setEmail(input.getEmail());
        agent.setFullName(input.getFullName());
        agent.setRole(optionalRole.get());

        //encoding password before storing
        agent.setPassword(passwordEncoder.encode(input.getPassword()));
        return userRepository.save(agent);
    }
}
