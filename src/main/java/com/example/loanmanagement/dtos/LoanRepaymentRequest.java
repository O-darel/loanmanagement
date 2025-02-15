package com.example.loanmanagement.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoanRepaymentRequest {
    private Integer id;
    private double paidAmount;
}

