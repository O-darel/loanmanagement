package com.example.loanmanagement.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoanStatsResponse {

    private long totalLoans;
    private long pendingLoans;
    private long approvedLoans;
    private long rejectedLoans;
    private long disbursedLoans;
    private long paidLoans;
    private double totalPrincipalAmount;      // Sum of all principal amounts
    private double totalOutstandingBalance;   // Sum of remaining balances
}
