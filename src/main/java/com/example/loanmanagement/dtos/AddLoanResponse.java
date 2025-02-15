package com.example.loanmanagement.dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class AddLoanResponse {
    private Integer id;
    private String name;
    private String email;
    private double principalAmount;
    private double interestRate;
    private double loanPeriod;
    private double repaymentFrequency;
    private Date applicationDate;
}
