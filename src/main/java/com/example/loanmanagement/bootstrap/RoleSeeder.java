package com.example.loanmanagement.bootstrap;

import com.example.loanmanagement.entities.Role;
import com.example.loanmanagement.entities.RoleEnum;
import com.example.loanmanagement.repositories.RoleRepository;
import org.hibernate.event.spi.RefreshContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

@Component
public class RoleSeeder implements ApplicationListener<ContextRefreshedEvent > {

    private final RoleRepository roleRepository;

    public RoleSeeder( RoleRepository roleRepository){
        this.roleRepository=roleRepository;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent){
        this.loadRoles();
    }

    public void loadRoles(){
        RoleEnum[] roleNames = new RoleEnum[] { RoleEnum.USER,RoleEnum.AGENT, RoleEnum.ADMIN };
        Map<RoleEnum,String> roleDescription= Map.of(
                RoleEnum.USER, "Default user role",
                RoleEnum.AGENT, "Agent role",
                RoleEnum.ADMIN, "Administrator role"
        );
        Arrays.stream(roleNames).forEach((roleName)->{
            Optional<Role> optionalRole=roleRepository.findByName(roleName);

            optionalRole.ifPresentOrElse(System.out::println,()->{
                Role roleToCreate=new Role();
                roleToCreate.setName(roleName);
                roleToCreate.setDescription(roleDescription.get(roleName));
                roleRepository.save(roleToCreate);

            });

        });
    }
}
