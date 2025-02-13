package com.example.loanmanagement.dtos;

import com.example.loanmanagement.entities.RoleEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterUserDto {

    private String fullName;
    private String email;
    private String password;

}
