package com.example.loanmanagement.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoanStatusResponse {
    private Integer id;
    private double principalAmount;
    private double interestRate;
    private double loanPeriod;
    private double repaymentFrequency;
    private double balanceDue;
    private  String status;

}
