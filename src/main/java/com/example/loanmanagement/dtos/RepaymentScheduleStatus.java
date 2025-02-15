package com.example.loanmanagement.dtos;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RepaymentScheduleStatus {

    private Integer id;
    private String name;
    private double principalAmount;
    private double interestRate;
    private double loanPeriod;
    private double repaymentFrequency;
    private double monthlyRepayment;
    private  String status;
}
