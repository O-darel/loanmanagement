package com.example.loanmanagement.dtos;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
@Getter
@Setter
public class PaymentResponseDto {

    private Integer id;
    private double principalAmount;
    private double interestRate;
    private double loanPeriod;
    private double paidAmount;
    private double balance;
    private LocalDate nextPaymentDate;
    private String status;
}
