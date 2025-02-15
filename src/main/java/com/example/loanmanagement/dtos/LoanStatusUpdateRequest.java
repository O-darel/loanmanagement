package com.example.loanmanagement.dtos;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoanStatusUpdateRequest {

    private Integer id;
    private String status;
}
