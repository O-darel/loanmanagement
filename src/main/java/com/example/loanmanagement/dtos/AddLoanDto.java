package com.example.loanmanagement.dtos;

import com.example.loanmanagement.entities.Loan;
import com.example.loanmanagement.entities.User;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class AddLoanDto {
    //private Integer userId;
    private double principalAmount;
    private double interestRate;
    private double loanPeriod;
    private double repaymentFrequency;
    //private Loan.LoanStatus status; //optional
    //private LocalDate applicationDate;


}
