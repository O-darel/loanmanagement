package com.example.loanmanagement.Helpers;

import com.example.loanmanagement.entities.Loan;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

public class LoanStatusTransition {
    private static final Map<Loan.LoanStatus, Set<Loan.LoanStatus>> validTransitions = new EnumMap<>(Loan.LoanStatus.class);

    static {
        validTransitions.put(Loan.LoanStatus.PENDING, EnumSet.of(Loan.LoanStatus.APPROVED, Loan.LoanStatus.REJECTED));
        validTransitions.put(Loan.LoanStatus.APPROVED, EnumSet.of(Loan.LoanStatus.DISBURSED));
        validTransitions.put(Loan.LoanStatus.DISBURSED, EnumSet.of(Loan.LoanStatus.PAID));
        validTransitions.put(Loan.LoanStatus.REJECTED, EnumSet.noneOf(Loan.LoanStatus.class)); // No transitions from REJECTED
        validTransitions.put(Loan.LoanStatus.PAID, EnumSet.noneOf(Loan.LoanStatus.class)); // No transitions from PAID
    }

    public static boolean isValidTransition(Loan.LoanStatus current, Loan.LoanStatus next) {
        return validTransitions.getOrDefault(current, EnumSet.noneOf(Loan.LoanStatus.class)).contains(next);
    }
}
