package com.example.loanmanagement.bootstrap;

import com.example.loanmanagement.dtos.RegisterUserDto;
import com.example.loanmanagement.entities.Role;
import com.example.loanmanagement.entities.RoleEnum;
import com.example.loanmanagement.entities.User;
import com.example.loanmanagement.repositories.RoleRepository;
import com.example.loanmanagement.repositories.UserRepository;
import com.example.loanmanagement.services.UserService;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.management.relation.RoleResult;
import java.util.Optional;

@Component
public class AdminSeeder implements ApplicationListener<ContextRefreshedEvent> {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminSeeder(UserRepository userRepository,
                       RoleRepository roleRepository,
                       PasswordEncoder passwordEncoder){
        this.userRepository=userRepository;
        this.roleRepository=roleRepository;
        this.passwordEncoder=passwordEncoder;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent){
        this.createAdmin();
    }

    public  void createAdmin(){
        RegisterUserDto userDto = new RegisterUserDto();
        userDto.setFullName("Admin");
        userDto.setEmail("super.admin@email.com");
        userDto.setPassword("Test@12345");

        Optional<Role> optionalRole = roleRepository.findByName(RoleEnum.ADMIN);
        Optional<User> optionalUser = userRepository.findByEmail(userDto.getEmail());

        if (optionalRole.isEmpty() || optionalUser.isPresent()) {
            return;
        }

        var user = new User();
        user .setFullName(userDto.getFullName());
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setRole(optionalRole.get());

        userRepository.save(user);
    }
}
