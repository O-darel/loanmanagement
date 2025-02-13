package com.example.loanmanagement.repositories;

import com.example.loanmanagement.entities.Role;
import com.example.loanmanagement.entities.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository  extends JpaRepository<Role,Integer> {
    Optional<Role> findByName(RoleEnum name);
}
